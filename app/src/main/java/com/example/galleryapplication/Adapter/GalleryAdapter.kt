package com.example.galleryapplication.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.galleryapplication.Objects.GalleryImage
import com.example.galleryapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview.view.*

class GalleryAdapter(var gallery: MutableList<GalleryImage>) : RecyclerView.Adapter<CustomViewHolder>(), Filterable {

    var searchList: MutableList<GalleryImage> = gallery

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.cardview, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return gallery.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        val image = gallery.get(position)

        Picasso.with(holder?.itemView?.context).load(image.previewURL)
            .into(holder?.itemView?.gallery_imageview)




        //show saved images
        for (i in holder.favoriteImage) {
            if (i == image.previewURL) {
                holder.itemView.favorite_image.setImageResource(R.drawable.ic_favorite_red_24dp)
            }
        }


        holder?.image = image
        holder?.position = position

    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                var charString: String = constraint.toString()
                if (charString.isEmpty()) {
                    gallery = searchList
                } else {
                    var filteredList: MutableList<GalleryImage> = mutableListOf()
                    for (image: GalleryImage in searchList) {
                        if (image.tags.toLowerCase().contains(charString.toLowerCase().trim())) {
                            filteredList.add(image)
                        }
                        gallery = filteredList
                    }
                }
                var filterResults: FilterResults = FilterResults()
                filterResults.values = gallery
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                gallery = results!!.values as MutableList<GalleryImage>
                notifyDataSetChanged()
            }

        }
    }

}

class CustomViewHolder(itemView: View, var image: GalleryImage? = null, var position: Int? = null) :
    RecyclerView.ViewHolder(itemView) {
    private var isSelected: Boolean = false
    private val preferences: SharedPreferences =
        itemView.getContext().getSharedPreferences("Favorite", Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    var favoriteImage: MutableList<String> = mutableListOf()
    var imagesUrl: MutableList<String> = mutableListOf()

    init {
        loadData()
       //clearData()
        itemView.setOnClickListener {
            loadData()
            image?.previewURL?.let { it1 -> saveData(it1) }
            if (isSelected) {
                snackMassage("Item was removed from Favorites")
                loadData()
                image?.previewURL?.let { it1 -> removeData(it1) }
                println("Shared ${preferences.all} + ${image?.id}")
                itemView.favorite_image.setImageResource(R.drawable.ic_favorite_border_red_24dp)

            } else {
                println("Shared ${preferences.all} + ${image?.id}")

                loadData()
                image?.previewURL?.let { it1 -> saveData(it1) }

                itemView.favorite_image.setImageResource(R.drawable.ic_favorite_red_24dp)
                snackMassage("Item was added to Favorites")
            }
            isSelected = !isSelected
        }

    }

    private fun saveData(url: String) {
        favoriteImage.add(url)
        editor.putStringSet("lastFavorite", favoriteImage.toSet())
        editor.commit()
    }



    fun loadData() {
        imagesUrl = preferences.getStringSet("lastFavorite", setOf<String>()).toMutableList()
        favoriteImage = imagesUrl
        println("shared count  is  ${imagesUrl.size}")

    }

    fun removeData(item: String) {
        favoriteImage.remove(item)
        editor.putStringSet("lastFavorite", favoriteImage.toSet())
        editor.commit()
        println("Remove Item: Favorite count is ${imagesUrl.count()} + favorite${favoriteImage.count()}")


    }


    fun clearData() {
        editor.clear()
        editor.apply()

    }
    fun snackMassage(massage: String){
        val snackbar = Snackbar.make(
            itemView, massage,
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
    }



}