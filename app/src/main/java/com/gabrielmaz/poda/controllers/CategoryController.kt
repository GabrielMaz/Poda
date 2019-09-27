package com.gabrielmaz.poda.controllers

import com.gabrielmaz.poda.services.CategoryService

class CategoryController {
    private val categoryService = RetrofitController.retrofit.create(CategoryService::class.java)

    suspend fun getCategories() = categoryService.getCategories()
}