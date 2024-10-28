package social.bondoo.bookhive.model

data class MUser(
    val id: String? = null,
    val userId: String,
    val displayName: String,
    val email: String,
    val avatarUrl: String,
    val quote: String,
    val profession: String,
) {
    fun toMap(): Map<String, Any> {
        return mutableMapOf(
            "userId" to userId,
            "displayName" to displayName,
            "email" to email,
            "quote" to quote,
            "avatarUrl" to avatarUrl,
            "profession" to profession,
        )
    }
}
