package handshug.jellycrew.error

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityMemberNotFoundBinding
import handshug.jellycrew.member.MemberContract
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_TERMS
import handshug.jellycrew.member.view.JoinActivity
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.getViewModel

class Error2001Activity : BindingActivity<ActivityMemberNotFoundBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_member_not_found

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> this.finish()
                    ACTIVITY_JOIN_CONFIRM -> this.finish()
                    FRAGMENT_JOIN_TERMS -> goToJoinTerms()
                }
            }
        })

        hideProgressBar()
    }

    private fun goToJoinTerms() {
        Intent(this, JoinActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        ActivityUtil.removeActivity(this)
        this.finish()
    }
}
