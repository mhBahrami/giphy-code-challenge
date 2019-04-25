package com.giphy.codechallenge.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.giphy.codechallenge.databinding.SearchFragBinding
import com.giphy.codechallenge.util.hideKeyboard
import com.giphy.codechallenge.util.onTextChanged
import com.giphy.codechallenge.util.setupSnackbar
import com.google.android.material.snackbar.Snackbar

/**
 * After the user searches for a GIF, displays a grid of GIFs if there is any.
 */

class SearchFragment : Fragment() {

    private lateinit var viewDataBinding: SearchFragBinding

    companion object {
        fun newInstance() = SearchFragment()
        private const val TAG = "SearchFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        viewDataBinding = SearchFragBinding.inflate(inflater, container, false).apply {
            viewmodel = (activity as SearchActivity).obtainViewModel()
        }
        setHasOptionsMenu(false)

        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.viewmodel?.let {
            view?.setupSnackbar(this, it.snackbarMessage, Snackbar.LENGTH_LONG)
        }
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSearchGifButton()
        setupSearchQueryEditText()
        setupListView()
    }

    private fun setupSearchGifButton() {
        viewDataBinding.searchGif.let {
            it.setOnClickListener { view ->
                viewDataBinding.viewmodel?.search()
                view.hideKeyboard()
            }
        }
    }

    private fun setupSearchQueryEditText() {
        viewDataBinding.searchQuery.onTextChanged {newText ->
            viewDataBinding.viewmodel?.queryChanged(newText)
        }
    }

    private fun setupListView() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            val gridLayoutManager = GridLayoutManager(activity, 2)
            viewDataBinding.results.layoutManager = gridLayoutManager
        } else {
            Log.w(TAG, "ViewModel not initialized when attempting to set up adapter.")
        }
    }


}