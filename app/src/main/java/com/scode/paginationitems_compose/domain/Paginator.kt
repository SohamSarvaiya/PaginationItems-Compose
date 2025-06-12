package com.scode.paginationitems_compose.domain

interface Paginator<Item>{
    suspend fun loadNextItems()
    fun reset()
}