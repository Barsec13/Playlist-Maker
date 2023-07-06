package com.practicum.playlistmaker.player.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.TimeUtils
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
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

    companion object {
        private const val SEND_TRACK_ID = "send_track_id"

        fun createArgs(sendTrackId: Int): Bundle {
            return bundleOf(SEND_TRACK_ID to sendTrackId)
        }
    }

    private var previewUrl: String? = null

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(requireArguments().getInt(SEND_TRACK_ID))
    }
    private lateinit var binding: FragmentPlayerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Присвоить значение переменным
        initViews()

        playerViewModel.getTrackHistory()

        playerViewModel.observeTrackHistoryState().observe(viewLifecycleOwner) { trackHistory ->
            getInfoTrack(trackHistory)
        }

        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            render(it)
        }

        playerViewModel.observerTimerState().observe(viewLifecycleOwner) { time ->
            duration.text = time
        }

        //Listener
        setListeners()
    }

    override fun onPause() {
        super.onPause()
        playerViewModel.activityPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun getInfoTrack(track: Track) {
        showDataTrack(track)
    }

    private fun render(state: PlayerStateInterface) {
        when (state) {
            is PlayerStateInterface.Play -> play()
            is PlayerStateInterface.Pause -> pause()
            is PlayerStateInterface.Prepare -> prepare()
        }
    }

    private fun prepare() {
        buttonPlay.isEnabled = true
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        duration.text = resources.getString(R.string.duration_start)
        //R.string.duration_start.toString()
    }

    private fun play() {
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    private fun pause() {
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
    }

    //Инициализация переменных
    private fun initViews() {
        //Кнопка "<-" из окна "Настройки"
        buttonArrowBackSettings = binding.toolbarSetting
        artworkUrl100 = binding.artworkUrl100
        trackName = binding.trackName
        artistName = binding.artistName
        trackTime = binding.trackTimeData
        collectionName = binding.collectionNameData
        releaseDate = binding.releaseDateData
        primaryGenreName = binding.primaryGenreNameData
        country = binding.countryData
        duration = binding.duration
        buttonPlay = binding.playTrack
    }

    //Настроить Listeners
    private fun setListeners() {
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener() {
            findNavController().navigateUp()
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
        trackTime.text = TimeUtils.formatTrackDuraction(track.trackTimeMillis.toInt())
        collectionName.text = track.collectionName
        releaseDate.text = track.releaseDate.substring(0..3)
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        duration.text = resources.getString(R.string.duration_start)

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