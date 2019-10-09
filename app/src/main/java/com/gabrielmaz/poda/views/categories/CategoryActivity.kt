package com.gabrielmaz.poda.views.categories

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gabrielmaz.poda.R
import com.gabrielmaz.poda.models.Category
import com.gabrielmaz.poda.models.Todo

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

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
                    null
                )
                .commit()
        }

    }

    companion object {

        const val todosTag = "todosTag"
        const val categoryTag = "categoryTag"
    }
}
