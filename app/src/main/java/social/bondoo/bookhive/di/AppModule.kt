package social.bondoo.bookhive.di

import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import social.bondoo.bookhive.network.BookHiveAPI
import social.bondoo.bookhive.repository.BookHiveRepository
import social.bondoo.bookhive.repository.FireRepository
import social.bondoo.bookhive.utils.Constants
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideBookHiveAPI(): BookHiveAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookHiveAPI::class.java)
    }

    @Singleton
    @Provides
    fun providerBookHiveRepository(bookHiveAPI: BookHiveAPI): BookHiveRepository {
        return BookHiveRepository(bookHiveAPI)
    }

    @Singleton
    @Provides
    fun provideFireBookRepository() =
        FireRepository(queryBook = FirebaseFirestore.getInstance().collection("books"))
}