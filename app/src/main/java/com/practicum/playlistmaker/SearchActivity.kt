package com.practicum.playlistmaker
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val HISTORY_TRACKS_SHARED_PREF = "history_tracks_shared_pref"
const val DATA_TRACK = "dataTrack"

class SearchActivity : AppCompatActivity() {
    //Переменная для работы с вводимым запросом
    var textSearch = ""
    //Переменные для работы с UI
    lateinit var searchEditText: EditText
    lateinit var searchClearIcon: ImageView
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var tracksAdapter: TrackAdapter
    lateinit var tracksHistoryAdapter: TrackHistoryAdapter
    lateinit var placeholderNothingWasFound: LinearLayout
    lateinit var placeholderCommunicationsProblem: LinearLayout
    lateinit var buttonReturn: Button
    lateinit var historyList: LinearLayout
    lateinit var buttonClear: Button
    lateinit var sharedPref: SharedPreferences
    lateinit var searchHistory: SearchHistory
    lateinit var handler: Handler
    lateinit var thread: Thread
    lateinit var progressBar: ProgressBar


    //Базовый URL iTunes Search API
    private val baseURLiTunesSearchAPI = "https://itunes.apple.com"

    //Подключаем Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURLiTunesSearchAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceiTunesSearch = retrofit.create(iTunesSearchAPI::class.java)

    var tracks = ArrayList<Track>()
    var historyTracks  = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Присвоить значение переменным
        initViews()

        //RecyclerView
        setAdapter(tracks)
        setAdapterHistory(historyTracks)

        visibleHistoryTrack()

        //Listener
        setListeners()

