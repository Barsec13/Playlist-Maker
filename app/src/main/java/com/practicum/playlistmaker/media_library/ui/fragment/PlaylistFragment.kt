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
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.media_library.ui.adapter.PlaylistAdapter
import com.practicum.playlistmaker.media_library.ui.models.PlaylistStateInterface
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {
    companion object{
        fun newInstance() = PlaylistFragment()
    }

    private lateinit var buttonNewPlaylist: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderNoPlaylist:LinearLayout
    private lateinit var playlistAdapter: PlaylistAdapter

    private val playlistViewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initAdapter()

        setListeners()

        playlistViewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        playlistViewModel.showPlaylist()
    }

    private fun render(state: PlaylistStateInterface) {
        when (state){
            is PlaylistStateInterface.PlaylistsIsEmpty -> showPlaceholder()
            is PlaylistStateInterface.Playlists -> showPlaylists(state.playlists)
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
        playlistAdapter = PlaylistAdapter(ArrayList<Playlist>())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = playlistAdapter
    }

    private fun setListeners() {
        buttonNewPlaylist.setOnClickListener(){
            sendToNewPlaylist()
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
        playlistAdapter.setPlaylists(playlists)
        placeholderNoPlaylist.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}