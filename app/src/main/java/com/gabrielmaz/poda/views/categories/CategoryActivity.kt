package com.gabrielmaz.poda.views.categories

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setSupportActionBar(findViewById(R.id.main_toolbar))

        if (savedInstanceState == null) {

            val category = intent.getParcelableExtra<Category>(categoryTag)

            title = category.name

            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.container,
                    CategoryFragment.newInstance(
                        intent.getParcelableArrayListExtra<Todo>(todosTag),
                        category
                    ),
                    CategoryFragment.categoryTag
                )
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_actionbar, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.sort -> {
            showDialog()
            true
        }
        else ->
            super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val array = arrayOf(
            getString(R.string.priority),
            getString(R.string.due_date),
            getString(R.string.creation_date)
        )

        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.sort_title)

        builder.setItems(array) { _, index ->

            supportFragmentManager.findFragmentByTag(CategoryFragment.categoryTag).also {
                it?.let { fragment ->
                    if (fragment.isVisible && fragment is CategoryFragment) {
                        fragment.sortList(index)
                    }
                }
            }
        }

        val dialog = builder.create()

        dialog.show()
    }

    companion object {

        const val todosTag = "todosTag"
        const val categoryTag = "categoryTag"
    }
}
