package kr.co.nbw.mediasearchapp.presentation.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue

class Utils {
    companion object {
        @JvmStatic
        fun dpToPx(dm: DisplayMetrics, dp: Float): Float {
            val result = try {
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm)
            } catch (e: Exception) {
                e.printStackTrace()
                dp
            }
            return result
        }

        @JvmStatic
        fun dpToPx(dp: Float): Float {
            return dpToPx(Resources.getSystem().displayMetrics, dp)
        }
    }
}