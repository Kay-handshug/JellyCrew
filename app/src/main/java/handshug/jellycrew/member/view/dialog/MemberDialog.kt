package handshug.jellycrew.member.view.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import handshug.jellycrew.R
import handshug.jellycrew.base.BaseDialog
import handshug.jellycrew.base.BindingDialog
import handshug.jellycrew.databinding.DialogMemberUserInfoValidityPeriodBinding
import handshug.jellycrew.member.viewModel.MemberViewModel

class MemberDialog(
    private val activity: Activity,
    private val viewModel: MemberViewModel
) : BaseDialog() {

    fun showUserinfoNotiDialog(): AlertDialog {
        val dialogBinding =
            BindingDialog<DialogMemberUserInfoValidityPeriodBinding>(
                    activity,
                    R.layout.dialog_member_user_info_validity_period
            )

        val dialog = dialogBinding.getAlertDialog()
        dialogBinding.binding.dialog = dialog
        return dialog
    }
}