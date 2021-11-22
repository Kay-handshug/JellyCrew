package handshug.jellycrew.member.view.dialog

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import handshug.jellycrew.R
import handshug.jellycrew.base.BaseDialog
import handshug.jellycrew.base.BindingDialog
import handshug.jellycrew.databinding.*
import handshug.jellycrew.member.viewModel.MemberViewModel

class MemberDialog(
    private val activity: Activity,
    private val viewModel: MemberViewModel
) : BaseDialog() {

    fun showLoginFinishDialog(): AlertDialog {
        val dialogBinding =
                BindingDialog<DialogMemberFinishBinding>(
                        activity,
                        R.layout.dialog_member_finish
                )

        val dialog = dialogBinding.getAlertDialog()
        dialogBinding.binding.viewModel = viewModel
        dialogBinding.binding.dialog = dialog
        return dialog
    }

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
            binding.btnJoinUserInfoGenderFemale.setOnClickListener {
                viewModel.selectedGender.value = 0
                dialog.dismiss()
            }
            binding.btnJoinUserInfoGenderMale.setOnClickListener {
                viewModel.selectedGender.value = 1
                dialog.dismiss()
            }
            binding.btnJoinUserInfoGenderEtc.setOnClickListener {
                viewModel.selectedGender.value = 2
                dialog.dismiss()
            }
        }

        return dialog
    }

    fun showDialogTitleContents(title: String, contents: String) {
        val dialogBinding = BindingDialog<DialogMemberTitleNContentsBinding>(
                activity,
                R.layout.dialog_member_title_n_contents
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
            binding.dialog = dialog
            binding.title = title
            binding.contents = contents
        }

        dialog.show()
    }
}