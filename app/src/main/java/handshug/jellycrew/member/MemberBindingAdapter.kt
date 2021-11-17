package handshug.jellycrew.member

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import handshug.jellycrew.R
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.visible
import kotlinx.android.synthetic.main.activity_join_email.view.*
import kotlinx.android.synthetic.main.activity_join_phone.view.*
import kotlinx.android.synthetic.main.activity_join_terms.view.*
import kotlinx.android.synthetic.main.common_title_join.view.*


@BindingAdapter("setCheckBoxAll")
fun ConstraintLayout.setCheckBoxAll(isUsed: Boolean = false) {
    if(!isUsed) return

    val cbAgreeAll = this.cb_join_terms_agree_all
    val cbAgreeItem01 = this.cb_join_terms_agree_item_01
    val cbAgreeItem02 = this.cb_join_terms_agree_item_02
    val cbAgreeItem03 = this.cb_join_terms_agree_item_03
    val cbAgreeItem04 = this.cb_join_terms_agree_item_04
    val cbAgreeItem05 = this.cb_join_terms_agree_item_05
    val cbAgreeItem06 = this.cb_join_terms_agree_item_06

    cbAgreeAll.setOnCheckedChangeListener { _, state ->
        if (state) {
            cbAgreeItem01.isChecked = true
            cbAgreeItem02.isChecked = true
            cbAgreeItem03.isChecked = true
            cbAgreeItem04.isChecked = true
            cbAgreeItem05.isChecked = true
            cbAgreeItem06.isChecked = true
        }
        else {
            if (checkItemLastOne(cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05) > 4) {
                cbAgreeItem01.isChecked = false
                cbAgreeItem02.isChecked = false
                cbAgreeItem03.isChecked = false
                cbAgreeItem04.isChecked = false
                cbAgreeItem05.isChecked = false
                cbAgreeItem06.isChecked = false
            }
        }
    }

    cbAgreeItem01.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem02.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem03.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem04.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem05.setOnCheckedChangeListener { _, state ->
        cbAgreeItem06.isChecked = state
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }
}

fun checkItemState(
        cbAll: AppCompatCheckBox,
        cb01: AppCompatCheckBox,
        cb02: AppCompatCheckBox,
        cb03: AppCompatCheckBox,
        cb04: AppCompatCheckBox,
        cb05: AppCompatCheckBox,
        state: Boolean) {
    if (state) {
        if (checkItemLastOne(cb01, cb02, cb03, cb04, cb05) > 4) cbAll.isChecked = true
    }
    else {
        if (checkItemLastOne(cb01, cb02, cb03, cb04, cb05) > 3) cbAll.isChecked = false
    }
}

fun checkItemLastOne(
        cb01: AppCompatCheckBox,
        cb02: AppCompatCheckBox,
        cb03: AppCompatCheckBox,
        cb04: AppCompatCheckBox,
        cb05: AppCompatCheckBox
    ): Int {

    var checkCount = 0
    if (cb01.isChecked) checkCount = checkCount.plus(1)
    if (cb02.isChecked) checkCount = checkCount.plus(1)
    if (cb03.isChecked) checkCount = checkCount.plus(1)
    if (cb04.isChecked) checkCount = checkCount.plus(1)
    if (cb05.isChecked) checkCount = checkCount.plus(1)

    return checkCount
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckClickEvent")
fun ConstraintLayout.setCheckClickEvent(viewModel: MemberViewModel) {
    val btnReqVerify = this.btn_join_phone_request_verify
    val btnNext = this.btn_join_phone_next

    val etInputPhoneNumber = this.et_join_phone_input
    val etInputVerifyNumber = this.et_join_phone_input_verify_number

    val tvInputVerifyCountDown = this.tv_join_phone_input_verify_number_countdown
    val tvInputErrorMsg = this.tv_join_phone_input_error_msg

    btnReqVerify.setOnClickListener {
        val phoneNumberCnt = etInputPhoneNumber.length()
        var phoneNumberBg = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)

        when {
            phoneNumberCnt < 1 -> {
                tvInputErrorMsg.text = context.getString(R.string.join_phone_error_01)
                tvInputErrorMsg.visible()
                phoneNumberBg = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
            }
            phoneNumberCnt < 10 -> {
                tvInputErrorMsg.text = context.getString(R.string.join_phone_error_02)
                tvInputErrorMsg.visible()
                phoneNumberBg = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
            }
            else -> {
                btnReqVerify.isSelected = false
                btnReqVerify.text = context.getString(R.string.join_phone_request_verify_retry)

                viewModel.showDialogToastSend()
                etInputVerifyNumber.visible()
                tvInputVerifyCountDown.visible()
                viewModel.countDownTimerStart()

                tvInputErrorMsg.gone()

                etInputVerifyNumber.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
        }
        etInputPhoneNumber.background = phoneNumberBg
    }

    btnNext.setOnClickListener {
        if ("00:00" == tvInputVerifyCountDown.text) {
            tvInputErrorMsg.text = context.getString(R.string.join_phone_error_04)
            tvInputErrorMsg.visible()

            etInputVerifyNumber.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
        }
        else {
            btnNext.isSelected = false
            tvInputErrorMsg.gone()

            etInputVerifyNumber.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)

            tvInputVerifyCountDown.gone()
            viewModel.countDownTimerStop()
            viewModel.navigateToJoinEmail()
        }
    }

    etInputPhoneNumber.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s?.length?: 0
            btnReqVerify.isSelected = cnt > 9
            etInputPhoneNumber.isSelected = cnt != 0
        }
    })

    etInputVerifyNumber.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s?.length?: 0
            btnNext.isSelected = cnt > 5
            etInputVerifyNumber.isSelected = cnt != 0
        }
    })
}

@BindingAdapter("setTitleIndex", "isShowRightBtn")
fun ConstraintLayout.setTitleIndex(index: Int, isShowRightBtn: Boolean = false) {
    val btnRight = this.btn_title_close

    val llIndex01 = this.ll_index_01
    val llIndex02 = this.ll_index_02
    val llIndex03 = this.ll_index_03
    val llIndex04 = this.ll_index_04
    val llIndex05 = this.ll_index_05

    if (isShowRightBtn) btnRight.visible()
    else btnRight.gone()

    llIndex01.gone()
    llIndex02.gone()
    llIndex03.gone()
    llIndex04.gone()
    llIndex05.gone()

    when (index) {
        0 -> llIndex01.visible()
        1 -> llIndex02.visible()
        2 -> llIndex03.visible()
        3 -> llIndex04.visible()
        4 -> llIndex05.visible()
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckEmail")
fun ConstraintLayout.setCheckEmail(viewModel: MemberViewModel) {
    val btnNext = this.btn_join_email_next

    val etEmailInput = this.et_join_email_input
    val tvInputErrorMsg = this.tv_join_email_input_error_msg

    btnNext.setOnClickListener {
        if(etEmailInput.length() < 1) {
            tvInputErrorMsg.visible()
            tvInputErrorMsg.text = context.getString(R.string.join_email_input_error)
            etEmailInput.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
        }
        else {
            tvInputErrorMsg.gone()
            etEmailInput.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
        }
    }

    etEmailInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s?.length?: 0
            btnNext.isSelected = cnt != 0
            etEmailInput.isSelected = cnt != 0
        }
    })
}
