package com.example.galleryapplication.Adapter

import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.example.galleryapplication.Objects.Gallery
import com.example.galleryapplication.Objects.GalleryImage
import com.example.galleryapplication.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cardview.view.*

class GalleryAdapter(var gallery: MutableList<GalleryImage>): RecyclerView.Adapter<CustomViewHolder>(),Filterable{

    var searchList:MutableList<GalleryImage> = gallery



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.cardview,parent,false)
        return CustomViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return gallery.count()
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

         val image = gallery.get(position)

        Picasso.with(holder?.itemView?.context).load(image.previewURL)
            .into(holder?.itemView?.gallery_imageview)

        holder?.image = image


    }


    override fun getFilter(): Filter {
         return object: Filter(){
             override fun performFiltering(constraint: CharSequence?): FilterResults {
                    var charString:String = constraint.toString()
                 if(charString.isEmpty()){
                    gallery = searchList
                 }else{
                     var filteredList: MutableList<GalleryImage> = mutableListOf()
                     for(image:GalleryImage in searchList){
                         if (image.tags.toLowerCase().contains(charString.toLowerCase().trim())){
                             filteredList.add(image)
                         }
                         gallery = filteredList
                     }}
                     var filterResults:FilterResults = FilterResults()
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

class CustomViewHolder(itemView: View, var image:GalleryImage? = null ) : RecyclerView.ViewHolder(itemView) {
    var favoriteImage:MutableList<GalleryImage> = mutableListOf()
    var isSelected:Boolean = true

    var count:Int = 0
    init {
        itemView.setOnClickListener{
            if(isSelected) {
                count++
                this!!.image?.let { it1 -> favoriteImage.add(it1) }
                itemView.favorite_image.setImageResource(R.drawable.ic_favorite_red_24dp)
                val snackbar = Snackbar.make(itemView, "Item was added to Favorites",
                    Snackbar.LENGTH_LONG)
                    snackbar.show()
                println("-------------${favoriteImage?.count()}  + ${image?.id}")
                isSelected = false
            }else{
                favoriteImage?.remove(image)
                val snackbar = Snackbar.make(itemView, "Item was removed from Favorites",
                    Snackbar.LENGTH_LONG)
                snackbar.show()
                itemView.favorite_image.setImageResource(R.drawable.ic_favorite_border_red_24dp)
                println("-------------${favoriteImage?.count()}  + ${image?.id}")

                isSelected = true
            }
        }
        println("count is $count")


    }

}