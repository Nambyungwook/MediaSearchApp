package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import kr.co.nbw.mediasearchapp.data.repository.FakeMediaRepository
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SaveMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SearchImagesUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SearchVideosUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@MediumTest
class SearchViewModelTest {
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var fakeMediaRepository: FakeMediaRepository
    private lateinit var deleteAndGetFavoriteTestMedia: MediaSearchData.MediaEntity

    @Before
    fun setUp() {
        fakeMediaRepository = FakeMediaRepository()

        searchViewModel = SearchViewModel(
            SearchImagesUseCase(fakeMediaRepository),
            SearchVideosUseCase(fakeMediaRepository),
            SaveMediaUseCase(fakeMediaRepository),
            DeleteMediaUseCase(fakeMediaRepository),
            GetFavoriteMediasUseCase(fakeMediaRepository),
            SavedStateHandle()
        )

        deleteAndGetFavoriteTestMedia = MediaSearchData.MediaEntity(
            mediaType = MediaType.IMAGE,
            thumbnailUrl = "deleteAndGetFavoriteTestMediaThumbnailUrl",
            title = "deleteAndGetFavoriteTestMediaTitle",
            categoryName = "deleteAndGetFavoriteTestMediaCategoryName",
            contentsUrl = "deleteAndGetFavoriteTestMediaContentsUrl",
            datetime = Date()
        )

        fakeMediaRepository.saveMedia(deleteAndGetFavoriteTestMedia)
    }

    @After
    fun tearDown() {
        // Clean up
    }

    @Test
    fun save_media_test() {
        val saveTestMedia = MediaSearchData.MediaEntity(
            mediaType = MediaType.IMAGE,
            thumbnailUrl = "thumbnailUrl",
            title = "mediaTitle",
            categoryName = "categoryName",
            contentsUrl = "contentsUrl",
            datetime = Date()
        )

        searchViewModel.saveMedia(saveTestMedia)
        val favoriteMedias = (searchViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).contains(saveTestMedia)
    }

    @Test
    fun get_favorite_medias_test() {
        val favoriteMedias = (searchViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).contains(deleteAndGetFavoriteTestMedia)
    }

    @Test
    fun delete_media_test() {
        searchViewModel.deleteMedia(deleteAndGetFavoriteTestMedia)

        val favoriteMedias = (searchViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()
        assertThat(favoriteMedias).doesNotContain(deleteAndGetFavoriteTestMedia)
    }


}