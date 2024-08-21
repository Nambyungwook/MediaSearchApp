package kr.co.nbw.mediasearchapp.domain.usecase

import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.repository.MediaRepository
import javax.inject.Inject

class DeleteMediaUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke(
        media: MediaEntity
    ) = repository.deleteMedia(media)
}