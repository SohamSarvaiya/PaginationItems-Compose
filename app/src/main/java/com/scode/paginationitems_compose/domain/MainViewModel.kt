package com.scode.paginationitems_compose.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scode.paginationitems_compose.presentation.ListItem
import kotlinx.coroutines.launch

class MainViewModel() : ViewModel(){

    private val repository = Repository()

    var state by mutableStateOf(ScreenState())

    private val paginator = DefaultPaginator(
        initialkey = state.page,
        onLoadUpdated = {
            state = state.copy(isLoading =  it)
        },
        onRequest = { nextPage ->
            repository.getItems(nextPage,20)
        },
        getNextKey = {
            state.page + 1
        },
        OnError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newKey ->
            state = state.copy(
                    items = state.items + items,
                    page = newKey,
                    endReached = items.isEmpty()
            )
        }
    )

     init {
         loadNextItems()

     }

    fun loadNextItems() {
        viewModelScope.launch {
            paginator.loadNextItems()
        }
    }

}

data class ScreenState(
    val isLoading:Boolean = false,
    val items:List<ListItem> = emptyList(),
    val error: String? = null,
    val endReached: Boolean = false,
    val page: Int = 0
)