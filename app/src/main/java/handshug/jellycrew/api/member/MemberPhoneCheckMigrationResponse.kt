package handshug.jellycrew.api.member

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.api.member.scheme.MemberAcountCafe24Data
import handshug.jellycrew.api.member.scheme.MemberAcountData
import handshug.jellycrew.api.member.scheme.MemberAcountSocialsData

data class MemberPhoneCheckMigrationResponse(
    @SerializedName("mobile") val mobile: String,
    @SerializedName("name") val name: String,
    @SerializedName("accountCafe24") val accountCafe24: MemberAcountCafe24Data,
    @SerializedName("account") val account: MemberAcountData,
    @SerializedName("accountSocials") val accountSocials: MemberAcountSocialsData
)
