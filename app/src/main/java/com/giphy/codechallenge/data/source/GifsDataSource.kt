package com.giphy.codechallenge.data.source

import com.giphy.sdk.core.models.Media

interface GifsDataSource {

    interface LoadGifsCallback {

        fun onDataNotAvailable()

        fun onGifsLoaded(gifs: List<Media>)
    }

    fun searchGifs(query: String, callback: LoadGifsCallback)

}