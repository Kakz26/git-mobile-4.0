package com.example.finaldicoding

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.finaldicoding.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var data: Animal? = null

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(androidx.appcompat.R.drawable.abc_ic_ab_back_material)

        val data: Animal? = intent.getParcelableExtra(EXTRA_DETAIL)
        if (data != null) {
            binding.name.text = data.name
            binding.desc.text = data.description
            binding.image.setImageResource(data.photo)

            val bookmarkButton: Button = findViewById(R.id.bookmarkButton)
            bookmarkButton.setOnClickListener {
                data?.isBookmark = !(data?.isBookmark ?: false)
                updateBookmarkIcon()
                saveBookmarkStatus()
                Log.d("DetailActivity", "Bookmark button clicked. isBookmark: ${data?.isBookmark}")
            }
            updateBookmarkIcon()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateBookmarkIcon() {
        // Sesuaikan ikon bookmark berdasarkan status bookmark
        val bookmarkButton: Button = findViewById(R.id.bookmarkButton)
        if (isAnimalBookmarked()) {
            bookmarkButton.text = "Bookmarked"
        } else {
            bookmarkButton.text = "Bookmark"
        }
    }

    private fun saveBookmarkStatus() {
        val sharedPreferences = getSharedPreferences("BookmarkPrefs", MODE_PRIVATE)
        val bookmarkedSet = sharedPreferences.getStringSet("bookmarkedSet", mutableSetOf()) ?: mutableSetOf()

        if (data != null) {
            if (data?.isBookmark == true) {
                bookmarkedSet.add(data?.name)
            } else {
                bookmarkedSet.remove(data?.name)
            }
        }

        val editor = sharedPreferences.edit()
        editor.putStringSet("bookmarkedSet", bookmarkedSet)
        editor.apply()
    }

    private fun isAnimalBookmarked(): Boolean {
        val sharedPreferences = getSharedPreferences("BookmarkPrefs", MODE_PRIVATE)
        val bookmarkedSet = sharedPreferences.getStringSet("bookmarkedSet", mutableSetOf()) ?: mutableSetOf()
        return data?.name in bookmarkedSet
    }

}
