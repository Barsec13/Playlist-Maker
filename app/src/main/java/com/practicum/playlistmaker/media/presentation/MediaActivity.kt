package com.practicum.playlistmaker.media.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TimeUtils.formatTrackDuraction
import com.practicum.playlistmaker.Constant.delayMillis
import com.practicum.playlistmaker.media.creator.CreatorMedia
import com.practicum.playlistmaker.media.domain.model.Track

class MediaActivity : AppCompatActivity(), MediaView {

    //Переменные
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var track: Track
    lateinit var artworkUrl100: ImageView
    lateinit var trackName: TextView
    lateinit var artistName: TextView
    lateinit var trackTime: TextView
    lateinit var collectionName: TextView
    lateinit var releaseDate: TextView
    lateinit var primaryGenreName: TextView
    lateinit var country: TextView
    lateinit var duration: TextView
    lateinit var previewUrl: String
    lateinit var buttonPlay: FloatingActionButton

    var handler = Handler(Looper.getMainLooper())
    private lateinit var mediaPresenter: MediaPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        //Присвоить значение переменным
        initViews()

        mediaPresenter = CreatorMedia.provideMediaPresenter(
            mediaRouter = MediaRouter(this),
            view = this
        )

        //Listener
        setListeners()

        //Отображение данных трека

        getInfoTrack()

        //Подготовка воспроизведения
        startPreparePlayer()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
        mediaPresenter.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
        duration.text = "00:00"
        mediaPresenter.onViewDestroyed()
    }

    //Инициализация переменных
    private fun initViews() {
        //Кнопка "<-" из окна "Настройки"
        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)
        artworkUrl100 = findViewById(R.id.artwork_url_100)
        trackName = findViewById(R.id.trackName)
        artistName = findViewById(R.id.artistName)
        trackTime = findViewById(R.id.track_time_data)
        collectionName = findViewById(R.id.collection_name_data)
        releaseDate = findViewById(R.id.release_date_data)
        primaryGenreName = findViewById(R.id.primary_genre_name_data)
        country = findViewById(R.id.country_data)
        duration = findViewById(R.id.duration)
        buttonPlay = findViewById(R.id.play_track)
    }

    //Настроить Listeners
    private fun setListeners() {
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener() {
            handler.removeCallbacksAndMessages(null)
            mediaPresenter.clickArrowBack()
        }
        buttonPlay.setOnClickListener() {
            handler.removeCallbacksAndMessages(null)
            mediaPresenter.playbackControl()
        }
    }

    //Отображение данных трека
    fun getInfoTrack() {
        mediaPresenter.loadInfoTrack()
    }

    override fun showDataTrack(track: Track) {
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
    private fun startPreparePlayer() {
        mediaPresenter.startPreparePlayer()
    }

    override fun preparePlayer() {
        buttonPlay.isEnabled = true
        handler.removeCallbacksAndMessages(null)
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        duration.text = "00:00"
    }

    override fun startPlayer() {
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
        startTimer()
    }

    override fun pausePlayer() {
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    //Отображение времени от начала воспроизведения трека
    private fun timerTrack(): Runnable {
        return object : Runnable {
            override fun run() {
                duration.text = mediaPresenter.getCurrentPosition()
                handler.postDelayed(this, delayMillis)
            }
        }
    }

    fun startTimer() {
        handler.post(timerTrack())
    }
}