package com.giphy.codechallenge.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.giphy.codechallenge.R
import com.giphy.codechallenge.util.obtainViewModel
import com.giphy.codechallenge.util.replaceFragmentInActivity
import com.giphy.codechallenge.util.setupActionBar


class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_act)

        setupActionBar(R.id.toolbar) {
            setDisplayHomeAsUpEnabled(false)
        }

        setupViewFragment()
    }

    private fun setupViewFragment() {
        supportFragmentManager.findFragmentById(R.id.contentFrame)
            ?: replaceFragmentInActivity(SearchFragment.newInstance(), R.id.contentFrame)
    }

    fun obtainViewModel(): SearchViewModel = obtainViewModel(SearchViewModel::class.java)
}
