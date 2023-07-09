package com.practicum.playlistmaker.media_library.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTrackBinding
import com.practicum.playlistmaker.media_library.ui.models.FavoriteStateInterface
import com.practicum.playlistmaker.media_library.ui.view_model.FavoriteTrackViewModel
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.fragment.PlayerFragment
import com.practicum.playlistmaker.search.ui.adapter.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTrackFragment : Fragment() {
    companion object {
        fun newInstance() = FavoriteTrackFragment()
    }

    private lateinit var favoriteTracksAdapter: TrackAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var placeholderNothingWasFound: LinearLayout
    private lateinit var favoriteList: LinearLayout

    private val favoriteTrackViewModel: FavoriteTrackViewModel by viewModel()

    private var _binding: FragmentFavoriteTrackBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        initAdapter()

        setListeners()

        favoriteTrackViewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        favoriteTrackViewModel.showFavoriteTrack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViews(){
        recyclerView = binding.recyclerViewFavorite
        placeholderNothingWasFound = binding.placeholderNothingWasFound
        favoriteList = binding.favoriteList
    }

    private fun render(state: FavoriteStateInterface){
        when (state){
            is FavoriteStateInterface.FavoriteTracksIsEmpty -> ShowPlaceHolder()
            is FavoriteStateInterface.FavoriteTracks -> ShowFavoriteTracks(state.favoriteTracks)
        }
    }

    private fun ShowPlaceHolder(){
        placeholderNothingWasFound.visibility = View.VISIBLE
        favoriteTracksAdapter.setTracks(null)
        favoriteList.visibility = View.GONE
    }

    private fun ShowFavoriteTracks(tracks: List<Track>){
        favoriteTracksAdapter.setTracks(tracks)
        placeholderNothingWasFound.visibility = View.GONE
        favoriteList.visibility = View.VISIBLE
    }

    private fun initAdapter() {
        favoriteTracksAdapter = TrackAdapter(ArrayList<Track>())
        recyclerView.adapter = favoriteTracksAdapter
    }

    private fun setListeners(){
        //Обработать нажатие на View трека в поиске
        favoriteTracksAdapter.itemClickListener = { position, track ->
            sendToPlayer(track)
        }
    }

    private fun sendToPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_mediaLibraryFragment_to_playerFragment,
            PlayerFragment.createArgs(track.trackId)
        )
    }
}