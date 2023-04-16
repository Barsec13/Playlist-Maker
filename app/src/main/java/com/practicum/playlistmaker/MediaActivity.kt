package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.practicum.playlistmaker.TimeUtils.formatTrackDuraction
import java.text.SimpleDateFormat
import java.util.Locale

const val SEND_TRACK = "send_track"
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
    lateinit var duration: TextView
    lateinit var sharePrefDataTrack: SharedPreferences
    lateinit var previewUrl: String
    lateinit var buttonPlay:FloatingActionButton
    lateinit var handler: Handler

    //Состояние медиаплеера
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        //Присвоить значение переменным
        initViews()

        //Отображение данных трека
        showDataTrack()

        //Подготовка воспроизведения
        preparePlayer()

        //Listener
        setListeners()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        duration.text = "00:00"
        mediaPlayer.release()
    }

    //Настроить Listeners
    private fun setListeners() {
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener() {
            finish()
        }

        buttonPlay.setOnClickListener(){
            playbackControl()
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
        duration = findViewById(R.id.duration)
        sharePrefDataTrack = getSharedPreferences(DATA_TRACK, MODE_PRIVATE)
        buttonPlay = findViewById(R.id.play_track)
        handler = Handler(Looper.getMainLooper())
    }


    //Отображение данных трека
    private fun showDataTrack(){

        track = Gson().fromJson(intent.getStringExtra(SEND_TRACK), Track::class.java) ?: return
        sharePrefDataTrack.edit().putString(DATA_TRACK, Gson().toJson(track)).apply()

        // Ссылка на пробный кусок песни
        previewUrl = track.previewUrl
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = formatTrackDuraction(track.trackTimeMillis.toInt())
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate.substring(0..3)
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        duration.text = "00:00"

        val roundingRadius = this.resources.getDimensionPixelSize(R.dimen.roundingRadiusPlayer)
        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(roundingRadius))
            .into(artworkUrl100)
    }

    //Подготовить воспроизведение
    private fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            buttonPlay.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            handler.removeCallbacksAndMessages(null)
            buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            duration.text = "00:00"
            playerState = STATE_PREPARED
        }
    }

    //Воспроизвести трек
    private fun startPlayer() {
        mediaPlayer.start()
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
        playerState = STATE_PLAYING
        startTimer()
    }

    //Пауза
    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        handler.removeCallbacksAndMessages(null)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun timerTrack(): Runnable{
        return object : Runnable{
            override fun run() {
                duration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                handler?.postDelayed(this, 300)
            }
        }
    }

    private fun startTimer(){
        handler?.post(timerTrack())
    }
}