package com.scode.paginationitems_compose

class DefaultPaginator<key, Item>(
    private val initialkey: key,
    private  val onLoadUpdated: (Boolean) -> Unit,
    private  val onRequest: suspend (nextKey:key) -> Result<List<Item>>,
    private  val getNextKey: suspend (List<Item>) -> key,
    private  val OnError: suspend (Throwable?) -> Unit,
    private  val onSuccess: suspend (items:List<Item>, newKey:key) -> Unit

): Paginator<Item> {

    private var currentKey = initialkey
    private var isMakingRequest = false

   override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        isMakingRequest = false
        val items = result.getOrElse {
            OnError(it)
            onLoadUpdated(false)
            return

        }
        currentKey = getNextKey(items)
        onSuccess(items, currentKey)
        onLoadUpdated(false)
    }


    override fun reset(){
            currentKey = initialkey
    }
}
