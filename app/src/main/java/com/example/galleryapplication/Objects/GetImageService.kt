package com.example.galleryapplication.Objects

import retrofit2.Call
import retrofit2.http.GET

interface GetImageService {


    @GET("api/?key=12175339-7048b7105116d7fa1da74220c")
    fun getAllImages(): Call<Gallery>
}