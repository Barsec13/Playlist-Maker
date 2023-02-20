package com.practicum.playlistmaker
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    //Переменная для работы с вводимым запросом
    var textSearch = ""
    //Переменные для работы с UI
    lateinit var searchEditText: EditText
    lateinit var searchClearIcon: ImageView
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var newsAdapter: TrackAdapter
    lateinit var placeholderNothingWasFound: LinearLayout
    lateinit var placeholderCommunicationsProblem: LinearLayout
    lateinit var buttonReturn: Button
    //Базовый URL iTunes Search API
    private var baseURLiTunesSearchAPI = "https://itunes.apple.com"


    //Подключаем Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseURLiTunesSearchAPI)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val serviceiTunesSearch = retrofit.create(iTunesSearchAPI::class.java)

    private val tracks = ArrayList<Track>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Список треков
//        val tracks : ArrayList<Track> = arrayListOf(
//            Track("Smells Like Teen Spirit", "Nirvana", "5:01",
//                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/" +
//                        "7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
//            Track("Billie Jean", "Michael Jackson", "4:35",
//                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811" +
//                        "-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
//            Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/" +
//                    "image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
//            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic." +
//                    "com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
//            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic" +
//                    ".com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
//        )

        //RecyclerView
        setAdapter(tracks)

        //Присвоить значение переменным
        initViews()

        //Listener
        setListeners()

        //Работа с вводимым текстом
        inputText()
    }

    //Присвоить значение переменным
    private fun initViews(){
        searchClearIcon = findViewById(R.id.searchClearIcon)
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.setText(textSearch)
        placeholderNothingWasFound = findViewById(R.id.placeholderNothingWasFound)
        placeholderCommunicationsProblem = findViewById(R.id.placeholderCommunicationsProblem)
        buttonReturn = findViewById(R.id.button_return)

        //Кнопка "<-" из окна "Настройки"
        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)
    }

    //Настроить Listeners
    private fun setListeners(){
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener(){
            finish()
        }

        searchClearIcon.setOnClickListener {
            //Объект для работы с клавиатурой
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            //Очистить поле для ввода
            searchEditText.setText("")
            //Скрыть клавиатуру
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            //Убрать информацию о неудачных запросах
            placeholderNothingWasFound.visibility = View.INVISIBLE
            placeholderNothingWasFound.visibility = View.INVISIBLE
            //Очистить найденный список треков
            tracks.clear()
            newsAdapter.notifyDataSetChanged()
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

    }

    //Настроить RecyclerView
    fun setAdapter(tracks : ArrayList<Track>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        newsAdapter = TrackAdapter(tracks)
        recyclerView.adapter = newsAdapter
    }

    //Работа с вводимым текстом
    private fun inputText(){
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            //Действие при вводе текста
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchClearIcon.visibility = searchClearIconVisibility(s)
                textSearch = searchEditText.getText().toString()
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

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
    }

    //Поиск трека черезе Retrofit
    private fun searchTrack(){
        serviceiTunesSearch.searchTrack(searchEditText.text.toString())
            .enqueue(object : Callback<TrackResponse>{
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (textSearch.isNotEmpty() && !response.body()?.results.isNullOrEmpty() && response.code() == 200){
                            tracks.clear()
                            tracks.addAll(response.body()?.results!!)
                            newsAdapter.notifyDataSetChanged()
                            placeholderNothingWasFound.visibility = View.INVISIBLE
                            placeholderCommunicationsProblem.visibility = View.INVISIBLE
                    }
                    else{
                        placeholderNothingWasFound.visibility = View.VISIBLE
                        placeholderCommunicationsProblem.visibility = View.INVISIBLE
                    }

                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    placeholderCommunicationsProblem.visibility = View.VISIBLE
                    placeholderNothingWasFound.visibility = View.INVISIBLE
                }
            })
    }
}

