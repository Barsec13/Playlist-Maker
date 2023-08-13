package com.practicum.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.media_library.ui.adapter.PlaylistsAdapter
import com.practicum.playlistmaker.media_library.ui.models.PlaylistsStateInterface
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.fragment.PlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {
    companion object{
        fun newInstance() = PlaylistsFragment()
    }

    private lateinit var buttonNewPlaylist: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderNoPlaylist:LinearLayout
    private lateinit var playlistsAdapter: PlaylistsAdapter

    private val playlistsViewModel: PlaylistsViewModel by viewModel()

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initAdapter()

        setListeners()

        playlistsViewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        playlistsViewModel.showPlaylist()

    }

    private fun render(state: PlaylistsStateInterface) {
        when (state){
            is PlaylistsStateInterface.PlaylistsIsEmpty -> showPlaceholder()
            is PlaylistsStateInterface.Playlists -> showPlaylists(state.playlists)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews() {
        buttonNewPlaylist = binding.buttonNewPlaylist
        recyclerView = binding.recyclerViewPlaylist
        placeholderNoPlaylist = binding.placeholderNoPlaylist
    }

    private fun initAdapter(){
        playlistsAdapter = PlaylistsAdapter(ArrayList<Playlist>())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistsAdapter
    }

    private fun setListeners() {
        buttonNewPlaylist.setOnClickListener(){
            sendToNewPlaylist()
        }

        playlistsAdapter.itemClickListener = {position, playlist ->
            sendToPlaylist(playlist.id)
        }
    }

    private fun sendToNewPlaylist() {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_newPlaylistFragment
        )
    }

    private fun showPlaceholder(){
        placeholderNoPlaylist.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun showPlaylists(playlists : List<Playlist>){
        playlistsAdapter.setPlaylists(playlists)
        placeholderNoPlaylist.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    private fun sendToPlaylist(playlistId: Int) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlistId)
        )
    }
}