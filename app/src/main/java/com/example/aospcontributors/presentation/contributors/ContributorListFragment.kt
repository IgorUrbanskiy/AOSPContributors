package com.example.aospcontributors.presentation.contributors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aospcontributors.R
import com.example.aospcontributors.databinding.FragmentContributorsListBinding
import com.example.aospcontributors.presentation.Resource
import com.example.aospcontributors.presentation.map.EXTRA_ADDRESS
import com.example.aospcontributors.presentation.map.MapActivity

/**
 * Created by Ihor Urbanskyi on 30.03.2019.
 */

class ContributorListFragment : Fragment(), ContributorsListAdapter.ItemClickListener {

    companion object {
        const val TAG = "ContributorListFragment"
    }

    private val contributorsAdapter: ContributorsListAdapter = ContributorsListAdapter(this)

    private lateinit var progress: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentContributorsListBinding>(
            inflater,
            R.layout.fragment_contributors_list,
            container,
            false
        )
        activity?.title = getString(R.string.contributors_toolbar_title)
        progress = binding.progressBar
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = contributorsAdapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val contributorsListViewModel = ViewModelProviders.of(this).get(ContributorsListViewModel::class.java)
        contributorsListViewModel.contributorsLiveData.observe(this, Observer {
            handleContributors(it)
        })
    }

    private fun handleContributors(resource: Resource<List<ContributorItem>>) {
        when (resource.status) {
            Resource.Status.SUCCESS -> {
                hideProgress()
                showContributorsList(resource.data)
            }
            Resource.Status.ERROR -> {
                hideProgress()
                showError()
            }
            Resource.Status.LOADING -> showProgress()
        }
    }

    private fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        progress.visibility = View.GONE
    }

    private fun showError() {
        Toast.makeText(context, R.string.error_during_data_downloading, Toast.LENGTH_LONG).show()
    }

    private fun showContributorsList(contributors: List<ContributorItem>?) {
        contributorsAdapter.setContributors(contributors)
    }

    override fun onLocationClick(location: String) {
        println("onLocationClick $location")
        val intent = Intent(context, MapActivity::class.java).apply {
            putExtra(EXTRA_ADDRESS, location)
        }
        startActivity(intent)
    }
}