package social.bondoo.bookhive.screens.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import social.bondoo.bookhive.model.MUser

//@HiltViewModel
class LoginScreenViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    private val _errorMutableLiveData = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMutableLiveData


    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            _loading.value = true
            try {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        _loading.value = false
                        if (task.isSuccessful) {
                            _errorMutableLiveData.value = ""
                            home()
                        } else {
                            _loading.value = false
                            _errorMutableLiveData.value = task.exception?.message.toString()
                            Log.d("FB", "signInWithEmailAndPassword: ${task.exception?.toString()}")
                        }
                    }
            } catch (e: Exception) {
                _loading.value = false
                Log.d("login", "signInWithEmailAndPassword: ${e.message}")
            }
        }

    fun clearErrorMessage() {
        _errorMutableLiveData.value = ""
    }

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        _loading.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _loading.value = false
                if (task.isSuccessful) {
                    val displayName = task.result?.user?.email?.split('@')?.get(0)
                    val userEmail = task.result?.user?.email
                    if (displayName != null && userEmail != null) {
                            createUser(displayName, userEmail)
                    }
                    home()
                } else {
                    _loading.value = false
                    Log.d(
                        "FB",
                        "createUserWithEmailAndPassword: ${task.exception?.toString()}"
                    )
                }
            }
    }

    private fun createUser(displayName: String, email: String) {
        val userId = auth.currentUser?.uid
        val user = MUser(
            userId = userId.toString(),
            displayName = displayName,
            email = email,
            avatarUrl = "https://picsum.photos/300",
            quote = "Lift is Great",
            profession = "Android Developer"
        ).toMap()

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
    }
}