package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import kr.co.nbw.mediasearchapp.data.repository.FakeMediaRepository
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@MediumTest
class FavoriteViewModelTest {
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var fakeMediaRepository: FakeMediaRepository
    private lateinit var deleteAndGetFavoriteTestMedia: MediaSearchData.MediaEntity

    @Before
    fun setup() {
        fakeMediaRepository = FakeMediaRepository()

        favoriteViewModel = FavoriteViewModel(
            GetFavoriteMediasUseCase(fakeMediaRepository),
            DeleteMediaUseCase(fakeMediaRepository)
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
    fun get_favorite_medias_test() {
        val favoriteMedias = (favoriteViewModel.getFavoriteMediaList() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).contains(deleteAndGetFavoriteTestMedia)
    }

    @Test
    fun delete_media_test() {
        favoriteViewModel.deleteMedia(deleteAndGetFavoriteTestMedia)

        val favoriteMedias = (favoriteViewModel.getFavoriteMediaList() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).doesNotContain(deleteAndGetFavoriteTestMedia)
    }
}