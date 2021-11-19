package handshug.jellycrew.member.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import handshug.jellycrew.member.view.*

class JoinPagerAdapter(
    fragmentActivity: FragmentActivity
) : FragmentStateAdapter(fragmentActivity) {

    companion object {
        private const val TERMS = 0
        private const val PHONE = 1
        private const val EMAIL = 2
        private const val PASSWORD = 3
        private const val USER_INFO = 4
        private const val SUCCESS = 5
    }

    private val fragmentsCreators: Map<Int, () -> Fragment?> = mapOf(
            TERMS to { JoinTermsFragment.newInstance() },
            PHONE to { JoinPhoneFragment.newInstance() },
            EMAIL to { JoinEmailFragment.newInstance() },
            PASSWORD to { JoinPasswordFragment.newInstance() },
            USER_INFO to { JoinUserInfoFragment.newInstance() }
    )

    override fun getItemCount(): Int {
        return fragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}