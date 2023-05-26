package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.TimeUtils.formatTrackDuraction
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface
import com.practicum.playlistmaker.player.ui.router.PlayerRouter
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

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
    lateinit var buttonPlay: FloatingActionButton

    private var previewUrl: String? = null

    private val playerViewModel: PlayerViewModel by viewModel()
    private lateinit var playerRouter: PlayerRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        //Присвоить значение переменным
        initViews()

        playerRouter = PlayerRouter(this)

        playerViewModel.observePlayerState().observe(this){
            render(it)
        }

        playerViewModel.observerTimerState().observe(this){ time ->
            duration.text = time
        }

        //Listener
        setListeners()
        //Отображение данных трека
        getInfoTrack()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.activityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getInfoTrack() {
        showDataTrack(playerRouter.getToMedia())
    }

    private fun render(state: PlayerStateInterface){
        when(state){
            is PlayerStateInterface.Play -> play()
            is PlayerStateInterface.Pause -> pause()
            is PlayerStateInterface.Prepare -> prepare()
        }
    }

    private fun prepare(){
        buttonPlay.isEnabled = true
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        duration.text = "00:00"
    }

    private fun play(){
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    private fun pause(){
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
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
            playerRouter.backView()
        }
        buttonPlay.setOnClickListener() {
            playerViewModel.playbackControl()
        }
    }

    private fun showDataTrack(track: Track) {
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

        startPreparePlayer(previewUrl)
    }

    //Подготовить воспроизведение
    private fun startPreparePlayer(previewUrl: String?) {
        playerViewModel.startPreparePlayer(previewUrl)
    }
}