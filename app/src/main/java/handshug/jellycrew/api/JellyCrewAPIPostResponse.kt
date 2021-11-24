package handshug.jellycrew.api

data class JellyCrewAPIPostResponse<T> (
    val code: String,
    val message: String,
    val data: T
)
