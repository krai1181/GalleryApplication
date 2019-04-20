package com.example.galleryapplication.Objects

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private var retrofit:Retrofit? = null
    private var BASE_URL = "https://pixabay.com/"

    val retrofitInstatce:Retrofit?
        get(){
            if (retrofit == null){
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            }
            return retrofit
        }
}