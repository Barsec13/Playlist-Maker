package com.practicum.playlistmaker.player.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlayerBinding
import com.practicum.playlistmaker.media_library.ui.models.PlaylistStateInterface
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.adapter.PlaylistAdapterBottomSheet
import com.practicum.playlistmaker.player.ui.models.LikeStateInterface
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface
import com.practicum.playlistmaker.player.ui.models.TrackInPlaylistStateInterface
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.util.TimeUtils
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    //Переменные
    private lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    private lateinit var track: Track
    private lateinit var artworkUrl100: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackTime: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var duration: TextView
    private lateinit var buttonPlay: FloatingActionButton
    private lateinit var buttonLike: ImageView
    private lateinit var bottomSheetContainer: LinearLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var buttonAddTrack: FloatingActionButton
    private lateinit var recyclerViewBottomSheet: RecyclerView
    private lateinit var buttonNewPlaylist: Button
    private lateinit var overlay: View


    companion object {
        private const val SEND_TRACK_ID = "send_track_id"

        fun createArgs(sendTrackId: Int): Bundle {
            return bundleOf(SEND_TRACK_ID to sendTrackId)
        }
    }

    private val playerViewModel: PlayerViewModel by viewModel {
        parametersOf(requireArguments().getInt(SEND_TRACK_ID))
    }
    private lateinit var binding: FragmentPlayerBinding
    private lateinit var playlistAdapterBottomSheet: PlaylistAdapterBottomSheet

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

        playerViewModel.observeTrackState().observe(viewLifecycleOwner) { track ->
            getInfoTrack(track)
        }

        playerViewModel.observePlayerState().observe(viewLifecycleOwner) {
            render(it)
        }

        playerViewModel.observerTimerState().observe(viewLifecycleOwner) { time ->
            duration.text = time
        }

        playerViewModel.observeIsFavoriteState().observe(viewLifecycleOwner) {
            renderLike(it)
        }

        playerViewModel.observePlaylistState().observe(viewLifecycleOwner){track ->
            renderPlaylists(track)
        }

        playerViewModel.observeTrackInPlaylistState().observe(viewLifecycleOwner){
            state -> renderAddTrackToPlaylist(state)
        }

        initAdapter()

        //Listener
        setListeners()
    }

    private fun renderAddTrackToPlaylist(state: TrackInPlaylistStateInterface?) {
        when(state){
            is TrackInPlaylistStateInterface.TrackOnPlaylist ->{
                showToast(getString(R.string.trackOnPlaylist))
            }
            is TrackInPlaylistStateInterface.TrackAddToPlaylist ->{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                showToast(getString(R.string.trackAddOnPlaylist))}
        }
    }

    private fun renderPlaylists(state: PlaylistStateInterface) {
        when (state){
            is PlaylistStateInterface.PlaylistsIsEmpty -> return
            is PlaylistStateInterface.Playlists -> showPlaylists(state.playlists)
        }
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

    private fun showPlaylists(playlists: List<Playlist>) {
        playlistAdapterBottomSheet.setPlaylists(playlists)
    }

    private fun render(state: PlayerStateInterface) {
        when (state) {
            is PlayerStateInterface.Play -> play()
            is PlayerStateInterface.Pause -> pause()
            is PlayerStateInterface.Prepare -> prepare()
        }
    }

    private fun renderLike(state: LikeStateInterface) {
        when (state) {
            is LikeStateInterface.LikeTrack ->
                buttonLike.setImageResource(R.drawable.ic_button_like_on)

            is LikeStateInterface.NotLikeTrack ->
                buttonLike.setImageResource(R.drawable.ic_button_like_off)
        }
    }

    private fun prepare() {
        buttonPlay.isEnabled = true
        buttonPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        duration.text = resources.getString(R.string.duration_start)
    }

    private fun play() {
        buttonPlay.setImageResource(R.drawable.ic_baseline_pause_24)
    }

    private fun pause() {
        buttonPlay.isEnabled = true
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
        buttonLike = binding.likeTrack
        buttonAddTrack = binding.buttonAddTrack
        bottomSheetContainer = binding.bottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        recyclerViewBottomSheet = binding.recyclerPlaylist
        buttonNewPlaylist = binding.buttonNewPlaylist
        overlay = binding.overlay
    }

    private fun initAdapter(){
        playlistAdapterBottomSheet = PlaylistAdapterBottomSheet(ArrayList<Playlist>())
        recyclerViewBottomSheet.adapter = playlistAdapterBottomSheet
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
        buttonLike.setOnClickListener() {
            playerViewModel.onFavoriteClicked()
        }

        buttonAddTrack.setOnClickListener(){
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // загружаем рекламный баннер
                        playerViewModel.showPlaylist()
                        overlay.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //overlay.visibility = View.GONE
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                overlay.alpha = slideOffset
            }
        })

        buttonNewPlaylist.setOnClickListener(){
            sendToNewPlaylist()
        }

        playlistAdapterBottomSheet.itemClickListener = {position, tracks, playlist ->
            playerViewModel.onPlaylistClick(tracks, playlist)
        }
    }

    private fun sendToNewPlaylist() {
        findNavController().navigate(
            R.id.action_playerFragment_to_newPlaylistFragment
        )
    }

    private fun showDataTrack(track: Track) {
        // Ссылка на пробный кусок песни
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
    }

    private fun showToast(messageToast: String) {
        val toast = Toast.makeText(requireContext(), messageToast, Toast.LENGTH_LONG)
        toast.show()
    }
}