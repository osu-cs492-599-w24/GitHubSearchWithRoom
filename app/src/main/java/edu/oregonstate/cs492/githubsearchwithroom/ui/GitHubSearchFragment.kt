package edu.oregonstate.cs492.githubsearchwithroom.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator
import edu.oregonstate.cs492.githubsearchwithroom.R
import edu.oregonstate.cs492.githubsearchwithroom.data.GitHubRepo
import edu.oregonstate.cs492.githubsearchwithroom.util.LoadingStatus
import edu.oregonstate.cs492.githubsearchwithroom.util.buildGitHubQuery

class GitHubSearchFragment : Fragment(R.layout.fragment_github_search) {
    private val viewModel: GitHubSearchViewModel by viewModels()
    private val adapter = GitHubRepoListAdapter(::onGitHubRepoClick)

    private lateinit var searchResultsListRV: RecyclerView
    private lateinit var searchErrorTV: TextView
    private lateinit var loadingIndicator: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBoxET: EditText = view.findViewById(R.id.et_search_box)
        val searchBtn: Button = view.findViewById(R.id.btn_search)

        searchErrorTV = view.findViewById(R.id.tv_search_error)
        loadingIndicator = view.findViewById(R.id.loading_indicator)

        searchResultsListRV = view.findViewById(R.id.rv_search_results)
        searchResultsListRV.layoutManager = LinearLayoutManager(requireContext())
        searchResultsListRV.setHasFixedSize(true)

        searchResultsListRV.adapter = adapter

        viewModel.searchResults.observe(viewLifecycleOwner) {
                searchResults -> adapter.updateRepoList(searchResults)
        }

        viewModel.loadingStatus.observe(viewLifecycleOwner) {
                loadingStatus ->
            when (loadingStatus) {
                LoadingStatus.LOADING -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.VISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
                LoadingStatus.ERROR -> {
                    searchResultsListRV.visibility = View.INVISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.VISIBLE
                }
                else -> {
                    searchResultsListRV.visibility = View.VISIBLE
                    loadingIndicator.visibility = View.INVISIBLE
                    searchErrorTV.visibility = View.INVISIBLE
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) {
                error -> searchErrorTV.text = getString(
            R.string.search_error,
            error
        )
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.github_search_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_bookmarks -> {
                            val directions = GitHubSearchFragmentDirections.navigateToBookmarkedRepos()
                            findNavController().navigate(directions)
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
        searchBtn.setOnClickListener {
            val query = searchBoxET.text.toString()
            if (!TextUtils.isEmpty(query)) {
                val sort = prefs.getString(getString(R.string.pref_sort_key), null)
                val user = prefs.getString(getString(R.string.pref_user_key), null)
                val fistIssues = prefs.getInt(getString(R.string.pref_first_issues_key), 0)
                val languages = prefs.getStringSet(getString(R.string.pref_language_key), null)
                viewModel.loadSearchResults(
                    buildGitHubQuery(query, user, languages, fistIssues),
                    sort
                )
                searchResultsListRV.scrollToPosition(0)
            }
        }
    }

    private fun onGitHubRepoClick(repo: GitHubRepo) {
        val directions = GitHubSearchFragmentDirections.navigateToRepoDetail(repo)
        findNavController().navigate(directions)
    }
}