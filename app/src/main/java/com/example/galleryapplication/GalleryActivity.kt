package com.example.galleryapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.SearchView
import com.example.galleryapplication.Adapter.GalleryAdapter
import com.example.galleryapplication.Objects.Gallery
import com.example.galleryapplication.Objects.GalleryImage
import com.example.galleryapplication.Objects.GetImageService
import com.example.galleryapplication.Objects.RetrofitClientInstance
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_gallery.*
import okhttp3.*
import java.io.IOException
import java.util.ArrayList

class GalleryActivity : AppCompatActivity() {

    lateinit var searchView: SearchView

    lateinit var adapter:GalleryAdapter
    lateinit var images:MutableList<GalleryImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)



        gallery_recyclerview.layoutManager = GridLayoutManager(this,3);
      //  gallery_recyclerview.adapter = GalleryAdapter(favoriteImages)




        val service = RetrofitClientInstance.retrofitInstatce?.create(GetImageService::class.java)
        val call = service?.getAllImages()
        call?.enqueue(object :retrofit2.Callback<Gallery>{
            override fun onResponse(call: retrofit2.Call<Gallery>, response: retrofit2.Response<Gallery>) {
                val body = response?.body()
                val images = body?.hits
                runOnUiThread {adapter = images?.let { GalleryAdapter(it) }!!
                    gallery_recyclerview.adapter = adapter
                }

            }
            override fun onFailure(call: retrofit2.Call<Gallery>, t: Throwable) {
                println("Failed to execute request")
            }


        })

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val searchItem = menu!!.findItem(R.id.action_searchImage)
        searchView = searchItem.actionView as SearchView
       // searchView.setSubmitButtonEnabled(true)
        searchView.setQueryHint("Search here ...")
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                    adapter.filter.filter(newText)
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item!!.itemId == R.id.action_searchImage)
            return true
        return super.onOptionsItemSelected(item)
    }


/*    fun getImages(){
        var url = "https://pixabay.com/api/?key=12175339-7048b7105116d7fa1da74220c"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                val gson = GsonBuilder().create()
                val mGallery= gson.fromJson(body, Gallery::class.java)

            *//*    runOnUiThread {adapter = GalleryAdapter(mGallery)
                    gallery_recyclerview.adapter = adapter
                }*//*



            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }


        })


    }*/


}



