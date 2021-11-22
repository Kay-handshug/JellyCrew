package handshug.jellycrew.member.view

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.InputType
import androidx.annotation.LayoutRes
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinPhoneBinding
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_START
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_STOP
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_REQUEST_VERIFY_SEND_FAIL
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST_VERIFY_FAIL
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST_VERIFY_SEND
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinPhoneFragment : BindingFragment<FragmentJoinPhoneBinding>() {

    private lateinit var timer: CountDownTimer

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_join_phone

    companion object {
        fun newInstance() = JoinPhoneFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        timer = viewModel.countDownTimer(
            binding.tvJoinPhoneInputVerifyNumberCountdown,
            binding.tvJoinPhoneInputErrorMsg,
            binding.btnJoinPhoneRequestVerify,
            binding.btnJoinPhoneNext
        )

        val dialog = MemberDialog(requireActivity(), viewModel)
        val dialogVerifySend = dialog.showToastDialog(getString(R.string.join_phone_verify_number_send))
        val dialogVerifyFail = dialog.showToastDialog(getString(R.string.join_phone_error_request_verify_fail))

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            toast(it)
        })

        viewModel.hideKeyboard.observe(viewLifecycleOwner, {
            hideKeyboard(it)
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    FRAGMENT_JOIN_EMAIL -> (activity as JoinActivity).moveChangePosition(2)
                    SHOW_DIALOG_TOAST_VERIFY_SEND -> {
                        val btnVerify = binding.btnJoinPhoneRequestVerify
                        dialogVerifySend.apply {
                            show()
                            btnVerify.isEnabled = false
                            Handler().postDelayed({
                                this.dismiss()
                                btnVerify.isEnabled = true
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
                    SHOW_DIALOG_REQUEST_VERIFY_SEND_FAIL -> dialog.showDialogTitleContents("인증문자가 계속 오지 않아요 :(", "고객센터 안내 내용")
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

    override fun onDestroy() {
        timer.cancel()
        super.onDestroy()
    }
}
