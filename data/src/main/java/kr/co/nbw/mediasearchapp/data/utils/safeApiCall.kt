package kr.co.nbw.mediasearchapp.data.utils

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kr.co.nbw.mediasearchapp.data.model.ResponseErrorBody
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResultWrapper<T> {
    return withContext(dispatcher) {
        try {
            val response = apiCall.invoke()
            response?.let { apiResponse ->
                ResultWrapper.Success(apiResponse)
            } ?: kotlin.run {
                ResultWrapper.Error(
                    code = -1,
                    errorType = "Response Null",
                    message = "응답 값이 없습니다.",
                    error = null
                )
            }
        } catch (throwable: Throwable) {
            when (throwable) {
                is IOException -> {
                    ResultWrapper.Error(
                        code = -1,
                        errorType = "Unknown Error",
                        message = "Throwable Message : ${throwable.message}\n나중에 다시 시도해 주세요.",
                        error = throwable
                    )
                }
                is HttpException -> {
                    try {
                        val gson = Gson().newBuilder().create()
                        val adapter = gson.getAdapter(ResponseErrorBody::class.java)
                        val responseErrorBody = throwable.response()?.errorBody()?.string()?.let { errorString ->
                            adapter.fromJson(errorString)
                        } ?: kotlin.run {
                            ResponseErrorBody(
                                errorType = "Unknown Error",
                                message = "알 수 없는 에러가 발생했습니다. 나중에 다시 시도해 주세요."
                            )
                        }
                        ResultWrapper.Error(
                            code = throwable.code(),
                            errorType = responseErrorBody.errorType,
                            message = responseErrorBody.message,
                            error = throwable
                        )
                    } catch (e: IOException) {
                        ResultWrapper.Error(
                            code = -1,
                            errorType = "Gson Error",
                            message = "Throwable Message : ${throwable.message ?: "없음"}\n나중에 다시 시도해 주세요.",
                            error = e
                        )
                    }
                }
                else -> ResultWrapper.Error(
                    code = -1,
                    errorType = "Unknown Error",
                    message = "Throwable Message : ${throwable.message ?: "없음"}\n나중에 다시 시도해 주세요.",
                    error = throwable
                )
            }
        }
    }
}