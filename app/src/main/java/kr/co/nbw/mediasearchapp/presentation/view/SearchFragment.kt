package kr.co.nbw.mediasearchapp.presentation.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nbw.mediasearchapp.R
import kr.co.nbw.mediasearchapp.databinding.FragmentSearchBinding
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.presentation.adapter.SearchMediasAdapter
import kr.co.nbw.mediasearchapp.presentation.utils.Constants.EXTRA_MEDIA_DATA_KEY
import kr.co.nbw.mediasearchapp.presentation.utils.Utils
import kr.co.nbw.mediasearchapp.presentation.utils.collectLatestStateFlow
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.ImageItemDividerDecoration
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.OnNextPageCallListener
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.OnNextPageScrollListener
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder.SearchMediaItemViewHolder
import kr.co.nbw.mediasearchapp.presentation.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment(), OnNextPageCallListener, OnFavoriteMediaItemClick {
    companion object {
        @JvmStatic
        fun newInstance() = SearchFragment()
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModels<SearchViewModel>()

    private lateinit var searchMediasAdapter: SearchMediasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupButtons()
        setupRecyclerView()

        collectLatestStateFlow(searchViewModel.searchViewState) { searchViewState ->
            when (searchViewState) {
                is SearchViewState.Loading -> {
                    binding.progressBarContainer.isVisible = true
                    binding.tvEmptylist.isVisible = false
                    binding.llErrorView.isVisible = false
                }
                is SearchViewState.Empty -> {
                    binding.progressBarContainer.isVisible = false
                    binding.tvEmptylist.isVisible = true
                    binding.llErrorView.isVisible = false
                    // fixme : submitList와 notifyData...() 중에 하나만 사용하여 구현해야됨 둘의 역할이 동일하며 특히나 ListAdapter를 사용할경우 submitList()를 사용하는게 좋음
                    searchMediasAdapter.submitList(emptyList())
                    searchMediasAdapter.notifyDataSetChanged()
                }
                is SearchViewState.Success -> {
                    binding.progressBarContainer.isVisible = false
                    binding.tvEmptylist.isVisible = false
                    binding.llErrorView.isVisible = false
                    searchMediasAdapter.submitList(searchViewState.mediaItemList)
                    // fixme : submitList와 notifyData...() 중에 하나만 사용하여 구현해야됨 둘의 역할이 동일하며 특히나 ListAdapter를 사용할경우 submitList()를 사용하는게 좋음
                    if (searchViewState.isFirstSearch) {
                        searchMediasAdapter.notifyItemRangeChanged(
                            0,
                            searchViewState.mediaItemList.size
                        )
                    } else {
                        searchMediasAdapter.notifyItemRangeInserted(
                            searchMediasAdapter.itemCount,
                            searchViewState.mediaItemList.size
                        )
                    }
                }
                is SearchViewState.Error -> {
                    binding.progressBarContainer.isVisible = false
                    binding.tvEmptylist.isVisible = false
                    binding.llErrorView.isVisible = true
                    binding.tvErrorMessage.text = searchViewState.errorMessage
                    // fixme : submitList와 notifyData...() 중에 하나만 사용하여 구현해야됨 둘의 역할이 동일하며 특히나 ListAdapter를 사용할경우 submitList()를 사용하는게 좋음
                    searchMediasAdapter.submitList(emptyList())
                    searchMediasAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            ibSearch.setOnClickListener {
                searchMedias {
                    Toast.makeText(requireContext(), getString(R.string.no_search_query), Toast.LENGTH_SHORT).show()
                }
                val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            }

            ibClear.setOnClickListener {
                etSearch.text = Editable.Factory.getInstance().newEditable("")
            }

            etSearch.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchMedias {
                        Toast.makeText(requireContext(), getString(R.string.no_search_query), Toast.LENGTH_SHORT).show()
                    }
                    val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

                    true
                } else {
                    false
                }
            }

            btnRetry.setOnClickListener {
                searchMedias {
                    Toast.makeText(requireContext(), getString(R.string.no_search_query), Toast.LENGTH_SHORT).show()
                    llErrorView.isVisible = false
                }
            }
        }
    }

    private fun searchMedias(
        emptyQueryAction: () -> Unit
    ) {
        val currentQuery = binding.etSearch.text.toString().trim()
        if (currentQuery.isNotEmpty()) {
            if (this::searchMediasAdapter.isInitialized) {
                // fixme : submitList와 notifyData...() 중에 하나만 사용하여 구현해야됨 둘의 역할이 동일하며 특히나 ListAdapter를 사용할경우 submitList()를 사용하는게 좋음
                searchMediasAdapter.submitList(emptyList())
                searchMediasAdapter.notifyDataSetChanged()
            }
            searchViewModel.searchMedias(currentQuery)
            searchViewModel.query = currentQuery
        } else {
            emptyQueryAction()
        }
    }

    private fun setupRecyclerView() {
        searchMediasAdapter = SearchMediasAdapter(this)
        binding.rvSearchResult.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(
                ImageItemDividerDecoration(
                    Utils.dpToPx(24f).toInt()
                )
            )
            adapter = searchMediasAdapter
            addOnScrollListener(
                OnNextPageScrollListener(this@SearchFragment)
            )
        }
        searchMediasAdapter.setOnItemClickListener {
            val intent = Intent(this.context, MediaActivity::class.java)
            intent.putExtra(EXTRA_MEDIA_DATA_KEY, it)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        updateFavoriteImage()
    }

    private fun updateFavoriteImage() {
        if (this::searchMediasAdapter.isInitialized) {
            searchMediasAdapter.currentList.forEachIndexed { index, mediaSearchData ->
                if (mediaSearchData is MediaSearchData.MediaEntity) {
                    val holder = binding.rvSearchResult.findViewHolderForAdapterPosition(index)
                    if (holder is SearchMediaItemViewHolder) holder.updateFavoriteMediaView(mediaSearchData)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onNext() {
        searchViewModel.loadMoreMedias()
    }

    override fun hasNextPage(): Boolean {
        return searchViewModel.hasNextPage
    }

    override fun getCurrentPage(): Int {
        return maxOf(searchViewModel.currentImageResponsePageIndex, searchViewModel.currentVideoResponsePageIndex)
    }

    override fun isLoading(): Boolean {
        return searchViewModel.searchViewState.value is SearchViewState.Loading
    }

    override fun saveMedia(media: MediaSearchData.MediaEntity) {
        when (searchViewModel.saveMedia(media)) {
            is ResultWrapper.Success -> {
                Toast.makeText(requireContext(), getString(R.string.success_save_media), Toast.LENGTH_SHORT).show()
                updateFavoriteImage()
            }
            is ResultWrapper.Error -> {
                Toast.makeText(requireContext(), getString(R.string.failed_save_media), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun deleteMedia(media: MediaSearchData.MediaEntity) {
        when (searchViewModel.deleteMedia(media)) {
            is ResultWrapper.Success -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.success_delete_media),
                    Toast.LENGTH_SHORT
                ).show()
            }

            is ResultWrapper.Error -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_delete_media),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getFavoriteMedias(): List<MediaSearchData.MediaEntity> {
        when (val result = searchViewModel.getFavoriteMedias()) {
            is ResultWrapper.Success -> {
                return result.value
            }
            is ResultWrapper.Error -> {
                Toast.makeText(requireContext(), getString(R.string.failed_get_favorite_medias), Toast.LENGTH_SHORT).show()
                return emptyList()
            }
        }
    }
}