package com.practicum.playlistmaker

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class MediaActivity : AppCompatActivity() {

    //Переменные
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var track: Track
    lateinit var sharedPrefDataTrack: SharedPreferences
    lateinit var artworkUrl100: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var trackTime: TextView
    lateinit var collectionName: TextView
    lateinit var releaseDate: TextView
    lateinit var primaryGenreName: TextView
    lateinit var country: TextView
    lateinit var time: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        //Присвоить значение переменным
        initViews()

        //Listener
        setListeners()

        //Отображение данных трека
        showDataTrack()
    }

    //Настроить Listeners
    private fun setListeners() {
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener() {
            finish()
        }
    }

    //Инициализация переменных
    private fun initViews() {
        //Кнопка "<-" из окна "Настройки"
        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)
        sharedPrefDataTrack = getSharedPreferences(DATA_TRACK, MODE_PRIVATE)
        artworkUrl100 = findViewById(R.id.artwork_url_100)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.track_time_data)
        collectionName = findViewById(R.id.collection_name_data)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name_data)
        country = findViewById(R.id.country_data)
        time = findViewById(R.id.duration) }

    //Отображение данных трека
    private fun showDataTrack(){
        val jsonTrack = sharedPrefDataTrack.getString(DATA_TRACK, null)
        if (jsonTrack == null) return
        track = Gson().fromJson(jsonTrack, Track::class.java)
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        time.text = "0:00"

        val roundingRadius = this.resources.getDimensionPixelSize(R.dimen.roundingRadiusPlayer)
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundingRadius))
            .into(artworkUrl100)
    }
}