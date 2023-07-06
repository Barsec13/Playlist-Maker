package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.fragment.PlayerFragment
import com.practicum.playlistmaker.search.domain.models.NetworkError
import com.practicum.playlistmaker.search.ui.adapter.TrackAdapter
import com.practicum.playlistmaker.search.ui.adapter.TrackHistoryAdapter
import com.practicum.playlistmaker.search.ui.models.SearchStateInterface
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    //Переменные для работы с UI
    lateinit var searchEditText: EditText
    lateinit var searchClearIcon: ImageView
    lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
    lateinit var tracksAdapter: TrackAdapter
    lateinit var tracksHistoryAdapter: TrackHistoryAdapter
    lateinit var placeholderNothingWasFound: LinearLayout
    lateinit var placeholderCommunicationsProblem: LinearLayout
    lateinit var buttonReturn: Button
    lateinit var historyList: LinearLayout
    lateinit var buttonClear: Button
    lateinit var progressBar: ProgressBar
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewHistory: RecyclerView

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Присвоить значение переменным
        initViews()

        //RecyclerView
        initAdapter()

        searchViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        //Listener
        setListeners()

        visibleHistoryTrack()
    }

    //Присвоить значение переменным
    private fun initViews() {
        searchClearIcon = binding.searchClearIcon
        searchEditText = binding.searchEditText
        placeholderNothingWasFound = binding.placeholderNothingWasFound
        placeholderCommunicationsProblem = binding.placeholderCommunicationsProblem
        buttonReturn = binding.buttonReturn
        historyList = binding.historyList
        buttonClear = binding.buttonClearHistory
        progressBar = binding.progressBar
        buttonArrowBackSettings = binding.toolbarSetting
        recyclerView = binding.recyclerView
        recyclerViewHistory = binding.recyclerViewHistory
        searchEditText.setText("")
    }

    private fun textSearch(): String {
        return searchEditText.getText().toString()
    }

    //Настроить RecyclerView
    private fun initAdapter() {
        tracksAdapter = TrackAdapter(ArrayList<Track>())
        recyclerView.adapter = tracksAdapter

        tracksHistoryAdapter = TrackHistoryAdapter(ArrayList<Track>())
        recyclerViewHistory.adapter = tracksHistoryAdapter
    }

    //Настроить Listeners
    private fun setListeners() {
        //Очистка истории поиска
        buttonClear.setOnClickListener() {
            searchViewModel.clickButtonClearHistory()
        }

        //Поиск трека
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !textSearch().equals("")) {
                searchTracks()
                true
            }
            false
        }

        //Очистить строку поиска
        searchClearIcon.setOnClickListener {
            searchViewModel.clearSearchText()
        }

        //Повторить предыдущий запрос после нажатия на кнопку "Обновить"
        buttonReturn.setOnClickListener() {
            searchTracks()
        }

        //Связать поля для ввода и TextWatcher
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            //Действие при вводе текста
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

                searchViewModel.onTextChanged(
                    changedText = s?.toString() ?: "",
                    focus = true
                )
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        //Обработать нажатие на View трека в поиске
        tracksAdapter.itemClickListener = { position, track ->
            searchViewModel.onTrackClick(track, position)
            sendToPlayer(track)
        }

        //Обработать нажатие на View трека в истории поиска
        tracksHistoryAdapter.itemClickListener = { position, track ->
            searchViewModel.onTrackClick(track, position)
            sendToPlayer(track)
            searchViewModel.visibleHistoryTrack()
        }
    }

    private fun render(state: SearchStateInterface) {
        when (state) {
            is SearchStateInterface.Loading -> showLoading()
            is SearchStateInterface.changeTextSearch -> showChangeTextSearch()
            is SearchStateInterface.SearchTracks -> showSearchTracks(state.tracks)
            is SearchStateInterface.HistoryTracks -> showHistoryTracks(state.tracks)
            is SearchStateInterface.Error -> showMessageError(state.error)
        }
    }

    private fun showLoading() {
        hideKeyboard()
        hideMessageError()
        showLoad()
        hideHistoryList()
        buttonClear.isEnabled = false
        hideTracks()
    }

    private fun showChangeTextSearch() {
        hideMessageError()
        hideLoad()
        showClearIcon()
        buttonClear.isEnabled = true
    }

    private fun showSearchTracks(tracks: List<Track>) {
        hideLoad()
        hideKeyboard()
        showTracks(tracks)
        hideMessageError()
        hideHistoryList()
        buttonClear.isEnabled = true
    }

    private fun showHistoryTracks(tracks: List<Track>) {
        hideLoad()
        hideKeyboard()
        hideMessageError()
        showTracks(emptyList())
        clearTextSearch()
        hideClearIcon()
        buttonClear.isEnabled = true
        if (tracks.isNotEmpty()) {
            showHistoryList()
        } else
            hideHistoryList()
        refreshHistory(tracks)
    }

    private fun showMessageError(networkError: NetworkError) {
        hideLoad()
        hideKeyboard()
        hideHistoryList()
        hideTracks()
        buttonClear.isEnabled = true
        when (networkError) {
            is NetworkError.NoData -> {
                placeholderNothingWasFound.isVisible = true
                placeholderCommunicationsProblem.isVisible = false
            }

            is NetworkError.NoInternet -> {
                placeholderCommunicationsProblem.isVisible = true
                placeholderNothingWasFound.isVisible = false
            }

            else -> {
                placeholderNothingWasFound.isVisible = true
                placeholderCommunicationsProblem.isVisible = false
            }
        }
    }

    private fun showLoad() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoad() {
        progressBar.visibility = View.GONE
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        //Скрыть клавиатуру
        inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
    }

    private fun searchTracks() {
        searchViewModel.loadTracks(textSearch())
    }

    private fun refreshHistory(tracks: List<Track>) {
        tracksHistoryAdapter.setTracks(tracks)
    }

    private fun clearTextSearch() {
        searchEditText.setText("")
    }

    //Отображение истории поиска
    private fun visibleHistoryTrack() {
        searchViewModel.visibleHistoryTrack()
    }

    private fun hideMessageError() {
        placeholderNothingWasFound.isVisible = false
        placeholderCommunicationsProblem.isVisible = false
    }

    private fun showHistoryList() {
        historyList.visibility = View.VISIBLE
    }

    private fun hideHistoryList() {
        historyList.visibility = View.GONE
    }

    private fun showTracks(tracks: List<Track>) {
        tracksAdapter.setTracks(tracks)
    }

    private fun hideTracks(){
        tracksAdapter.setTracks(null)
    }

    private fun hideClearIcon() {
        searchClearIcon.visibility = View.GONE
    }

    private fun showClearIcon() {
        searchClearIcon.visibility = View.VISIBLE
    }

    private fun sendToPlayer(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(track.trackId)
        )
    }
}