package kr.co.nbw.mediasearchapp.presentation.viewmodel

import androidx.test.filters.MediumTest
import com.google.common.truth.Truth.assertThat
import kr.co.nbw.mediasearchapp.data.repository.FakeMediaRepository
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData
import kr.co.nbw.mediasearchapp.domain.model.MediaType
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.domain.usecase.DeleteMediaUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.GetFavoriteMediasUseCase
import kr.co.nbw.mediasearchapp.domain.usecase.SaveMediaUseCase
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.Date

@MediumTest
class MediaViewModelTest {
    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var fakeMediaRepository: FakeMediaRepository
    private lateinit var deleteAndGetFavoriteTestMedia: MediaSearchData.MediaEntity

    @Before
    fun setUp() {
        fakeMediaRepository = FakeMediaRepository()

        mediaViewModel = MediaViewModel(
            GetFavoriteMediasUseCase(fakeMediaRepository),
            DeleteMediaUseCase(fakeMediaRepository),
            SaveMediaUseCase(fakeMediaRepository)
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

        mediaViewModel.saveMedia(saveTestMedia)
        val favoriteMedias = (mediaViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).contains(saveTestMedia)
    }

    @Test
    fun delete_media_test() {
        mediaViewModel.deleteMedia(deleteAndGetFavoriteTestMedia)

        val favoriteMedias = (mediaViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).doesNotContain(deleteAndGetFavoriteTestMedia)
    }

    @Test
    fun get_favorite_medias_test() {
        val favoriteMedias = (mediaViewModel.getFavoriteMedias() as? ResultWrapper.Success)?.value ?: emptyList()

        assertThat(favoriteMedias).contains(deleteAndGetFavoriteTestMedia)
    }
}