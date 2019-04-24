package com.giphy.codechallenge.data.source

class GifsRepository(
    /* Later we can add a new data source for device storage data source.

     private val gifsLocalDataSource: LocalDataSource
     */
    private val gifsRemoteDataSource: GifsDataSource
) : GifsDataSource {

    override fun searchGifs(query: String, callback: GifsDataSource.LoadGifsCallback) {
        gifsRemoteDataSource.searchGifs(query, callback)
    }

    companion object {

        private var INSTANCE: GifsRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param gifsRemoteDataSource the backend data source
         * *
         * @return the [GifsRepository] instance
         */
        @JvmStatic fun getInstance(gifsRemoteDataSource: GifsDataSource) =
            INSTANCE ?: synchronized(GifsRepository::class.java) {
                INSTANCE ?: GifsRepository(gifsRemoteDataSource)
                    .also { INSTANCE = it }
            }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}