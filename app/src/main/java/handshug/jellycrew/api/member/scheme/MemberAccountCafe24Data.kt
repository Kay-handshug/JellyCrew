package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberAccountCafe24Data(
    @SerializedName("id") val id: String,
    @SerializedName("email") val email: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("name") val name: String,
    @SerializedName("birthday") val birthday: String,
    @SerializedName("joinedAt") val joinedAt: String,
    @SerializedName("migratedAt") val migratedAt: String,
    @SerializedName("usableMoney") val usableMoney: Long
)
