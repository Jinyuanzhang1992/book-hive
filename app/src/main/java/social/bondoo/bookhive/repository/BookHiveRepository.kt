package social.bondoo.bookhive.repository

import social.bondoo.bookhive.data.Resource
import social.bondoo.bookhive.model.Item
import social.bondoo.bookhive.network.BookHiveAPI
import javax.inject.Inject

class BookHiveRepository @Inject constructor(
    private val bookHiveAPI: BookHiveAPI
) {
    suspend fun getBooks(searchQuery: String): Resource<List<Item>> {
        return try {
            Resource.Loading(data = true)
            val itemList = bookHiveAPI.getBooks(searchQuery).items
            if (itemList.isNotEmpty()) Resource.Loading(data = false)
            Resource.Success(data = itemList)
        }catch (exception: Exception) {
            Resource.Error(message = exception.message.toString())
        }
    }

    suspend fun getBookInfo(bookId: String): Resource<Item> {
        val response = try {
            Resource.Loading(data = true)
            bookHiveAPI.getBookById(bookId)
        }catch (exception: Exception){
            return Resource.Error(message = "An error occurred ${exception.message.toString()}")
        }
        Resource.Loading(data = false)
        return Resource.Success(data = response)
    }
}
