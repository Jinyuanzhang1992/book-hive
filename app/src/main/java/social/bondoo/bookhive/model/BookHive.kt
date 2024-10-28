package social.bondoo.bookhive.model

data class BookHive(
    val items: List<Item>,
    val kind: String,
    val totalItems: Int
)