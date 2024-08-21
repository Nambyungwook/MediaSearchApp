package kr.co.nbw.mediasearchapp.presentation.view

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nbw.mediasearchapp.presentation.R
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.presentation.adapter.FavoriteMediasAdapter
import kr.co.nbw.mediasearchapp.presentation.databinding.FragmentFavoriteBinding
import kr.co.nbw.mediasearchapp.presentation.mapper.toMediaUiModel
import kr.co.nbw.mediasearchapp.presentation.utils.Utils
import kr.co.nbw.mediasearchapp.presentation.utils.collectLatestStateFlow
import kr.co.nbw.mediasearchapp.presentation.viewmodel.FavoriteViewModel

@AndroidEntryPoint
class FavoriteFragment : Fragment(), OnFavoriteMediaItemClick {
    companion object {
        @JvmStatic
        fun newInstance() = FavoriteFragment()
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val favoriteViewModel by viewModels<FavoriteViewModel>()

    private lateinit var favoriteMediasAdapter: FavoriteMediasAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        favoriteViewModel.fetchFavoriteMedias()
        collectLatestStateFlow(favoriteViewModel.favoriteMedias) { favoriteMediasInfo ->

            when (favoriteMediasInfo) {
                is ResultWrapper.Success -> {
                    val favoriteMedias = favoriteMediasInfo.value
                    if (favoriteMedias.isEmpty()) {
                        binding.llEmptyFavorite.isVisible = true
                        binding.rvFavoriteMedias.isVisible = false
                    } else {
                        binding.llEmptyFavorite.isVisible = false
                        binding.rvFavoriteMedias.isVisible = true
                        favoriteMediasAdapter.submitList(favoriteMedias)
                    }
                }
                is ResultWrapper.Error -> {
                    binding.llEmptyFavorite.isVisible = true
                    binding.rvFavoriteMedias.isVisible = false
                    Toast.makeText(requireContext(), getString(R.string.failed_get_favorite_medias), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        favoriteMediasAdapter = FavoriteMediasAdapter(this)
        binding.rvFavoriteMedias.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                private val spacing = Utils.dpToPx(7.5f)
                override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                    val position = parent.getChildAdapterPosition(view)

                    if (position % 2 == 0) {
                        outRect.right = spacing.toInt()
                    } else {
                        outRect.left = spacing.toInt()
                    }
                    outRect.bottom = Utils.dpToPx(40f).toInt()
                }
            })
            adapter = favoriteMediasAdapter
        }
        favoriteMediasAdapter.setOnItemClickListener { mediaEntity ->
            val intent = Intent(this.context, MediaActivity::class.java)
            intent.putExtra("media", mediaEntity.toMediaUiModel())
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.fetchFavoriteMedias()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun saveMedia(media: kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity) {
        TODO("Not yet implemented")
    }

    override fun deleteMedia(media: kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity) {
        when (favoriteViewModel.deleteMedia(media)) {
            is ResultWrapper.Success -> {
                favoriteViewModel.fetchFavoriteMedias()
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

    override fun getFavoriteMedias(): List<kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity> {
        when (val result = favoriteViewModel.getFavoriteMediaList()) {
            is ResultWrapper.Success -> {
                return result.value
            }
            is ResultWrapper.Error -> {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.failed_get_favorite_medias),
                    Toast.LENGTH_SHORT
                ).show()
                return emptyList()
            }
        }
    }
}