package social.bondoo.bookhive.screens.details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import social.bondoo.bookhive.data.Resource
import social.bondoo.bookhive.model.AccessInfo
import social.bondoo.bookhive.model.Item
import social.bondoo.bookhive.repository.BookHiveRepository
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val repository: BookHiveRepository
) : ViewModel() {
    var book:Item? by mutableStateOf(null)
    var isLoading: Boolean by mutableStateOf(true)

    fun getBookById(bookId: String) {
        viewModelScope.launch {
            if (bookId.isEmpty()) return@launch
            try {
                when(val response = repository.getBookInfo(bookId)){
                    is Resource.Success -> {
                        book = response.data!!
                        if (book != null) isLoading = false
                    }
                    is Resource.Error -> {
                        isLoading = false
                        Log.e("Network", "getBookById: Failed getting book", )
                    }
                    else -> {isLoading = false}
                }
            }catch (exception: Exception){
                isLoading = false
                Log.d("Network", "getBookById: ${exception.message.toString()}")
            }
        }
    }
}