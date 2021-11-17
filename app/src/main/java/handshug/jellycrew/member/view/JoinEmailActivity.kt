package handshug.jellycrew.member.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityJoinEmailBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_PASSWORD
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinEmailActivity : BindingActivity<ActivityJoinEmailBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_join_email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()
        this.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val dialog = MemberDialog(this, viewModel)

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> finish()
                    ACTIVITY_MAIN -> goToMainActivity()
                    ACTIVITY_JOIN_PASSWORD -> goToJoinPasswordActivity()
                }
            }
        })
    }

    private fun goToMainActivity() {
        Preference.isLogin = true

        TimeSynchronizer.sync()
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        ActivityUtil.removeActivity(this)
        finish()
    }

    private fun goToJoinPasswordActivity() {
        Intent(this, JoinPasswordActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
