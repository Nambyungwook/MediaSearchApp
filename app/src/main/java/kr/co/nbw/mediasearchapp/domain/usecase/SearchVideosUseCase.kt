package kr.co.nbw.mediasearchapp.domain.usecase

import kr.co.nbw.mediasearchapp.domain.repository.MediaRepository
import javax.inject.Inject

class SearchVideosUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    suspend operator fun invoke(
        query: String,
        sort: String,
        page: Int,
        size: Int
    ) = repository.searchVideos(
        query = query,
        sort = sort,
        page = page,
        size = size
    )
}