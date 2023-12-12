package com.example.finaldicoding

import ListHeroAdapter
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finaldicoding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val list = ArrayList<Animal>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listview.setHasFixedSize(true)

        val statusLogin = intent.getBooleanExtra("statusLogin", false)


//        val itemAnimator = DefaultItemAnimator()
//        itemAnimator.addDuration = 500
//        itemAnimator.removeDuration = 500
//        binding.listview.itemAnimator = itemAnimator

        if (!statusLogin) {
            // Jika belum login, panggil LoginActivity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        } else {
            // Jika sudah login, tampilkan data di RecyclerView
            list.addAll(getListHeroes())
            showRecyclerList()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_list -> {
                binding.listview.layoutManager = LinearLayoutManager(this)
            }
            R.id.action_grid -> {
                binding.listview.layoutManager = GridLayoutManager(this, 2)
            }
            R.id.action_bookmark -> {
                // Tambahkan logika untuk menangani aksi klik pada bookmark di sini
                showBookmarkedAnimals()
            }
            R.id.action_logout -> {
                logoutUser()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun logoutUser() {
        // Hapus status login dari shared preferences
        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Arahkan ke halaman login setelah logout
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun showBookmarkedAnimals() {
        val bookmarkedAnimals = getListHeroes().filter { it.isBookmark }

        if (bookmarkedAnimals.isNotEmpty()) {
            val bookmarkedAdapter = ListHeroAdapter(this, bookmarkedAnimals)
            binding.listview.adapter = bookmarkedAdapter

            // Tambahkan item click listener untuk menangani klik pada item bookmark
            bookmarkedAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback {
                override fun onItemClicked(data: Animal) {
                    // Tambahkan logika untuk menangani klik pada item bookmark di sini
                    showBookmarkDetail(data)
                }
            })
        } else {
            Toast.makeText(this, "No bookmarked items", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBookmarkDetail(bookmarkedAnimal: Animal) {
        // Tambahkan logika untuk menampilkan detail item bookmark di sini
        val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
        detailIntent.putExtra(DetailActivity.EXTRA_DETAIL, bookmarkedAnimal)
        startActivity(detailIntent)
    }



    private fun getListHeroes(): ArrayList<Animal> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val dataSoundNames = resources.getStringArray(R.array.data_sound)

        val listHero = ArrayList<Animal>()
        val minSize = minOf(dataName.size, dataDescription.size, dataPhoto.length(), dataSoundNames.size)

        for (i in 0 until minSize) {
            val photoResourceId = if (i < dataPhoto.length()) dataPhoto.getResourceId(i, -1) else -1
            val soundName = if (i < dataSoundNames.size) dataSoundNames[i] else "breach"
            val hero = Animal(dataName[i], dataDescription[i], photoResourceId, soundName)
            listHero.add(hero)
        }

        dataPhoto.recycle()
        return listHero
    }

    private fun showRecyclerList() {
        binding.listview.layoutManager = LinearLayoutManager(this)
        val listHeroAdapter = ListHeroAdapter(this, list)
        binding.listview.adapter = listHeroAdapter

        listHeroAdapter.setOnItemClickCallback(object : ListHeroAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Animal) {
                val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                detailIntent.putExtra(DetailActivity.EXTRA_DETAIL, data)
                startActivity(detailIntent)
            }
        })
    }
}
