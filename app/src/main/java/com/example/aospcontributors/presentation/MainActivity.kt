package com.example.aospcontributors.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.aospcontributors.R
import com.example.aospcontributors.presentation.contributors.ContributorListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showContributorsListFragment()
    }

    private fun showContributorsListFragment() {
        supportFragmentManager.findFragmentByTag(ContributorListFragment.TAG) ?: ContributorListFragment().let {
            supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.container, it, ContributorListFragment.TAG)
                    commit()
                }
        }
    }
}
