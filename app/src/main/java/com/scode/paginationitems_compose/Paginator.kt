package com.scode.paginationitems_compose

interface Paginator<Item>{
    suspend fun loadNextItems()
    fun reset()
}