package handshug.jellycrew.member.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import handshug.jellycrew.member.view.*

class JoinPagerAdapter(
        fragmentActivity: FragmentActivity,
        private val isSocialJoin: Boolean = false
) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val TERMS = 0
        private const val PHONE = 1
        private const val EMAIL = 2
        private const val PASSWORD = 3
        private const val USER_INFO = 4
        private const val SUCCESS = 5

        private const val SNS_PHONE = 0
        private const val SNS_EMAIL = 1
        private const val SNS_PASSWORD = 2
        private const val SNS_USER_INFO = 3
    }

    private val fragmentsCreators: Map<Int, () -> Fragment?> = mapOf(
            TERMS to { JoinTermsFragment.newInstance() },
            PHONE to { JoinPhoneFragment.newInstance() },
            EMAIL to { JoinEmailFragment.newInstance() },
            PASSWORD to { JoinPasswordFragment.newInstance() },
            USER_INFO to { JoinUserInfoFragment.newInstance() }
    )

    private val fragmentsCreatorsForSocial: Map<Int, () -> Fragment?> = mapOf(
            SNS_PHONE to { JoinPhoneFragment.newInstance() },
            SNS_EMAIL to { JoinEmailFragment.newInstance() },
            SNS_PASSWORD to { JoinPasswordFragment.newInstance() },
            SNS_USER_INFO to { JoinUserInfoFragment.newInstance() }
    )

    override fun getItemCount(): Int {
        return if (isSocialJoin) fragmentsCreatorsForSocial.size else fragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return if (isSocialJoin) fragmentsCreatorsForSocial[position]?.invoke() ?: throw IndexOutOfBoundsException()
        else fragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}