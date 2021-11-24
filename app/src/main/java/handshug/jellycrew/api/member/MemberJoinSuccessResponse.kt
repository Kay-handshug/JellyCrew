package handshug.jellycrew.api.member

import com.google.gson.annotations.SerializedName
import handshug.jellycrew.api.member.scheme.MemberAccountData
import handshug.jellycrew.api.member.scheme.MemberAccountMoneyData
import handshug.jellycrew.api.member.scheme.MemberAccountSocialsData

data class MemberJoinSuccessResponse(
        @SerializedName("account") val account: MemberAccountData?,
        @SerializedName("accountMoney") val accountMoney: MemberAccountMoneyData?,
        @SerializedName("accountSocials") val accountSocials: List<MemberAccountSocialsData>?
)
