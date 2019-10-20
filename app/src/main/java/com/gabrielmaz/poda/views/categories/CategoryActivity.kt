package com.gabrielmaz.poda.views.categories

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo

class CategoryActivity : AppCompatActivity(), CategoryFragment.OnFragmentInteractionListener {
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

    override fun todoListUpdated(newTodos: ArrayList<Todo>) {
        val result = Intent()
        result.putParcelableArrayListExtra(modifiedTodosTag, newTodos)
        setResult(Activity.RESULT_OK, result)
    }

    private fun createDialog(): AlertDialog {
        val options = linkedMapOf(
            getString(R.string.priority) to CategoryFragment.SortOption.Priority,
            getString(R.string.due_date) to CategoryFragment.SortOption.DueDate,
            getString(R.string.creation_date) to CategoryFragment.SortOption.CreationDate
        )

        val keys = options.keys.toTypedArray()

        val builder = AlertDialog.Builder(this)

        builder.setTitle(R.string.sort_title)

        builder.setItems(keys) { _, keyIndex ->
            supportFragmentManager.findFragmentByTag(CategoryFragment.categoryTag).also {
                it?.let { fragment ->
                    if (fragment.isVisible && fragment is CategoryFragment) {
                        fragment.sortList(options[keys[keyIndex]]!!)
                    }
                }
            }
        }

        return builder.create()
    }

    private fun showDialog() {
        val dialog = createDialog()
        dialog.show()
    }

    companion object {
        const val todosTag = "todosTag"
        const val categoryTag = "categoryTag"
        const val modifiedTodosTag = "modifiedTodos"
    }
}
