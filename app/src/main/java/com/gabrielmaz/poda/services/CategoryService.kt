package com.gabrielmaz.poda.services

import com.gabrielmaz.poda.models.Category
import retrofit2.http.GET

interface CategoryService {
    @GET("categories")
    suspend fun getCategories(): List<Category>
}