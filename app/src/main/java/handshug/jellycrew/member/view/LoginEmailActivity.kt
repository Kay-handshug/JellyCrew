package handshug.jellycrew.member.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityLoginEmailBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_FIND_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_HOME
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_RESET_PASSWORD
import handshug.jellycrew.member.MemberContract.Companion.LOGIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.MEMBER_LOGIN
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.Log
import org.koin.androidx.viewmodel.ext.android.getViewModel


class LoginEmailActivity : BindingActivity<ActivityLoginEmailBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_login_email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()
        this.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.title = getString(R.string.login)

        val dialog = MemberDialog(this, viewModel)

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.hideKeyboard.observe(this, {
            this.currentFocus?.let { it1 -> hideKeyboard(it1, null) }
        })

        viewModel.isLoginSuccess.observe(this, { state ->
            if (state) goToMainActivity()
            else dialog.showDialogLoginNotFound()
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> goToLoginHome()
                    ACTIVITY_LOGIN_HOME -> goToLoginHome()
                    ACTIVITY_FIND_EMAIL -> goToFindEmail()
                    ACTIVITY_RESET_PASSWORD -> goToResetPassword()
                    MEMBER_LOGIN -> {
                        Preference.userEmail = binding.etLoginEmailInput.text.toString()
                        Preference.userPassword = binding.etLoginPasswordInput.text.toString()

                        viewModel.loginEmail()
                    }
                    LOGIN_SUCCESS -> goToMainActivity()
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

    private fun goToLoginHome() {
        startActivity(LoginActivity::class.java)
        finish()
    }

    private fun goToFindEmail() {

    }

    private fun goToResetPassword() {

    }
}
