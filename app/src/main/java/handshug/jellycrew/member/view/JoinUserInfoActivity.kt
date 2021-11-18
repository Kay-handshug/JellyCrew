package handshug.jellycrew.member.view

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
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

        viewModel.toastMessage.observe(this, {
            toast(it)
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
                            }
                        }

                        picker.show(supportFragmentManager, picker.toString())
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

    override fun onDestroy() {
        super.onDestroy()
    }
}
