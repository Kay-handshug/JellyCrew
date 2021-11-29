package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.datepicker.MaterialDatePicker
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinUserInfoBinding
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_DATE_PICKER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_GENDER
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinUserInfoFragment : BindingFragment<FragmentJoinUserInfoBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.fragment_join_user_info

    companion object {
        fun newInstance() = JoinUserInfoFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val dialog = MemberDialog(requireActivity(), viewModel)
        val dialogGender = dialog.showGenderDialog()

        viewModel.toastMessage.observe(viewLifecycleOwner, {
            toast(it)
        })

        viewModel.hideKeyboard.observe(viewLifecycleOwner, {
            hideKeyboard(it)
        })

        viewModel.selectedGender.observe(viewLifecycleOwner, { index ->
            binding.tvJoinUserInfoGender.apply {
                binding.ivJoinUserInfoGenderDropDown.isSelected = true
                isSelected = true
                ViewUtil.setBackgroundDrawable(this, R.drawable.selector_btn_radius08_gray400_n_gray700)

                text = when (index) {
                    0 -> getString(R.string.join_user_info_gender_female)
                    1 -> getString(R.string.join_user_info_gender_male)
                    else -> getString(R.string.join_user_info_gender_etc)
                }

                isVerifyAllOk(
                        binding.btnJoinUserInfoNext,
                        binding.tvJoinUserInfoBirth,
                        binding.tvJoinUserInfoGender
                )
            }
        })

        viewModel.isJoinSuccess.observe(viewLifecycleOwner, { state ->
            if (state) {
                goToJoinSuccess()
            }
            else {
                toast(getString(R.string.toast_request_join_fail))
            }
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_JOIN_CONFIRM -> {
                        val birth = binding.tvJoinUserInfoBirth.text.toString()
                        val gender = viewModel.selectedGender.value?: 0

                        Preference.userBirth = birth
                        Preference.userGender = EtcUtil.getGenderType(gender)
                        if (Preference.loginType != 0) Preference.userPassword = ""

                        viewModel.joinConfirm()
                    }
                    SHOW_DIALOG_DATE_PICKER -> {
                        val builder = MaterialDatePicker.Builder.datePicker()
                                .setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                        val picker = builder.build().apply {
                            addOnNegativeButtonClickListener { dismiss() }
                            addOnPositiveButtonClickListener { date ->

                                if(!FormatterUtil.isOver14YearsOld(date)) {
                                    binding.tvJoinUserInfoBirthErrorMsg.visible()
                                }
                                else {
                                    binding.tvJoinUserInfoBirthErrorMsg.gone()
                                    val dateString = FormatterUtil.getDateTimestamp(date)
                                    binding.tvJoinUserInfoBirth.text = dateString
                                    binding.tvJoinUserInfoBirth.isSelected = true
                                    binding.ivJoinUserInfoBirthDropDown.isSelected = true
                                    ViewUtil.setBackgroundDrawable(binding.tvJoinUserInfoBirth, R.drawable.selector_btn_radius08_gray400_n_gray700)

                                    isVerifyAllOk(
                                            binding.btnJoinUserInfoNext,
                                            binding.tvJoinUserInfoBirth,
                                            binding.tvJoinUserInfoGender
                                    )
                                }
                            }
                        }

                        picker.show(parentFragmentManager, picker.toString())
                    }
                    SHOW_DIALOG_GENDER -> {
                        dialogGender.show()
                    }
                }
            }
        })
    }

    private fun isVerifyAllOk(
            btnNext: AppCompatButton,
            tvBirth: AppCompatTextView,
            tvGender: AppCompatTextView) {
        val isVerify = tvBirth.isSelected && tvGender.isSelected
        btnNext.isSelected = isVerify
        btnNext.isEnabled = isVerify
    }

    private fun goToJoinSuccess() {
        startActivity(JoinSuccessActivity::class.java)
        activity?.finish()
    }
}
