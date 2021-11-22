package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinTermsBinding
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PHONE
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TERMS_DETAIL_01
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TERMS_DETAIL_02
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_USER_INFO_NOTI
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinTermsFragment : BindingFragment<FragmentJoinTermsBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_join_terms

    companion object {
        fun newInstance() = JoinTermsFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val dialog = MemberDialog(requireActivity(), viewModel)
        val showDialogUserInfoNoti = dialog.showUserinfoNotiDialog()

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            toast(it)
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    SHOW_DIALOG_TERMS_DETAIL_01 -> dialog.showDialogTitleContents("이용약관", getString(R.string.join_terms_test_contents_s))
                    SHOW_DIALOG_TERMS_DETAIL_02 -> dialog.showDialogTitleContents("개인정보수집 및 이용동의", getString(R.string.join_terms_test_contents_l))
                    SHOW_DIALOG_USER_INFO_NOTI -> showDialogUserInfoNoti.show()
                    FRAGMENT_JOIN_PHONE -> (activity as JoinActivity).moveChangePosition(1)
                }
            }
        })
    }
}
