package handshug.jellycrew.api.member.scheme

import com.google.gson.annotations.SerializedName

data class MemberAccountMoneyData(
    @SerializedName("id") val id: Long,
    @SerializedName("accountId") val accountId: Long,
    @SerializedName("money") val money: Long,
    @SerializedName("totalMoney") val totalMoney: Long,
    @SerializedName("reason") val reason: String,
    @SerializedName("createdAt") val createdAt: String
)
