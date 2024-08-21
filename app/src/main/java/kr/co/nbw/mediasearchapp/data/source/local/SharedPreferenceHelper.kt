package kr.co.nbw.mediasearchapp.data.source.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import javax.inject.Inject

class SharedPreferenceHelper @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {

    companion object {
        const val PREFERENCE_NAME = "favorite_medias"
        const val KEY_SAVED_ITEMS = "key_saved_items"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    private val type = object : TypeToken<List<MediaEntity>>() {}.type

    /**
     * SharedPreference에 저장된 미디어 리스트 가져오기
     * @return ResultWrapper<List<MediaEntity>> 저장된 미디어 리스트
     */
    fun getFavoriteMedias(): ResultWrapper<List<MediaEntity>> {
        val savedMediasString = sharedPreferences.getString(KEY_SAVED_ITEMS, "")
        return if (savedMediasString.isNullOrBlank()) {
            // 저장된 미디어가 없음
            ResultWrapper.Success(emptyList())
        } else {
            try {
                ResultWrapper.Success(gson.fromJson(savedMediasString, type))
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                ResultWrapper.Error(
                    code = -1,
                    errorType = "Gson Error",
                    message = "Gson Error : ${e.message}",
                    error = e
                )
            }
        }
    }

    /**
     * SharedPreference에 미디어 저장
     * @param media 저장할 미디어
     * @return 저장 성공 여부
     */
    fun saveMedia(media: MediaEntity): ResultWrapper<MediaEntity> {
        var savedMediasString = sharedPreferences.getString(KEY_SAVED_ITEMS, "")
        var savedMedias: List<MediaEntity> = emptyList()

        if (!savedMediasString.isNullOrBlank()) {
            try {
                savedMedias = gson.fromJson(savedMediasString, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return ResultWrapper.Error(
                    code = -1,
                    errorType = "Gson Error",
                    message = "Gson Error : ${e.message}",
                    error = e
                )
            }
        }

        if (!savedMedias.contains(media)) {
            val mutableSavedImages = savedMedias.toMutableList()
            mutableSavedImages.add(media)
            try {
                savedMediasString = gson.toJson(mutableSavedImages, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return ResultWrapper.Error(
                    code = -1,
                    errorType = "Gson Error",
                    message = "Gson Error : ${e.message}",
                    error = e
                )
            }
            sharedPreferences.edit().putString(KEY_SAVED_ITEMS, savedMediasString).apply()
        } else {
            // 이미 저장된 이미지
            return ResultWrapper.Error(
                code = -1,
                errorType = "Already Saved",
                message = "이미 저장된 이미지입니다.",
                error = null
            )
        }
        return ResultWrapper.Success(media)
    }

    /**
     * SharedPreference에 저장된 미디어 삭제
     * @param media 삭제할 미디어
     * @return 삭제 성공 여부
     */
    fun deleteMedia(media: MediaEntity): ResultWrapper<MediaEntity> {
        var savedMediasString = sharedPreferences.getString(KEY_SAVED_ITEMS, "")
        var savedMedias: List<MediaEntity> = emptyList()

        if (!savedMediasString.isNullOrBlank()) {
            try {
                savedMedias = gson.fromJson(savedMediasString, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return ResultWrapper.Error(
                    code = -1,
                    errorType = "Gson Error",
                    message = "Gson Error : ${e.message}",
                    error = e
                )
            }
        }

        if (savedMedias.contains(media)) {
            val mutableSavedImages = savedMedias.toMutableList()
            mutableSavedImages.remove(media)

            try {
                savedMediasString = gson.toJson(mutableSavedImages, type)
            } catch (e: JsonSyntaxException) {
                e.printStackTrace()
                return ResultWrapper.Error(
                    code = -1,
                    errorType = "Gson Error",
                    message = "Gson Error : ${e.message}",
                    error = e
                )
            }
            sharedPreferences.edit().putString(KEY_SAVED_ITEMS, savedMediasString).apply()
        } else {
            // 저장되지 않은 이미지라 삭제 불가
            return ResultWrapper.Error(
                code = -1,
                errorType = "Not Saved",
                message = "저장되지 않은 이미지입니다.",
                error = null
            )
        }
        return ResultWrapper.Success(media)
    }
}