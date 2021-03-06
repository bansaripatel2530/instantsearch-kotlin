package searchbox

import android.widget.SearchView
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.android.searchbox.SearchBoxViewImpl
import com.algolia.instantsearch.helper.searchbox.connectView
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import shouldEqual


@SmallTest
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TestSearchBoxSearchView {

    private val text = "text"

    private fun view() = SearchBoxViewImpl(SearchView(ApplicationProvider.getApplicationContext()))

    @Test
    fun connectShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.query = text
        viewModel.connectView(view)
        view.searchView.query.toString() shouldEqual text
    }

    @Test
    fun onQueryChangedShouldUpdateQuery() {
        val view = view()
        val viewModel = SearchBoxViewModel()

        viewModel.connectView(view)
        view.searchView.setQuery(text, false)
        viewModel.query shouldEqual text
    }

    @Test
    fun onSubmitShouldCallOnQuerySubmitted() {
        val view = view()
        val viewModel = SearchBoxViewModel()
        var expected: String? = null

        viewModel.onQuerySubmitted += { expected = it }
        viewModel.connectView(view)
        view.searchView.setQuery(text, true)
        expected shouldEqual text
    }
}