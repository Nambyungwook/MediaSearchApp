package kr.co.nbw.mediasearchapp.domain.model

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    /**
     * Error response
     * @param code HTTP status code : 알 수 없는 경우 -1
     * @param errorType String : 에러의 종류
     * @param message String : 에러 메시지
     * @param error Throwable
     */
    data class Error(
        val code: Int,
        val errorType: String,
        val message: String,
        val error: Throwable?
    ) : ResultWrapper<Nothing>()
}