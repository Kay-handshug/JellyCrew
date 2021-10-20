package handshug.jellycrew.main.model

import com.google.gson.annotations.SerializedName

data class AdminContact (
    @SerializedName("name")
    var name: String = "",

    @SerializedName("is_login")
    var isLogin: Int = 0
)