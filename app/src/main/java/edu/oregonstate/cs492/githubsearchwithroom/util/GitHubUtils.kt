package edu.oregonstate.cs492.githubsearchwithroom.util

import android.text.TextUtils

/**
 * This method constructs a complete GitHub search query that incorporates not only a specific
 * query term (from the parameter `query`) but also search qualifiers for a particular user,
 * set of languages, and the number of "good first issues".  See the GitHub API documentation
 * for more details about how this query is constructed:
 *
 * https://docs.github.com/en/rest/search#constructing-a-search-query
 */
fun buildGitHubQuery(
    query: String,
    user: String?,
    languages: Set<String>?,
    firstIssues: Int
) : String {
    /*
     * e.g. "android user:square language:kotlin language:java good-first-issues:>=2"
     */
    var fullQuery = query
    if (!TextUtils.isEmpty(user)) {
        fullQuery += " user:$user"
    }
    if (languages != null && languages.isNotEmpty()) {
        fullQuery += languages.joinToString(separator = " language:", prefix = " language:")
    }
    if (firstIssues > 0) {
        fullQuery += " good-first-issues:>=$firstIssues"
    }
    return fullQuery
}