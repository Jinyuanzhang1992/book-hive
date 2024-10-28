package social.bondoo.bookhive.data

import social.bondoo.bookhive.model.BookHive
import social.bondoo.bookhive.model.Item

data class DataOrException<T,Boolean,E:Exception>(
    var data:T? = null,
    var loading:Boolean? = null,
    var e:E? = null
)

typealias ResponseAlias = DataOrException<List<Item>, Boolean, Exception>
typealias ResponseAliasItem = DataOrException<Item, Boolean, Exception>