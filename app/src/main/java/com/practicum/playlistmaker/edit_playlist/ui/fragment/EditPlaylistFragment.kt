package com.practicum.playlistmaker.edit_playlist.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.edit_playlist.ui.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.new_playlist.ui.fragment.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EditPlaylistFragment: NewPlaylistFragment() {

    companion object {
        private const val PLAYLIST_ID = "playlist_id"

        fun createArgs(playlistId: Int): Bundle {
            return bundleOf(PLAYLIST_ID to playlistId)
        }
    }

    override val viewModelPlaylist: EditPlaylistViewModel by viewModel(){
        parametersOf(requireArguments().getInt(EditPlaylistFragment.PLAYLIST_ID))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelPlaylist.observeStatePlaylist().observe(viewLifecycleOwner) { playlist ->
            showInfoPlaylist(playlist)
        }

        viewModelPlaylist.getInfoPlaylist()

        buttonArrowBack.setOnClickListener() {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        buttonCreateNewPlaylist.setOnClickListener() {

            if (oldCoverDrawable != coverPlaylist.drawable) {
                viewModelPlaylist.saveImage(namePlaylistText.text.toString())
            }

            viewModelPlaylist.editPlaylistClicked(
                namePlaylistText.text.toString(), descriptionPlaylistText.text.toString()
            )

            findNavController().navigateUp()
        }
    }

    private fun showInfoPlaylist(playlist: Playlist?) {
        if (playlist == null) return
        if(!playlist.uriCover.toString().isNullOrEmpty()){
            coverPlaylist.setImageURI(playlist.uriCover)
            oldCoverDrawable = coverPlaylist.drawable
        }
        namePlaylistText.setText(playlist.playListName)
        if(!playlist.playlistDescription.isNullOrEmpty()) descriptionPlaylistText.setText(playlist.playlistDescription)
        buttonArrowBack.title = getString(R.string.edit)
        buttonCreateNewPlaylist.text = getString(R.string.save)
    }
}