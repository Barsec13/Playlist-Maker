package com.practicum.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.databinding.PlaylistViewOnPlayerBinding
import com.practicum.playlistmaker.editplaylist.ui.fragment.EditPlaylistFragment
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.fragment.PlayerFragment
import com.practicum.playlistmaker.playlist.ui.models.SharePlaylistStateInterface
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.util.GetWord
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {
    private lateinit var coverPlaylist: ImageView
    private lateinit var namePlaylist: TextView
    private lateinit var descriptionPlaylist: TextView
    private lateinit var totalTime: TextView
    private lateinit var quantityTracks: TextView
    private lateinit var share: ImageView
    private lateinit var more: ImageView
    private lateinit var recyclerViewBottomSheet: RecyclerView
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var buttonArrowBack: androidx.appcompat.widget.Toolbar
    private lateinit var playlistIsEmpty: TextView
    private lateinit var bottomSheetBehaviorMore: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetBehaviorTracks: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetContainerMore: LinearLayout
    private lateinit var bottomSheetContainerTracks: LinearLayout
    private lateinit var overlay: View
    private lateinit var infoPlaylistMore: PlaylistViewOnPlayerBinding
    private lateinit var buttonSharePlaylist: Button
    private lateinit var buttonEditPlaylist: Button
    private lateinit var buttonDeletePlaylist: Button

    companion object {
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }

    private val viewModel: PlaylistViewModel by viewModel() {
        parametersOf(requireArguments().getInt(PLAYLIST_ID))
    }
    private lateinit var tracksAdapterBottomSheet: TrackAdapter

    private lateinit var binding: FragmentPlaylistBinding

    private var trackId: Int? = null

    private var playlistId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initAdapter()

        setListeners()

        setObserve()

        viewModel.getPlaylist()
    }

    override fun onPause() {
        super.onPause()
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setObserve() {
        viewModel.observeStatePlaylist().observe(viewLifecycleOwner) { playlist ->
            showPlaylist(playlist)
            playlistId = playlist.id
        }

        viewModel.observeStateTracksInPlaylist().observe(viewLifecycleOwner) { tracks ->
            showTracks(tracks)
        }

        viewModel.observeStateAllTimeTracks().observe(viewLifecycleOwner) { allTime ->
            showAllTimeTracks(allTime)
        }

        viewModel.observeStateQuantityTracks().observe(viewLifecycleOwner) { quantity ->
            showQuantityTrack(quantity)
        }

        viewModel.observeStateSharePlaylist().observe(viewLifecycleOwner){sharePlaylist ->
            renderStateSharePlaylist(sharePlaylist)
        }
    }

    private fun renderStateSharePlaylist(sharePlaylist: SharePlaylistStateInterface) {
        when(sharePlaylist){
            is SharePlaylistStateInterface.playlistIsEmpity -> showToast(getString(R.string.no_track_in_playlist))
        }
    }

    private fun initAdapter() {
        tracksAdapterBottomSheet = TrackAdapter(ArrayList<Track>(), TrackAdapter.LOW_RESOLUTION)
        recyclerViewBottomSheet.adapter = tracksAdapterBottomSheet
    }

    private fun showAllTimeTracks(allTime: String) {
        val getWordMinutes = GetWord.getWordTime(allTime.toInt(), requireView())
        totalTime.text = "$allTime $getWordMinutes"
    }

    private fun showQuantityTrack(quantity: Int) {
        val getWordTracks: String = GetWord.getWordTrack(quantity, requireView())
        quantityTracks.text = "$quantity $getWordTracks"
    }

    private fun showTracks(tracks: List<Track>?) {
        if (tracks.isNullOrEmpty()) playlistIsEmpty.visibility = View.VISIBLE
        else playlistIsEmpty.visibility = View.GONE
        tracksAdapterBottomSheet.setTracks(tracks)
    }

    private fun setListeners() {
        buttonArrowBack.setOnClickListener() {
            findNavController().navigateUp()
        }
        tracksAdapterBottomSheet.itemClickListener = { position, tracks ->
            sendToPlayer(tracks)
        }
        tracksAdapterBottomSheet.itemLongClickListener = { position, tracks ->
            trackId = tracks.trackId
            confirmDialog.show()
        }

        share.setOnClickListener(){
            viewModel.clickOnShare(quantityTracks.text.toString())
        }

        more.setOnClickListener(){
            bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetContainerMore.visibility = View.VISIBLE

            if(coverPlaylist.drawable != null) infoPlaylistMore.coverPlaylist.setImageDrawable(coverPlaylist.drawable)
            infoPlaylistMore.namePlaylist.text = namePlaylist.text
            infoPlaylistMore.countTracks.text = quantityTracks.text
        }

        buttonSharePlaylist.setOnClickListener(){
            viewModel.clickOnShare(quantityTracks.text.toString())
        }

        buttonDeletePlaylist.setOnClickListener(){
            viewModel.deletePlaylist()
            findNavController().navigateUp()
        }

        buttonEditPlaylist.setOnClickListener(){
            sendToEditPlaylist(playlistId!!)
        }

        bottomSheetBehaviorMore.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        overlay.visibility = View.VISIBLE
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlay.visibility = View.GONE
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                //overlay.alpha = slideOffset
            }
        })
    }

    private fun initViews() {
        coverPlaylist = binding.coverPlaylist
        namePlaylist = binding.namePlaylist
        descriptionPlaylist = binding.descriptionPlaylist
        totalTime = binding.totalTime
        share = binding.share
        more = binding.more
        quantityTracks = binding.totalQuantity
        recyclerViewBottomSheet = binding.recyclerTracks
        buttonArrowBack = binding.toolbarPlaylist
        playlistIsEmpty = binding.playlistEmpty
        overlay = binding.overlay

        infoPlaylistMore = binding.infoPlaylistMore
        buttonSharePlaylist = binding.buttonSharePlaylist
        buttonEditPlaylist = binding.buttonEditPlaylist
        buttonDeletePlaylist = binding.buttonDeletePlaylist

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_track)
            .setMessage("")
            .setPositiveButton(R.string.yes) { dialog, which ->
                if (trackId == null) return@setPositiveButton
                viewModel.deleteTrack(trackId!!)
            }
            .setNegativeButton(R.string.no) { dialog, which -> }

        bottomSheetContainerTracks = binding.bottomSheet
        bottomSheetContainerMore = binding.bottomSheetMore

        bottomSheetBehaviorTracks = BottomSheetBehavior.from(bottomSheetContainerTracks).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            peekHeight = (resources.displayMetrics.heightPixels.toFloat() * 0.33f).toInt()
        }

        bottomSheetBehaviorMore = BottomSheetBehavior.from(bottomSheetContainerMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            peekHeight = (resources.displayMetrics.heightPixels.toFloat() * 0.45f).toInt()
            bottomSheetContainerMore.visibility = View.GONE
        }
    }

    private fun showPlaylist(playlist: Playlist) {
        if (playlist.uriCover.toString().isNotEmpty()) {
            coverPlaylist.setImageURI(playlist.uriCover)
        }
        namePlaylist.text = playlist.playListName
        descriptionPlaylist.text = playlist.playlistDescription
    }

    private fun sendToPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            PlayerFragment.createArgs(track.trackId)
        )
    }

    private fun showToast(messageToast: String) {
        val toast = Toast.makeText(requireContext(), messageToast, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun sendToEditPlaylist(playlistId: Int) {
        bottomSheetBehaviorMore.state = BottomSheetBehavior.STATE_HIDDEN
        bottomSheetContainerMore.visibility = View.GONE
        findNavController().navigate(
            R.id.action_playlistFragment_to_editPlaylistFragment,
            EditPlaylistFragment.createArgs(playlistId)
        )
    }
}