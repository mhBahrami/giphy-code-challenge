package com.giphy.codechallenge.data.source.remote

import androidx.annotation.VisibleForTesting
import com.giphy.codechallenge.data.SearchResult
import com.giphy.codechallenge.data.source.GifsDataSource
import com.giphy.codechallenge.util.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GifsRemoteDataSource private constructor (
    private val appExecutors: AppExecutors,
    private val giphyApiService: GiphyApiService
) : GifsDataSource {

    /**
     * Note: [GifsDataSource.LoadGifsCallback] would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun searchGifs(query: String, callback: GifsDataSource.LoadGifsCallback) {
        try {
            appExecutors.networkIO.execute {
                val call =
                    giphyApiService.search(
                        query,
                        GiphyApiService.API_KEY,
                        GiphyApiService.DEFAULT_LIMIT
                    )
                call.enqueue(object : Callback<SearchResult> {
                    override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                        // We can improve it by detecting the issue and displaying
                        // the appropriate message to the user
                        callback.onDataNotAvailable()
                    }

                    override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                        response.body()?.let {
                            when {
                                it.data.count() > 0 -> callback.onGifsLoaded(it.data)
                                else -> callback.onDataNotAvailable()
                            }
                        }
                    }
                })
            }
        }
        catch (e: Exception)
        {
            // We can improve it by detecting the issue and displaying
            // the appropriate message to the user
            callback.onDataNotAvailable()
        }
    }

    companion object {
        private var INSTANCE: GifsRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, giphyApiService: GiphyApiService): GifsRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(GifsRemoteDataSource::javaClass) {
                    INSTANCE = GifsRemoteDataSource(appExecutors, giphyApiService)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }

}