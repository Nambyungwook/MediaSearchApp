package kr.co.nbw.mediasearchapp.presentation.view

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kr.co.nbw.mediasearchapp.R
import kr.co.nbw.mediasearchapp.databinding.ActivityMediaBinding
import kr.co.nbw.mediasearchapp.domain.model.MediaSearchData.MediaEntity
import kr.co.nbw.mediasearchapp.domain.model.ResultWrapper
import kr.co.nbw.mediasearchapp.presentation.utils.Constants.EXTRA_MEDIA_DATA_KEY
import kr.co.nbw.mediasearchapp.presentation.viewmodel.MediaViewModel

@AndroidEntryPoint
class MediaActivity : AppCompatActivity() {
    private val binding: ActivityMediaBinding by lazy { ActivityMediaBinding.inflate(layoutInflater) }

    private val mediaViewModel by viewModels<MediaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val mediaData: MediaEntity = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_MEDIA_DATA_KEY, MediaEntity::class.java)
        } else {
            intent.getParcelableExtra(EXTRA_MEDIA_DATA_KEY) as? MediaEntity
        } ?: kotlin.run {
            Toast.makeText(this, getString(R.string.failed_get_media), Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        Glide.with(this)
            .load(mediaData.thumbnailUrl)
            .placeholder(R.color.gray_400)
            .error(R.drawable.icon_error)
            .fallback(R.drawable.icon_warning)
            .into(binding.ivMedia)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.tvMediaTitle.text = mediaData.title
        binding.ivBack.setOnClickListener {
            finish()
        }

        when (val result = mediaViewModel.getFavoriteMedias()) {
            is ResultWrapper.Success -> {
                val favoriteMedias = result.value
                if (favoriteMedias.contains(mediaData)) {
                    binding.ivFavorite.setBackgroundResource(R.drawable.icon_like_on)
                } else {
                    binding.ivFavorite.setBackgroundResource(R.drawable.icon_like_off)
                }
                binding.ivFavorite.setOnClickListener {
                    if (favoriteMedias.contains(mediaData)) {
                        mediaViewModel.deleteMedia(mediaData)
                        binding.ivFavorite.setBackgroundResource(R.drawable.icon_like_off)
                    } else {
                        mediaViewModel.saveMedia(mediaData)
                        binding.ivFavorite.setBackgroundResource(R.drawable.icon_like_on)
                    }
                }
            }
            is ResultWrapper.Error -> {
                Toast.makeText(this, getString(R.string.failed_get_favorite_medias), Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }
    }
}