        //Работа с вводимым текстом
        inputText()
    }

    companion object {
        private const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    //Запускаем поиск, если пользователь 2 секунды не вводит текст
    private val searchRunnable = Runnable {searchTrack() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    //Ограничение двойного нажатия на трек для открытия плеера
    private var isClickAllowed = true

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    //Присвоить значение переменным
    private fun initViews(){
        searchClearIcon = findViewById(R.id.searchClearIcon)
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setText(textSearch)
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonReturn = findViewById(R.id.button_return)
        historyList = findViewById(R.id.history_list)
        buttonClear = findViewById(R.id.button_clear_history)
        progressBar = findViewById(R.id.progressBar)

        //Shared Preferences
        sharedPref = getSharedPreferences(HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)

        //Объект класса для работы с историей поиске
        searchHistory = SearchHistory(sharedPref)

        //Присвоить значение из сохраненного поиска треков
        historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>

        //Кнопка "<-" из окна "Настройки"
        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)

        //Handler
        handler = Handler(Looper.getMainLooper())
    }

    //Настроить Listeners
    private fun setListeners(){
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener(){
            finish()
        }

        //Очистка истории поиска
        buttonClear.setOnClickListener(){
            searchHistory.clearHistory()
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            tracksHistoryAdapter.tracksHistory.clear()
            tracksHistoryAdapter.notifyDataSetChanged()
            historyList.visibility = View.GONE
        }

        searchClearIcon.setOnClickListener {
            hideKeyboard()
            //Очистить поле для ввода
            searchEditText.setText("")

            //Убрать информацию о неудачных запросах
            placeholderNothingWasFound.isVisible  = false
            placeholderCommunicationsProblem.isVisible = false

            //Очистить найденный список треков
            tracks.clear()
            tracksAdapter.notifyDataSetChanged()

            //Показать историю поисков
            historyList.visibility = View.VISIBLE
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            tracksHistoryAdapter.tracksHistory = historyTracks
            tracksHistoryAdapter.notifyDataSetChanged()
        }

        //Поиск трека
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrack()
                true
            }
            false
        }

        //Повторить предыдущий запрос после нажатия на кнопку "Обновить"
        buttonReturn.setOnClickListener(){
            placeholderCommunicationsProblem.visibility = View.INVISIBLE
            searchTrack()
        }

        //Обработать нажатие на View трека в поиске
        tracksAdapter.itemClickListener = { position, track ->
            openMedia(track,position)
        }

        tracksHistoryAdapter.itemClickListener = { position, track ->
            openMedia(track,position)
        }
    }

    //Настроить RecyclerView
    fun setAdapter(tracks : ArrayList<Track>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        tracksAdapter = TrackAdapter(tracks)
        recyclerView.adapter = tracksAdapter
    }

    fun setAdapterHistory(historyTracks : ArrayList<Track>){
        val recyclerViewHistory = findViewById<RecyclerView>(R.id.recyclerViewHistory)
        tracksHistoryAdapter = TrackHistoryAdapter(historyTracks)
        recyclerViewHistory.adapter = tracksHistoryAdapter
    }

    //Отображение истории поиска
    private fun visibleHistoryTrack(){
        if (historyTracks.isNotEmpty()) historyList.visibility = View.VISIBLE
        else historyList.visibility = View.GONE
    }

    //Работа с вводимым текстом
    private fun inputText(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            //Действие при вводе текста
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchDebounce()
                searchClearIcon.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.getText().toString()

                //Убрать историю поиска с кнопкой, если ввод текста в фокусе и не пустой
                historyList.visibility = if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE
                else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        //Связать поля для ввода и TextWatcher
        searchEditText.addTextChangedListener(simpleTextWatcher)
    }

    //Скрыть или показать кнопку для сброса ввода
    private fun searchClearIconVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    //Cохранить уже имеющееся состояние Activity
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_SEARCH,textSearch)
    }

    //Получить сохранённое значение Activity из onSaveInstanceState
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textSearch = savedInstanceState.getString(TEXT_SEARCH).toString()
        searchEditText.setText(textSearch)
    }

    //Поиск трека черезе Retrofit
    private fun searchTrack(){
        progressBar.visibility = View.VISIBLE
        serviceiTunesSearch.searchTrack(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>) {
                    if (response.code() == 200) {
                        if (!response.body()?.results.isNullOrEmpty()){
                            progressBar.visibility = View.GONE
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            tracksAdapter.notifyDataSetChanged()
                            showMessageError(NetworkError.SuccessRequest())
                        }
                        else showMessageError(NetworkError.NoData())
                    } else showMessageError(NetworkError.ServerError())
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    showMessageError(NetworkError.NoInternet())
                }
            })
    }

    //Обработка результатов запроса
    fun showMessageError(networkError: NetworkError){
        progressBar.visibility = View.GONE
        when (networkError){
            is NetworkError.SuccessRequest ->{
                placeholderNothingWasFound.isVisible = false
                placeholderCommunicationsProblem.isVisible = false
            }
            is NetworkError.NoData ->{
                placeholderNothingWasFound.isVisible = true
                placeholderCommunicationsProblem.isVisible = false
            }

            is NetworkError.NoInternet -> {
                placeholderCommunicationsProblem.isVisible = true
                placeholderNothingWasFound.isVisible = false
            }

            else -> {}
        }
    }

    //Передача данных через intent
    private fun sendToMedia(track : Track){
        val searchIntent = Intent(this@SearchActivity, MediaActivity::class.java).apply {
            putExtra(SEND_TRACK, Gson().toJson(track))
        }
        startActivity(searchIntent)
    }

    private fun openMedia(track: Track,position: Int) {
        if (clickDebounce()) {
            searchHistory.addTrack(track, position)
            historyTracks = searchHistory.tracksHistoryFromJson() as ArrayList<Track>
            tracksHistoryAdapter.tracksHistory.clear()
            tracksHistoryAdapter.tracksHistory = historyTracks
            tracksHistoryAdapter.notifyDataSetChanged()
            sendToMedia(track)
        }
    }

    private fun hideKeyboard(){
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        //Скрыть клавиатуру
        inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }
}