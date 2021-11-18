package handshug.jellycrew.member

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import handshug.jellycrew.R
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.visible
import kotlinx.android.synthetic.main.activity_join_email.view.*
import kotlinx.android.synthetic.main.activity_join_password.view.*
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
    val etInputVerifyCode = this.et_join_phone_input_verify_code

    val tvInputVerifyCountDown = this.tv_join_phone_input_verify_number_countdown
    val tvInputErrorMsg = this.tv_join_phone_input_error_msg

    btnReqVerify.setOnClickListener {
        etInputPhoneNumber.isSelected = false
        btnReqVerify.isSelected = false
        btnReqVerify.text = context.getString(R.string.join_phone_request_verify_retry)

        viewModel.showDialogToastSend()
        etInputVerifyCode.visible()
        tvInputVerifyCountDown.visible()
        viewModel.countDownTimerStart()
    }

    btnNext.setOnClickListener {
        tvInputVerifyCountDown.gone()
        viewModel.countDownTimerStop()
        viewModel.navigateToJoinEmail()
    }

    etInputPhoneNumber.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length

            etInputPhoneNumber.isSelected = cnt != 0
            etInputPhoneNumber.setSelection(cnt)

            var bg = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
            if (cnt < 1) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_01))
            }
            else if (cnt < 10 || !viewModel.verifyPhoneNumber(s.toString())) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_02))
            }
            else {
                btnReqVerify.isSelected = true
                btnReqVerify.isEnabled = true

                tvInputErrorMsg.gone()
                bg = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
            etInputPhoneNumber.background = bg
        }
    })

    etInputVerifyCode.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length

            etInputVerifyCode.isSelected = cnt != 0
            etInputVerifyCode.setSelection(cnt)

            var bg = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
            if (cnt < 1) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_03))
            }
            else if (!viewModel.verifyAuthCode(s.toString())) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_04))
            }
            else if ("00:00" == tvInputVerifyCountDown.text) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_05))
            }
            else {
                btnNext.isSelected = true
                btnNext.isEnabled = true

                tvInputErrorMsg.gone()
                bg = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
            etInputVerifyCode.background = bg
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
        viewModel.navigateToJoinPassword()
    }

    etEmailInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length
            etEmailInput.isSelected = cnt != 0
            etEmailInput.setSelection(cnt)
            etEmailInput.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)

            if (cnt < 1) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_email_input_empty))
            }
            else if (!viewModel.verifyEmail(s.toString())) {
                setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_email_input_error))
            }
            else {
                btnNext.isSelected = true
                btnNext.isEnabled = true

                tvInputErrorMsg.gone()
                etEmailInput.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
        }
    })
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckPassword")
fun ConstraintLayout.setCheckPassword(viewModel: MemberViewModel) {
    val btnNext = this.btn_join_password_next
    val btnPasswordView01 = this.ibtn_join_password_input_view
    val btnPasswordView02 = this.ibtn_join_password_input_view_same_check

    val etInputPassword01 = this.et_join_password_input
    val etInputPassword02 = this.et_join_password_input_same_check

    val llRuleLayout = this.ll_join_password_input_rule
    val tvRule01 = llRuleLayout.tv_join_password_rule_01
    val tvRule02 = llRuleLayout.tv_join_password_rule_02
    val tvRule03 = llRuleLayout.tv_join_password_rule_03
    val tvSameCheckHint = this.tv_join_password_input_same_check_hint

    btnPasswordView01.setOnClickListener {
        it.isSelected = !it.isSelected

        if (it.isSelected) etInputPassword01.transformationMethod = HideReturnsTransformationMethod.getInstance()
        else etInputPassword01.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    btnPasswordView02.setOnClickListener {
        it.isSelected = !it.isSelected

        if (it.isSelected) etInputPassword02.transformationMethod = HideReturnsTransformationMethod.getInstance()
        else etInputPassword02.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    btnNext.setOnClickListener {
        viewModel.navigateToJoinUserInfo()
    }

    etInputPassword01.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            val cnt = text.length

            if (cnt < 1) {
                btnPasswordView01.gone()

                val textColor = context.getColor(R.color.color_common_text_gray500)
                tvRule01.setTextColor(textColor)
                tvRule02.setTextColor(textColor)
                tvRule03.setTextColor(textColor)

                etInputPassword01.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
            else {
                btnPasswordView01.visible()

                val textColorRed = context.getColor(R.color.color_common_red500)
                val textColorGreen = context.getColor(R.color.color_common_green400)
                tvRule01.apply {
                    val isVerify = viewModel.verifyPasswordAlphabet(text)
                    setTextColor(if(isVerify) textColorGreen else textColorRed)
                    this.isSelected = isVerify
                }
                tvRule02.apply {
                    val isVerify = (viewModel.verifyPasswordNumber(text) || viewModel.verifyPasswordSpecialCharacters(text))
                    setTextColor(if(isVerify) textColorGreen else textColorRed)
                    this.isSelected = isVerify
                }
                tvRule03.apply {
                    val isVerify = cnt > 5
                    setTextColor(if(isVerify) textColorGreen else textColorRed)
                    this.isSelected = isVerify
                }

                if (tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected) {
                    etInputPassword01.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
                }
                else {
                    etInputPassword01.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
                }
            }

            etInputPassword01.isSelected = cnt != 0
            etInputPassword01.setSelection(cnt)

            btnNext.isSelected = false
            btnNext.isEnabled = false

            etInputPassword02.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)
            val password02Text = etInputPassword02.text.toString()
            if (password02Text.isNotEmpty() && text != password02Text) {
                tvSameCheckHint.text = context.getString(R.string.join_password_same_check_error)
                tvSameCheckHint.setTextColor(context.getColor(R.color.color_common_red500))
                etInputPassword02.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
            }
            else if (text.isNotEmpty() && text == password02Text) {
                val isVerify = tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected
                btnNext.isSelected = isVerify
                btnNext.isEnabled = isVerify

                tvSameCheckHint.text = context.getString(R.string.join_password_same_check)
                tvSameCheckHint.setTextColor(context.getColor(R.color.color_common_green400))
            }
        }
    })

    etInputPassword02.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length
            etInputPassword02.isSelected = cnt != 0

            btnNext.isSelected = false
            btnNext.isEnabled = false

            tvSameCheckHint.setTextColor(context.getColor(R.color.color_common_text_gray500))
            tvSameCheckHint.text = context.getString(R.string.join_password_same_check)

            btnPasswordView02.visible()
            etInputPassword02.setSelection(cnt)
            etInputPassword02.background = context.getDrawable(R.drawable.selector_btn_radius08_gray400_n_gray700)

            when {
                cnt < 1 -> {
                    btnPasswordView02.gone()
                }
                etInputPassword01.text.toString() != s.toString() -> {
                    tvSameCheckHint.setTextColor(context.getColor(R.color.color_common_red500))
                    tvSameCheckHint.text = context.getString(R.string.join_password_same_check_error)
                    etInputPassword02.background = context.getDrawable(R.drawable.common_box_radius08_white_border_red500)
                }
                else -> {
                    val isVerify = tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected
                    btnNext.isSelected = isVerify
                    btnNext.isEnabled = isVerify

                    tvSameCheckHint.setTextColor(context.getColor(R.color.color_common_green400))
                }
            }
        }
    })
}

@BindingAdapter("setCheckUserInfo")
fun ConstraintLayout.setCheckUserInfo(viewModel: MemberViewModel) {

}

fun setErrorMsg(view: AppCompatTextView, msg: String) {
    view.text = msg
    view.visible()
}
