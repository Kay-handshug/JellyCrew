package handshug.jellycrew.member.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import androidx.annotation.LayoutRes
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityJoinPhoneBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_START
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_STOP
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST_VERIFY_FAIL
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST_VERIFY_SEND
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinPhoneActivity : BindingActivity<ActivityJoinPhoneBinding>() {

    private lateinit var timer: CountDownTimer

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_join_phone

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()
        this.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        timer = viewModel.countDownTimer(binding.tvJoinPhoneInputVerifyNumberCountdown)

        val dialog = MemberDialog(this, viewModel)
        val dialogVerifySend = dialog.showToastDialog(getString(R.string.join_phone_verify_number_send))
        val dialogVerifyFail = dialog.showToastDialog(getString(R.string.join_phone_error_request_verify_fail))

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> finish()
                    ACTIVITY_MAIN -> goToMainActivity()
                    ACTIVITY_JOIN_EMAIL -> goToJoinEmailActivity()

                    SHOW_DIALOG_TOAST_VERIFY_SEND -> {
                        dialogVerifySend.apply {
                            show()
                            Handler().postDelayed({
                                this.dismiss()
                            }, 2000L)
                        }
                    }
                    SHOW_DIALOG_TOAST_VERIFY_FAIL -> {
                        dialogVerifyFail.apply {
                            show()
                            Handler().postDelayed({
                                this.dismiss()
                            }, 2000L)
                        }
                    }
                    COUNT_DOWN_TIMER_START -> {
                        timer.cancel()
                        timer.start()
                    }
                    COUNT_DOWN_TIMER_STOP -> {
                        timer.cancel()
                    }
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

    private fun goToJoinEmailActivity() {
        Intent(this, JoinEmailActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}
