package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinPasswordBinding
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_USER_INFO
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinPasswordFragment : BindingFragment<FragmentJoinPasswordBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_join_password

    companion object {
        fun newInstance() = JoinPasswordFragment()
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

        viewModel.viewEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    FRAGMENT_JOIN_USER_INFO -> (activity as JoinActivity).moveChangePosition(4)
                }
            }
        })
    }
}
