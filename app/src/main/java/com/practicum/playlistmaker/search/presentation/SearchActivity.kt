package com.practicum.playlistmaker.search.presentation

import android.content.Context
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
import com.practicum.playlistmaker.search.domain.models.NetworkResponse
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.media.domain.model.Track
import com.practicum.playlistmaker.Constant.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker.Constant.SEARCH_DEBOUNCE_DELAY
import com.practicum.playlistmaker.search.creator.CreatorSearch

const val HISTORY_TRACKS_SHARED_PREF = "history_tracks_shared_pref"

class SearchActivity : AppCompatActivity(), SearchViewActivity {

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
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewHistory: RecyclerView

    private lateinit var searchPresenter: SearchPresenter

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Присвоить значение переменным
        initViews()

        //Инициализация истории поиска
        initSearchHistory()

        searchPresenter = CreatorSearch.provideSearchPresenter(
            viewSearch = this,
            view = this,
            sharedPreferences = sharedPref,
        )

        //RecyclerView
        initAdapter()

        //Listener
        setListeners()

        visibleHistoryTrack()
    }

    //Присвоить значение переменным
    private fun initViews() {
        searchClearIcon = findViewById(R.id.searchClearIcon)
        searchEditText = findViewById(R.id.searchEditText)
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonReturn = findViewById(R.id.button_return)
        historyList = findViewById(R.id.history_list)
        buttonClear = findViewById(R.id.button_clear_history)
        progressBar = findViewById(R.id.progressBar)
        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        searchEditText.setText("")

    }

    private fun textSearch(): String {
        return searchEditText.getText().toString()
    }

    private fun initSearchHistory() {
        //Shared Preferences
        sharedPref = getSharedPreferences(HISTORY_TRACKS_SHARED_PREF, MODE_PRIVATE)
    }

    //Настроить RecyclerView
    private fun initAdapter() {
        tracksAdapter = TrackAdapter(ArrayList<Track>())
        recyclerView.adapter = tracksAdapter

        tracksHistoryAdapter = TrackHistoryAdapter(ArrayList<Track>())
        recyclerViewHistory.adapter = tracksHistoryAdapter
    }

    //Настроить Listeners
    private fun setListeners() {
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener() {
            searchPresenter.btnArrowBackClick()
        }

        //Очистка истории поиска
        buttonClear.setOnClickListener() {
            searchPresenter.clickButtonClearHistory()
        }

        //Поиск трека
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !textSearch().equals("")) {
                searchTracks()
                true
            }
            false
        }

        //Очистить строку поиска
        searchClearIcon.setOnClickListener {
            searchPresenter.clearSearchText()
        }

        //Повторить предыдущий запрос после нажатия на кнопку "Обновить"
        buttonReturn.setOnClickListener() {
            searchTracks()
        }

        //Связать поля для ввода и TextWatcher
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            //Действие при вводе текста
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                if (s?.isEmpty() == false) searchDebounce()

                //Убрать историю поиска с кнопкой и показать значек очисткиЫ, если ввод текста в фокусе и не пустой
                searchClearIcon.visibility =
                    if (s.isNullOrEmpty()) {
                        View.GONE
                    } else {
                        View.VISIBLE
                    }

                historyList.visibility =
                    if (searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE
                    else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        })

        //Обработать нажатие на View трека в поиске
        tracksAdapter.itemClickListener = { position, track ->
            if (clickDebounce()) {
                searchPresenter.onTrackClick(track, position)
            }
        }

        //Обработать нажатие на View трека в истории поиска
        tracksHistoryAdapter.itemClickListener = { position, track ->
            if (clickDebounce()) {
                searchPresenter.onTrackClick(track, position)
            }
        }
    }

    override fun showLoad() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideLoad() {
        progressBar.visibility = View.GONE
    }

    override fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        //Скрыть клавиатуру
        inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    fun searchTracks() {
        searchPresenter.loadTracks(textSearch())
    }

    //Запускаем поиск, если пользователь 2 секунды не вводит текст
    private val searchRunnable = Runnable { searchTracks() }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    //Ограничение двойного нажатия на трек для открытия плеера
    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun refreshHistory(historyTracks: List<Track>) {
        tracksHistoryAdapter.setTracks(
            searchPresenter.tracksHistoryFromJson()
        )
    }

    override fun clearTextSearch() {
        searchEditText.setText("")
    }

    //Отображение истории поиска
    private fun visibleHistoryTrack() {
        searchPresenter.visibleHistoryTrack()
        //Скрыть сообщения об ошибках
    }

    override fun hideMessageError() {
        placeholderNothingWasFound.isVisible = false
        placeholderCommunicationsProblem.isVisible = false
    }

    override fun showHistoryList() {
        historyList.visibility = View.VISIBLE
    }

    override fun hideHistoryList() {
        historyList.visibility = View.GONE
    }

    override fun showTracks(tracks: List<Track>) {
        tracksAdapter.setTracks(tracks)
    }

    //Обработка результатов запроса
    override fun showMessageError(networkResponse: NetworkResponse) {

        when (networkResponse) {
            is NetworkResponse.SuccessRequest -> {
                placeholderNothingWasFound.isVisible = false
                placeholderCommunicationsProblem.isVisible = false
            }

            is NetworkResponse.NoData -> {
                placeholderNothingWasFound.isVisible = true
                placeholderCommunicationsProblem.isVisible = false
            }

            is NetworkResponse.NoInternet -> {
                placeholderCommunicationsProblem.isVisible = true
                placeholderNothingWasFound.isVisible = false
            }

            else -> {
                placeholderNothingWasFound.isVisible = true
                placeholderCommunicationsProblem.isVisible = false
            }
        }
    }
}