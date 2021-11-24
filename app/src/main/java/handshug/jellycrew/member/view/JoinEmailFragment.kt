package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinEmailBinding
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PASSWORD
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.visible
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinEmailFragment : BindingFragment<FragmentJoinEmailBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_join_email

    companion object {
        fun newInstance() = JoinEmailFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            toast(it)
        })

        viewModel.hideKeyboard.observe(viewLifecycleOwner, {
            hideKeyboard(it)
        })

        viewModel.sameCheckEmail.observe(viewLifecycleOwner, { state ->
            if (!state) {
                goToPassword()
            }
            else {
                val tvErrorMsg = binding.tvJoinEmailInputErrorMsg
                tvErrorMsg.visible()
                tvErrorMsg.text = getString(R.string.join_email_already_use)
            }
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    FRAGMENT_JOIN_PASSWORD -> viewModel.sameCheckEmail(binding.etJoinEmailInput.text.toString())
                }
            }
        })
    }

    private fun goToPassword() {
        val userEmail = binding.etJoinEmailInput.text.toString()
        Preference.userEmail = userEmail
        (activity as JoinActivity).moveChangePosition(3)
    }
}
