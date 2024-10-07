package kr.co.nbw.mediasearchapp.presentation.view

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kr.co.nbw.mediasearchapp.presentation.R
import kr.co.nbw.mediasearchapp.presentation.view.recyclerview.viewholder.SearchMediaItemViewHolder
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainActivityTest {

    @get:Rule
    var activityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Test
    @SmallTest
    fun test_activity_state() {
        val activityState = activityScenarioRule.scenario.state.name
        assertThat(activityState).isEqualTo("RESUMED")
    }

    // UI Test
    @Test
    @LargeTest
    fun from_SearchFragment_to_MediaActivity_Ui_Operation() {
        // 1. SearchFragment
        // 1-1) 리사이클러뷰 대신 "No Result"가 출력되는지 확인
        Espresso.onView(ViewMatchers.withId(R.id.tv_emptylist))
            .check(ViewAssertions.matches(ViewMatchers.withText("검색 결과가 없습니다.")))
        // 1-2) 검색어로 "android"를 입력
        Espresso.onView(ViewMatchers.withId(R.id.et_search))
            .perform(ViewActions.typeText("android"))
        // 1-3) 검색버튼 클릭
        Espresso.onView(ViewMatchers.withId(R.id.ib_search))
            .perform(ViewActions.click())
        // (1-3의 네트워크 동작이 끝날때까지 대기해야됨 - waitFor()를 사용)
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(3000))
        // 1-4) 리사이클러뷰 표시를 확인
        Espresso.onView(ViewMatchers.withId(R.id.rv_search_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // 1-5) 첫번째 반환값을 클릭
        Espresso.onView(ViewMatchers.withId(R.id.rv_search_result))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchMediaItemViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(1000))
        // 1-6) MediaActivity 결과를 확인 및 이미지 저장
        Espresso.onView(ViewMatchers.withId(R.id.iv_favorite))
            .perform(ViewActions.click())
        // 1-7) 이전 화면으로 돌아감
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.withId(R.id.rv_search_result))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // 2. FavoriteFragment
        // 2-1) FavoriteFragment로 이동
        Espresso.onView(ViewMatchers.withText("즐겨찾기"))
            .perform(ViewActions.click())
        // 2-2) 리사이클러뷰 표시를 확인
        Espresso.onView(ViewMatchers.withId(R.id.rv_favorite_medias))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // 2-3) 첫번째 반환값을 클릭
        Espresso.onView(ViewMatchers.withId(R.id.rv_favorite_medias))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SearchMediaItemViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
        // 2-4) MediaActivity 결과를 확인 및 이미지 제거
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(1000))
        Espresso.onView(ViewMatchers.withId(R.id.iv_favorite))
            .perform(ViewActions.click())
        // 2-5) 이전 화면으로 돌아감
        Espresso.pressBack()
        Espresso.onView(ViewMatchers.isRoot()).perform(waitFor(1000))
        Espresso.onView(ViewMatchers.withId(R.id.fragment_favorite))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    private fun waitFor(delay: Long): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> = ViewMatchers.isRoot()

            override fun getDescription(): String = "wait for $delay milliseconds"

            override fun perform(uiController: UiController, view: View?) {
                uiController.loopMainThreadForAtLeast(delay)
            }

        }
    }
}