package handshug.jellycrew.member.view.dialog

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.api.member.scheme.MemberAccountCafe24Data
import handshug.jellycrew.api.member.scheme.MemberAccountData
import handshug.jellycrew.api.member.scheme.MemberAccountSocialsData
import handshug.jellycrew.base.BaseDialog
import handshug.jellycrew.base.BindingDialog
import handshug.jellycrew.databinding.*
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.FormatterUtil
import kotlin.text.StringBuilder

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

    fun showDialogSendSmsFail() {
        val dialogBinding = BindingDialog<DialogMemberPhoneVerifyFailBinding>(
                activity,
                R.layout.dialog_member_phone_verify_fail
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
        }

        dialog.show()
    }

    fun showDialogAlreadyJoinUser(account: MemberAccountData?, socials: List<MemberAccountSocialsData>?) {
        val dialogBinding = BindingDialog<DialogMemberAlreadyJoinBinding>(
                activity,
                R.layout.dialog_member_already_join
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
            binding.dialog = dialog

            val accountEmail = account?.email?: ""
            val maskedEmail = if (accountEmail.isNotEmpty()) FormatterUtil.maskedEmail(accountEmail) else ""
            binding.email = maskedEmail

            val socialsType: StringBuilder = StringBuilder()
            val maxCnt = socials?.size?: 0
            for (i in 1..maxCnt) {
                val type = socials?.get(i)?.socialType
                socialsType.append(type)
                if (maxCnt != i) {
                    socialsType.append(",")
                }
            }
            binding.socialsType = socialsType.toString()
        }

        dialog.show()
    }

    fun showDialogAlreadyCafe24Member(phoneNumber: String, name: String, cafe24: MemberAccountCafe24Data) {
        val dialogBinding = BindingDialog<DialogMemberForCafe24Binding>(
                activity,
                R.layout.dialog_member_for_cafe24
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
            binding.dialog = dialog
            binding.phone = phoneNumber
            binding.name = name

            binding.btnMemberCafe24Next.setOnClickListener {
                Preference.apply {
                    isCafe24MemberJoin = true
                    userName = name
                    userPhoneNumber = phoneNumber
                    userMoney = cafe24.usableMoney
                    userEmail = cafe24.email
                    userBirth = FormatterUtil.convertServerDate(cafe24.birthday)
                }

                viewModel.navigateToJoinEmailCafe24()
                dialog.dismiss()
            }

            binding.btnMemberCafe24NewJoin.setOnClickListener {
                Preference.apply {
                    isCafe24MemberJoin = false
                    userName = name
                    userPhoneNumber = phoneNumber
                }

                viewModel.navigateToJoinEmailCafe24()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    fun showDialogAlreadyCafe24MemberNotFound() {
        val dialogBinding = BindingDialog<DialogMemberForCafe24NotFoundBinding>(
                activity,
                R.layout.dialog_member_for_cafe24_not_found
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
            binding.dialog = dialog

            binding.btnMemberCafe24NotFoundNext.setOnClickListener {

            }

            binding.btnMemberCafe24NotFoundNewJoin.setOnClickListener {

            }

            binding.tvMemberCafe24NotFoundCsKakao.setOnClickListener {
                Toast.makeText(activity, "toast cs kakao", Toast.LENGTH_SHORT).show()
            }

            binding.tvMemberCafe24NotFoundCsEmail.setOnClickListener {
                Toast.makeText(activity, "toast cs email", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    fun showDialogLoginNotFound() {
        val dialogBinding = BindingDialog<DialogMemberNotFoundBinding>(
                activity,
                R.layout.dialog_member_not_found
        )

        val dialog = dialogBinding.getBottomSheetDialog()
        dialogBinding.apply {
            binding.viewModel = viewModel
        }

        dialog.show()
    }
}