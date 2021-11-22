package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.datepicker.MaterialDatePicker
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentJoinUserInfoBinding
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_DATE_PICKER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_GENDER
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.FormatterUtil
import handshug.jellycrew.utils.ViewUtil
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.visible
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
                        binding.tvJoinUserInfoRule01,
                        binding.tvJoinUserInfoRule02,
                        binding.tvJoinUserInfoBirth,
                        binding.tvJoinUserInfoGender
                )
            }
        })

        viewModel.viewEvent.observe(viewLifecycleOwner, { it ->
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_JOIN_SUCCESS -> {
                        startActivity(JoinSuccessActivity::class.java)
                        activity?.finish()
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
                                            binding.tvJoinUserInfoRule01,
                                            binding.tvJoinUserInfoRule02,
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
            tvRule01: AppCompatTextView,
            tvRule02: AppCompatTextView,
            tvBirth: AppCompatTextView,
            tvGender: AppCompatTextView) {
        val isVerify = tvRule01.isSelected && tvRule02.isSelected && tvBirth.isSelected && tvGender.isSelected
        btnNext.isSelected = isVerify
        btnNext.isEnabled = isVerify
    }
}
