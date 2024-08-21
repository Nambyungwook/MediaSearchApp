package kr.co.nbw.mediasearchapp.domain.usecase

import kr.co.nbw.mediasearchapp.domain.repository.MediaRepository
import javax.inject.Inject

class GetFavoriteMediasUseCase @Inject constructor(
    private val repository: MediaRepository
) {
    operator fun invoke() = repository.getFavoriteMedias()
}