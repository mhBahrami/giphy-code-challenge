package com.giphy.codechallenge.search

import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.giphy.codechallenge.Event
import com.giphy.codechallenge.R
import com.giphy.codechallenge.data.source.GifsDataSource
import com.giphy.codechallenge.data.source.GifsRepository
import com.giphy.sdk.core.models.Media
import java.util.ArrayList

/**
 * Exposes the data to be used in the gif list screen.
 *
 *
 * [BaseObservable] implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a [Bindable] annotation to the property's
 * getter method.
 */
class SearchViewModel(
    private val gifsRepository: GifsRepository
) : ViewModel() {

    val listAdapter: SearchResultAdapter = SearchResultAdapter()

    // Two-way data binding, exposing MutableLiveData
    val query = MutableLiveData<String>()

    private val _items = MutableLiveData<List<Media>>().apply { value = emptyList() }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _noResultLabelRes = MutableLiveData<Int>()
    val noResultLabelRes: LiveData<Int>
        get() = _noResultLabelRes

    private val _noResultIconRes = MutableLiveData<Int>()
    val noResultIconRes: LiveData<Int>
        get() = _noResultIconRes

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarMessage: LiveData<Event<Int>>
        get() = _snackbarText

    // These LiveData depend on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }
    val searchButtonEnable = MediatorLiveData<Boolean>()

    init {
        // Set initial state
        setupEmptyView(R.drawable.ic_gif, R.string.search_empty)
    }

    fun search() {
        replaceItems(listOf())
        isLoading(true)

        _snackbarText.value = Event(R.string.search_action)
        val currentQuery = query.value

        currentQuery?.let {
            if (it.trim().isNotEmpty()) {

                gifsRepository.searchGifs(query.value!!.trim(), object : GifsDataSource.LoadGifsCallback {
                    override fun onDataNotAvailable() {
                        setupEmptyView(R.drawable.ic_search, R.string.no_result)

                        isLoading(false)
                    }

                    override fun onGifsLoaded(gifs: List<Media>) {
                        replaceItems(gifs)
                        setupEmptyView(R.drawable.ic_gif, R.string.search_empty)

                        isLoading(false)
                    }
                })
            }
        }

    }

    fun queryChanged(@NonNull _query: String) {
        searchButtonEnable.value = _query.isNotEmpty()
    }

    fun isLoading(loading: Boolean) {
        _dataLoading.value = loading
        searchButtonEnable.value = !loading
    }

    fun replaceItems(gifs: List<Media>) {
        val itemsValue = ArrayList(gifs)
        _items.value = itemsValue
        listAdapter.replaceItems(itemsValue)
    }

    private fun setupEmptyView(@DrawableRes noGifIconDrawable: Int,
                               @StringRes noGifLabelString: Int) {
        _noResultIconRes.value = noGifIconDrawable
        _noResultLabelRes.value = noGifLabelString
    }
}