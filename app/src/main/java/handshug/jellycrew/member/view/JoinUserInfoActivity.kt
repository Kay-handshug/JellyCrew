package handshug.jellycrew.member.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.datepicker.MaterialDatePicker
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityJoinUserInfoBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_DATE_PICKER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_GENDER
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.FormatterUtil
import handshug.jellycrew.utils.Log
import org.koin.androidx.viewmodel.ext.android.getViewModel


class JoinUserInfoActivity : BindingActivity<ActivityJoinUserInfoBinding>() {

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_join_user_info

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()
        this.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        val dialog = MemberDialog(this, viewModel)
        val dialogGender = dialog.showGenderDialog()

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.selectedGender.observe(this, { isFemale ->
            binding.tvJoinUserInfoGender.apply {
                binding.ivJoinUserInfoGenderDropDown.isSelected = true
                isSelected = true
                text = if (isFemale) {
                    getString(R.string.join_user_info_gender_female)
                } else {
                    getString(R.string.join_user_info_gender_male)
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

        viewModel.viewEvent.observe(this, { it ->
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> finish()
                    ACTIVITY_MAIN -> goToMainActivity()
                    SHOW_DIALOG_DATE_PICKER -> {
                        val builder = MaterialDatePicker.Builder.datePicker()
                                .setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
                        val picker = builder.build().apply {
                            addOnNegativeButtonClickListener { dismiss() }
                            addOnPositiveButtonClickListener { date ->
                                val dateString = FormatterUtil.getDateTimestamp(date)
                                binding.tvJoinUserInfoBirth.text = dateString
                                binding.tvJoinUserInfoBirth.isSelected = true
                                binding.ivJoinUserInfoBirthDropDown.isSelected = true

                                isVerifyAllOk(
                                        binding.btnJoinUserInfoNext,
                                        binding.tvJoinUserInfoRule01,
                                        binding.tvJoinUserInfoRule02,
                                        binding.tvJoinUserInfoBirth,
                                        binding.tvJoinUserInfoGender
                                )
                            }
                        }

                        picker.show(supportFragmentManager, picker.toString())
                    }
                    SHOW_DIALOG_GENDER -> {
                        dialogGender.show()
                    }
                }
            }
        })
    }

    private fun goToMainActivity() {
        Preference.isLogin = true

        TimeSynchronizer.sync()
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        ActivityUtil.removeActivity(this)
        finish()
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

    override fun onDestroy() {
        super.onDestroy()
    }
}
