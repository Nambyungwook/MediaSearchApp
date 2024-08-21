package kr.co.nbw.mediasearchapp.presentation.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nbw.mediasearchapp.R
import kr.co.nbw.mediasearchapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTabLayout()
    }

    private fun setupTabLayout() {
        binding.vpMain.adapter = FragmentAdapter(this)
        TabLayoutMediator(
            binding.tabLayout,
            binding.vpMain
        ) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_search)
                else -> getString(R.string.tab_favorite)
            }
        }.attach()
    }
}

class FragmentAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // Fixme: FragmentManager를 사용하는 방법으로 수정
            0 -> SearchFragment.newInstance()
            else -> FavoriteFragment.newInstance()
        }
    }
}