package social.bondoo.bookhive.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import social.bondoo.bookhive.model.BookHive
import social.bondoo.bookhive.model.Item
import javax.inject.Singleton

@Singleton
interface BookHiveAPI {
    @GET("volumes")
    suspend fun getBooks(
        @Query("q") query: String,
    ): BookHive

    @GET("volumes/{bookId}")
    suspend fun getBookById(
        @Path("bookId") bookId: String
    ): Item
}