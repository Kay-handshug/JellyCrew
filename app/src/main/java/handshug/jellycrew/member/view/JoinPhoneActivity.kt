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
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.GET_COUNT_DOWN_TIMER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST
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
        val dialogVerifyFail = dialog.showToastDialog(getString(R.string.join_phone_error_request_verify_fail))

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> finish()
                    ACTIVITY_MAIN -> goToMainActivity()
                    SHOW_DIALOG_TOAST -> {
                        dialogVerifyFail.show()
                        Handler().postDelayed({
                            dialogVerifyFail.dismiss()
                        }, 2000L)
                    }
                    GET_COUNT_DOWN_TIMER -> {
                        timer.cancel()
                        timer.start()
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

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}
