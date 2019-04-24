package com.giphy.codechallenge

import com.giphy.codechallenge.data.source.GifsRepository
import com.giphy.codechallenge.data.source.remote.GifsRemoteDataSource
import com.giphy.codechallenge.data.source.remote.GiphyApiService
import com.giphy.codechallenge.util.AppExecutors

object Injection {

    fun provideGifsRepository(): GifsRepository {
        return GifsRepository.getInstance(
            // Later we can add a new data source for database.
            GifsRemoteDataSource.getInstance(AppExecutors(), provideGiphyApiService()))
    }

    private fun provideGiphyApiService(): GiphyApiService {
        return GiphyApiService.getInstance()
    }
}