package com.practicum.playlistmaker.new_playlist.ui.fragment

import android.Manifest
import android.content.ContentResolver
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment() : Fragment() {
    private lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    private lateinit var coverPlaylist: ImageView
    private lateinit var namePlaylistText: TextInputEditText
    private lateinit var descriptionPlaylistText: TextInputEditText
    private lateinit var namePlaylist: TextInputLayout
    private lateinit var descriptionPlaylist: TextInputLayout
    private lateinit var buttonCreateNewPlaylist: AppCompatButton
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var contentResolver: ContentResolver
    private lateinit var confirmDialog: MaterialAlertDialogBuilder
    private lateinit var oldCoverDrawable: Drawable

    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    private lateinit var binding: FragmentNewPlaylistBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contentResolver = requireContext().contentResolver

        initViews()

        setListeners()

        newPlaylistViewModel.observeStateCover().observe(viewLifecycleOwner) {
            coverPlaylist.setImageURI(it)
        }

        newPlaylistViewModel.showCover()
    }

    private fun initViews() {
        buttonArrowBackSettings = binding.toolbarSetting
        coverPlaylist = binding.coverPlaylist
        namePlaylistText = binding.textNamePlaylist
        descriptionPlaylistText = binding.textDescriptionPlaylist
        buttonCreateNewPlaylist = binding.buttonCreatePlaylist
        namePlaylist = binding.namePlaylist
        descriptionPlaylist = binding.descriptionPlaylist

        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { Uri ->
            if (Uri != null) {
                coverPlaylist.setImageURI(Uri)
                newPlaylistViewModel.saveUriImage(Uri)
            }
        }

        confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_exiting_playlist_creation)
            .setMessage(R.string.dialog_loss_of_data)
            .setNeutralButton(R.string.dialog_cancel) { dialog, which -> }
            .setNegativeButton(R.string.dialog_complete) { dialog, which ->
                exit()
            }

        oldCoverDrawable = coverPlaylist.drawable
    }

    private fun setListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkFillingField()
            }
        })

        buttonArrowBackSettings.setOnClickListener() {
            checkFillingField()
        }

        namePlaylistText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonCreateNewPlaylist.isEnabled = true
                namePlaylist.setBoxStrokeColorStateList(
                    resources.getColorStateList(
                        R.color.text_input_box_stroke,
                        null
                    )
                )
                namePlaylist.defaultHintTextColor =
                    ContextCompat.getColorStateList(requireContext(), R.color.text_input)
            }

            override fun afterTextChanged(s: Editable?) {
                if (namePlaylistText.text.isNullOrEmpty()) {
                    buttonCreateNewPlaylist.isEnabled = false
                    namePlaylist.setBoxStrokeColorStateList(
                        resources.getColorStateList(
                            R.color.text_not_box_stroke,
                            null
                        )
                    )

                    namePlaylist.defaultHintTextColor =
                        ContextCompat.getColorStateList(requireContext(), R.color.text_not)
                }
            }
        })

        descriptionPlaylistText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionPlaylist.setBoxStrokeColorStateList(
                    resources.getColorStateList(
                        R.color.text_input_box_stroke,
                        null
                    )
                )
                descriptionPlaylist.defaultHintTextColor =
                    resources.getColorStateList(R.color.text_input, null)
            }

            override fun afterTextChanged(s: Editable?) {
                if (namePlaylistText.text.isNullOrEmpty()) {
                    descriptionPlaylist.setBoxStrokeColorStateList(
                        resources.getColorStateList(
                            R.color.text_not_box_stroke,
                            null
                        )
                    )
                    descriptionPlaylist.defaultHintTextColor =
                        resources.getColorStateList(R.color.text_not, null)
                }
            }
        })

        coverPlaylist.setOnClickListener() {
            requestPermission()
        }

        buttonCreateNewPlaylist.setOnClickListener() {

            if (oldCoverDrawable != coverPlaylist.drawable) {
                newPlaylistViewModel.saveImage(namePlaylistText.text.toString())
            }

            newPlaylistViewModel.createPlaylistClicked(
                namePlaylistText.text.toString(), descriptionPlaylistText.text.toString()
            )

            showMessageCreatePlaylist()

            exit()
        }
    }

    private fun exit() {
        findNavController().navigateUp()
    }

    private fun showMessageCreatePlaylist() {
        val messageCreatePlaylist =
            getString(R.string.playlist) + " " + namePlaylistText.text.toString() + " " + getString(
                R.string.created
            )

        Snackbar
            .make(
                requireContext(),
                binding.fragmentNewPlaylist,
                messageCreatePlaylist,
                Snackbar.LENGTH_SHORT
            )
            .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.snackbar))
            .setTextColor(ContextCompat.getColor(requireContext(), R.color.snackbar_text))
            .setDuration(Snackbar.LENGTH_SHORT)
            .show()
    }

    private fun requestPermission() {
        val requester = PermissionRequester.instance()

        lifecycleScope.launch {
            if (Build.VERSION.SDK_INT >= 33) {
                requester.request(Manifest.permission.READ_MEDIA_IMAGES)
            } else {
                requester.request(Manifest.permission.READ_EXTERNAL_STORAGE)
            }.collect { result ->
                when (result) {
                    is PermissionResult.Granted -> {
                        launchPickMedia()
                    }// Пользователь дал разрешение, можно продолжать работу
                    is PermissionResult.Denied.NeedsRationale -> {
                        showToast(R.string.rationale_permission.toString())
                    }// Необходимо показать разрешение
                    is PermissionResult.Denied.DeniedPermanently -> {
                        showToast(R.string.denied_permanently_permission.toString())
                    }// Запрещено навсегда, перезапрашивать нет смысла, предлагаем пройти в настройки
                    is PermissionResult.Cancelled -> {
                        return@collect
                    }// Запрос на разрешение отменён
                }
            }
        }
    }

    private fun launchPickMedia() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showToast(messageToast: String) {
        val toast = Toast.makeText(requireContext(), messageToast, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun checkFillingField() {
        if (!namePlaylistText.text.isNullOrEmpty()
            || !descriptionPlaylistText.text.isNullOrEmpty()
            || oldCoverDrawable != coverPlaylist.drawable
        ) {
            confirmDialog.show()
        } else exit()
    }
}