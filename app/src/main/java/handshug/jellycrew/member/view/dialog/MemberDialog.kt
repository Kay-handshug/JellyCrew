package handshug.jellycrew.member.view.dialog

import android.app.Activity
//import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import handshug.jellycrew.R
import handshug.jellycrew.base.BaseDialog
import handshug.jellycrew.base.BindingDialog
import handshug.jellycrew.databinding.DialogMemberGenderBinding
import handshug.jellycrew.databinding.DialogMemberToastBinding
import handshug.jellycrew.databinding.DialogMemberUserInfoValidityPeriodBinding
import handshug.jellycrew.member.viewModel.MemberViewModel

class MemberDialog(
    private val activity: Activity,
    private val viewModel: MemberViewModel
) : BaseDialog() {

    fun showUserinfoNotiDialog(): BottomSheetDialog {
        val dialogBinding =
            BindingDialog<DialogMemberUserInfoValidityPeriodBinding>(
                    activity,
                    R.layout.dialog_member_user_info_validity_period
            )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.binding.dialog = dialog
        return dialog
    }

    fun showToastDialog(msg: String): BottomSheetDialog {
        val dialogBinding =
            BindingDialog<DialogMemberToastBinding>(
                    activity,
                    R.layout.dialog_member_toast
            )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.binding.dialog = dialog
        dialogBinding.binding.msg = msg
        return dialog
    }

    fun showGenderDialog(): BottomSheetDialog {
        val dialogBinding = BindingDialog<DialogMemberGenderBinding>(
                activity,
                R.layout.dialog_member_gender
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.dialog = dialog
            binding.btnJoinUserInfoGenderMale.setOnClickListener {
                viewModel.selectedGender.value = false
                dialog.dismiss()
            }
            binding.btnJoinUserInfoGenderFemale.setOnClickListener {
                viewModel.selectedGender.value = true
                dialog.dismiss()
            }
        }


        return dialog
    }
}