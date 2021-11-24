package handshug.jellycrew.api.member

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.api.member.scheme.MemberAccountCafe24Data
import handshug.jellycrew.api.member.scheme.MemberAccountData
import handshug.jellycrew.api.member.scheme.MemberAccountSocialsData

data class MemberPhoneCheckMigrationResponse(
        @SerializedName("mobile") val mobile: String,
        @SerializedName("name") val name: String,
        @SerializedName("accountCafe24") val accountCafe24: MemberAccountCafe24Data,
        @SerializedName("account") val account: MemberAccountData,
        @SerializedName("accountSocials") val accountSocials: List<MemberAccountSocialsData>
)
