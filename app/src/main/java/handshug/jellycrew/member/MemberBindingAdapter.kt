package handshug.jellycrew.member

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.appcompat.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.*
import kotlinx.android.synthetic.main.activity_join_success.view.*
import kotlinx.android.synthetic.main.activity_login_email.view.*
import kotlinx.android.synthetic.main.activity_member_title.view.*
import kotlinx.android.synthetic.main.dialog_member_already_join.view.*
import kotlinx.android.synthetic.main.dialog_member_for_cafe24.view.*
import kotlinx.android.synthetic.main.fragment_join_email.view.*
import kotlinx.android.synthetic.main.fragment_join_password.view.*
import kotlinx.android.synthetic.main.fragment_join_phone.view.*
import kotlinx.android.synthetic.main.fragment_join_terms.view.*
import kotlinx.android.synthetic.main.fragment_join_user_info.view.*


@BindingAdapter("setCheckLogin")
fun ConstraintLayout.setCheckLogin(viewModel: MemberViewModel) {
    val btnEmailDelete = this.iv_login_email_input_delete
    val btnPasswordDelete = this.iv_login_password_input_delete
    val btnPasswordView = this.ibtn_login_password_input_view
    val btnLogin = this.btn_login

    val etEmailInput = this.et_login_email_input
    val etPasswordInput = this.et_login_password_input

    val tvEmailErrorMsg = this.tv_login_email_input_error_msg

    val llPasswordRule = this.ll_login_password_input_rule
    val tvRule01 = llPasswordRule.tv_login_password_rule_01
    val tvRule02 = llPasswordRule.tv_login_password_rule_02
    val tvRule03 = llPasswordRule.tv_login_password_rule_03

    this.setOnClickListener {
        viewModel.hideKeyboard(it)
    }

    btnEmailDelete.setOnClickListener {
        etEmailInput.text?.clear()
    }

    btnPasswordDelete.setOnClickListener {
        etPasswordInput.text?.clear()
    }

    btnPasswordView.setOnClickListener {
        it.isSelected = !it.isSelected

        if (it.isSelected) etPasswordInput.transformationMethod = HideReturnsTransformationMethod.getInstance()
        else etPasswordInput.transformationMethod = PasswordTransformationMethod.getInstance()
    }

    btnLogin.setOnClickListener {
        viewModel.memberLogin()
    }

    etEmailInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length

            btnLogin.isSelected = false
            btnLogin.isEnabled = false

            ViewUtil.setBackgroundDrawable(etEmailInput, R.drawable.common_box_radius08_white_border_red500)
            if (cnt < 1) {
                setErrorMsg(tvEmailErrorMsg, context.getString(R.string.join_email_input_empty))
            }
            else if (!viewModel.verifyEmail(s.toString())) {
                setErrorMsg(tvEmailErrorMsg, context.getString(R.string.join_email_input_error))
            }
            else {
                if (tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected) {
                    btnLogin.isSelected = true
                    btnLogin.isEnabled = true
                }
                tvEmailErrorMsg.gone()
                ViewUtil.setBackgroundDrawable(etEmailInput, R.drawable.selector_btn_radius08_gray400_n_gray700)
            }

            if (cnt == 0) {
                btnEmailDelete.invisible()
                etEmailInput.isSelected = false
            }
            else {
                btnEmailDelete.visible()
                etEmailInput.isSelected = true
            }

            etEmailInput.setSelection(cnt)
        }
    })

    etPasswordInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val text = s.toString()
            val cnt = text.length

            btnLogin.isSelected = false
            btnLogin.isEnabled = false

            if (cnt < 1) {
                btnPasswordView.gone()

                ViewUtil.setTextColor(tvRule01, R.color.color_common_text_gray500)
                ViewUtil.setTextColor(tvRule02, R.color.color_common_text_gray500)
                ViewUtil.setTextColor(tvRule03, R.color.color_common_text_gray500)
                ViewUtil.setBackgroundDrawable(etPasswordInput, R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
            else {
                btnPasswordView.visible()

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
                    val emailText = etEmailInput.text.toString()
                    if (emailText.length > 1 && viewModel.verifyEmail(emailText)) {
                        btnLogin.isSelected = true
                        btnLogin.isEnabled = true
                    }
                    ViewUtil.setBackgroundDrawable(etPasswordInput, R.drawable.selector_btn_radius08_gray400_n_gray700)
                }
                else {
                    ViewUtil.setBackgroundDrawable(etPasswordInput, R.drawable.common_box_radius08_white_border_red500)
                }
            }

            if (cnt == 0) {
                btnPasswordDelete.invisible()
                etPasswordInput.isSelected = false
            }
            else {
                btnPasswordDelete.visible()
                etPasswordInput.isSelected = true
            }
            etPasswordInput.setSelection(cnt)
        }
    })
}

@BindingAdapter("setTitleIndex", "setIndex", "isShowRightBtn", "setTitle")
fun ConstraintLayout.setTitleIndex(viewModel: MemberViewModel, index: Int, isShowRightBtn: Boolean = false, title: String) {
    val btnRight = this.btn_title_close
    val btnLeft = this.btn_title_back

    val llIndex01 = this.ll_index_01
    val llIndex02 = this.ll_index_02
    val llIndex03 = this.ll_index_03
    val llIndex04 = this.ll_index_04
    val llIndex05 = this.ll_index_05

    val tvTitle = this.tv_title
    tvTitle.text = if(title.isNotEmpty()) title else context.getString(R.string.join_title)

    if (isShowRightBtn) btnRight.visible()
    else btnRight.gone()

    btnLeft.setOnClickListener {
        if(index < 0) viewModel.navigateToLoginHome()
        else viewModel.showDialogFinish()
    }

    llIndex01.gone()
    llIndex02.gone()
    llIndex03.gone()
    llIndex04.gone()
    llIndex05.gone()

    if (Preference.loginType == 0) {
        when (index) {
            0 -> llIndex01.visible()
            1 -> llIndex02.visible()
            2 -> llIndex03.visible()
            3 -> llIndex04.visible()
            4 -> llIndex05.visible()
        }
    }
    else {
        when (index) {
            0 -> llIndex01.visible()
            1 -> llIndex03.visible()
            2 -> llIndex05.visible()
        }
    }
}

@BindingAdapter("setCheckTerms")
fun ConstraintLayout.setCheckTerms(viewModel: MemberViewModel) {
    val cbAgreeAll = this.cb_join_terms_agree_all
    val cbAgreeItem01 = this.cb_join_terms_agree_item_01
    val cbAgreeItem02 = this.cb_join_terms_agree_item_02
    val cbAgreeItem03 = this.cb_join_terms_agree_item_03
    val cbAgreeItem04 = this.cb_join_terms_agree_item_04
    val cbAgreeItem05 = this.cb_join_terms_agree_item_05
    val cbAgreeItem06 = this.cb_join_terms_agree_item_06

    val btnNext = this.btn_join_terms_agree_confirm

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
        checkItemEssential(cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, btnNext)
    }

    cbAgreeItem02.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
        checkItemEssential(cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, btnNext)
    }

    cbAgreeItem03.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
        checkItemEssential(cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, btnNext)
    }

    cbAgreeItem04.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem05.setOnCheckedChangeListener { _, state ->
        cbAgreeItem06.isChecked = state
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, cbAgreeItem05, state)
    }

    cbAgreeItem06.setOnCheckedChangeListener { _, _ ->
        if (!cbAgreeItem05.isChecked) cbAgreeItem05.isChecked = true
    }

    btnNext.setOnClickListener {
        Preference.isMarketingAgree = cbAgreeItem04.isChecked
        Preference.isLifeTimeMember = cbAgreeItem06.isChecked

        viewModel.navigateToJoinPhone()
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

fun checkItemEssential(
        cb01: AppCompatCheckBox,
        cb02: AppCompatCheckBox,
        cb03: AppCompatCheckBox,
        btnNext: AppCompatButton) {
    val isVerify = cb01.isChecked && cb02.isChecked && cb03.isChecked
    btnNext.isSelected = isVerify
    btnNext.isEnabled = isVerify
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckPhoneVerify")
fun ConstraintLayout.setCheckPhoneVerify(viewModel: MemberViewModel) {
    val btnNameInputDelete = this.iv_join_phone_name_input_delete
    val btnPhoneInputDelete = this.iv_join_phone_input_delete
    val btnReqVerify = this.btn_join_phone_request_verify
    val btnNext = this.btn_join_phone_next

    val etName = this.et_join_phone_name_input
    val etInputPhoneNumber = this.et_join_phone_input
    val etInputVerifyCode = this.et_join_phone_input_verify_code

    val llNameRule = this.ll_join_phone_name_input_rule
    val tvRule01 = llNameRule.tv_join_phone_name_rule_01
    val tvRule02 = llNameRule.tv_join_phone_name_rule_02

    val tvInputVerifyCountDown = this.tv_join_phone_input_verify_number_countdown
    val tvInputErrorMsg = this.tv_join_phone_input_error_msg

    if (Preference.loginType != 0) {
        if (Preference.userSocialName.isNotEmpty()) {
            etName.setText(Preference.userSocialName)
            setEditBoxNameVerify(context, viewModel, etName.text.toString(), btnNext, btnReqVerify, tvRule01, tvRule02, etInputPhoneNumber, etName, btnNameInputDelete)
        }
        if (Preference.userSocialPhoneNumber.isNotEmpty()) {
            etInputPhoneNumber.setText(Preference.userSocialPhoneNumber)
            setEditBoxPhoneNunmber(context, viewModel, etInputPhoneNumber.text.toString(), btnReqVerify, tvRule01, tvRule02, etInputPhoneNumber, etName, btnPhoneInputDelete, tvInputErrorMsg)
        }
    }

    this.setOnClickListener {
        viewModel.hideKeyboard(it)
    }

    btnNameInputDelete.setOnClickListener {
        etName.text?.clear()
    }

    btnPhoneInputDelete.setOnClickListener {
        etInputPhoneNumber.text?.clear()
    }

    btnReqVerify.setOnClickListener {
        etInputPhoneNumber.isSelected = false
        btnReqVerify.isSelected = false
        btnReqVerify.text = context.getString(R.string.join_phone_request_verify_retry)

        viewModel.showDialogToastSend()
        etInputVerifyCode.text?.clear()
        etInputVerifyCode.visible()
        tvInputVerifyCountDown.visible()
        tvInputErrorMsg.gone()
        viewModel.countDownTimerStart()
    }

    btnNext.setOnClickListener {
        viewModel.reqPhoneVerifyConfirm()
    }

    etName.setOnFocusChangeListener { view, state ->
        loseFocusRecheck(view, state)
    }

    etInputPhoneNumber.setOnFocusChangeListener { view, state ->
        loseFocusRecheck(view, state)
    }

    etName.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setEditBoxNameVerify(context, viewModel, s.toString(), btnNext, btnReqVerify, tvRule01, tvRule02, etInputPhoneNumber, etName, btnNameInputDelete)
        }
    })

    etInputPhoneNumber.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setEditBoxPhoneNunmber(context, viewModel, s.toString(), btnReqVerify, tvRule01, tvRule02, etInputPhoneNumber, etName, btnPhoneInputDelete, tvInputErrorMsg)
        }
    })

    etInputVerifyCode.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length

            etInputVerifyCode.isSelected = cnt != 0
            etInputVerifyCode.setSelection(cnt)

            ViewUtil.setBackgroundDrawable(etInputVerifyCode, R.drawable.common_box_radius08_white_border_red500)
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
                ViewUtil.setBackgroundDrawable(etInputVerifyCode, R.drawable.selector_btn_radius08_gray400_n_gray700)
            }
        }
    })
}

private fun setEditBoxNameVerify(
        context: Context, viewModel: MemberViewModel, text: String,
        btnNext: AppCompatButton, btnReqVerify: AppCompatButton,
        tvRule01: AppCompatTextView, tvRule02: AppCompatTextView,
        etInputPhoneNumber: AppCompatEditText, etName: AppCompatEditText,
        btnNameInputDelete: AppCompatImageView
) {
    val cnt = text.length

    btnNext.isSelected = false
    btnNext.isEnabled = false

    btnReqVerify.isSelected = false
    btnReqVerify.isEnabled = false

    val textColorRed = context.getColor(R.color.color_common_red500)
    if (cnt < 1) {
        tvRule01.setTextColor(textColorRed)
        tvRule02.setTextColor(textColorRed)

        ViewUtil.setBackgroundDrawable(etName, R.drawable.common_box_radius08_white_border_red500)
    }
    else {
        val textColorGreen = context.getColor(R.color.color_common_green400)
        tvRule01.apply {
            val isVerify = viewModel.verifyName(text)
            setTextColor(if(isVerify) textColorGreen else textColorRed)
            this.isSelected = isVerify
        }
        tvRule02.apply {
            val isVerify = cnt > 1
            setTextColor(if(isVerify) textColorGreen else textColorRed)
            this.isSelected = isVerify
        }

        if (tvRule01.isSelected && tvRule02.isSelected) {
            if(etInputPhoneNumber.length() > 9 || viewModel.verifyPhoneNumber(etInputPhoneNumber.text.toString())) {
                btnReqVerify.isSelected = true
                btnReqVerify.isEnabled = true
            }
            ViewUtil.setBackgroundDrawable(etName, R.drawable.selector_btn_radius08_gray400_n_gray700)
        }
        else {
            ViewUtil.setBackgroundDrawable(etName, R.drawable.common_box_radius08_white_border_red500)
        }
    }

    if (cnt == 0) {
        btnNameInputDelete.invisible()
        etName.isSelected = false
    }
    else {
        btnNameInputDelete.visible()
        etName.isSelected = true
    }
    etName.setSelection(cnt)
}

private fun setEditBoxPhoneNunmber(
        context: Context, viewModel: MemberViewModel, text: String,
        btnReqVerify: AppCompatButton, tvRule01: AppCompatTextView, tvRule02: AppCompatTextView,
        etInputPhoneNumber: AppCompatEditText, etName: AppCompatEditText,
        btnPhoneInputDelete: AppCompatImageView, tvInputErrorMsg: AppCompatTextView
) {
    val cnt = text.length

    if (cnt == 0) {
        btnPhoneInputDelete.invisible()
        etInputPhoneNumber.isSelected = false
    }
    else {
        btnPhoneInputDelete.visible()
        etInputPhoneNumber.isSelected = true
        loseFocusRecheck(etInputPhoneNumber, true)
    }

    ViewUtil.setBackgroundDrawable(etInputPhoneNumber, R.drawable.common_box_radius08_white_border_red500)
    if (cnt < 1) {
        setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_01))
    }
    else if (cnt < 10 || !viewModel.verifyPhoneNumber(text)) {
        setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_phone_error_02))
    }
    else {
        if (tvRule01.isSelected && tvRule02.isSelected) {
            btnReqVerify.isSelected = true
            btnReqVerify.isEnabled = true

            ViewUtil.setBackgroundDrawable(etName, R.drawable.selector_btn_radius08_gray400_n_gray700)
        }
        else {
            ViewUtil.setBackgroundDrawable(etName, R.drawable.common_box_radius08_white_border_red500)
        }
        tvInputErrorMsg.gone()
        ViewUtil.setBackgroundDrawable(etInputPhoneNumber, R.drawable.selector_btn_radius08_gray400_n_gray700)
    }
    etInputPhoneNumber.setSelection(cnt)
}

fun loseFocusRecheck(view: View, state: Boolean) {
    if (state) {
        // lose focus recheck
        Handler().postDelayed({
            view.requestFocus()
        }, 500L)
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckEmail")
fun ConstraintLayout.setCheckEmail(viewModel: MemberViewModel) {
    val btnEmailInputDelete = this.iv_join_email_input_delete
    val btnNext = this.btn_join_email_next

    val etEmailInput = this.et_join_email_input
    val tvInputErrorMsg = this.tv_join_email_input_error_msg

    if (Preference.loginType != 0 && Preference.userSocialEmail.isNotEmpty() && viewModel.verifyEmail(Preference.userSocialEmail)) {
        btnNext.isSelected = true
        btnNext.isEnabled = true

        etEmailInput.setText(Preference.userSocialEmail)
        setEditBoxEmail(context, viewModel, etEmailInput.text.toString(), btnNext, btnEmailInputDelete, etEmailInput, tvInputErrorMsg)
    }

    if (Preference.isCafe24MemberJoin && Preference.userEmail.isNotEmpty() && viewModel.verifyEmail(Preference.userEmail)) {
        btnNext.isSelected = true
        btnNext.isEnabled = true

        etEmailInput.setText(Preference.userEmail)
        setEditBoxEmail(context, viewModel, etEmailInput.text.toString(), btnNext, btnEmailInputDelete, etEmailInput, tvInputErrorMsg)
    }

    this.setOnClickListener {
        viewModel.hideKeyboard(it)
    }

    btnEmailInputDelete.setOnClickListener {
        etEmailInput.text?.clear()
    }

    btnNext.setOnClickListener {
        viewModel.navigateToJoinPassword()
    }

    etEmailInput.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            setEditBoxEmail(context, viewModel, s.toString(), btnNext, btnEmailInputDelete, etEmailInput, tvInputErrorMsg)
        }
    })
}

private fun setEditBoxEmail(
        context: Context, viewModel: MemberViewModel, text: String,
        btnNext: AppCompatButton, btnEmailInputDelete: AppCompatImageView,
        etEmailInput: AppCompatEditText, tvInputErrorMsg: AppCompatTextView
) {
    val cnt = text.length

    if (cnt == 0) {
        btnEmailInputDelete.invisible()
        etEmailInput.isSelected = false
    }
    else {
        btnEmailInputDelete.visible()
        etEmailInput.isSelected = true
    }

    ViewUtil.setBackgroundDrawable(etEmailInput, R.drawable.common_box_radius08_white_border_red500)
    if (cnt < 1) {
        setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_email_input_empty))
    }
    else if (!viewModel.verifyEmail(text)) {
        setErrorMsg(tvInputErrorMsg, context.getString(R.string.join_email_input_error))
    }
    else {
        btnNext.isSelected = true
        btnNext.isEnabled = true

        tvInputErrorMsg.gone()
        ViewUtil.setBackgroundDrawable(etEmailInput, R.drawable.selector_btn_radius08_gray400_n_gray700)
    }
    etEmailInput.setSelection(cnt)
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckPassword")
fun ConstraintLayout.setCheckPassword(viewModel: MemberViewModel) {
    val btnPasswordDelete01 = this.iv_join_password_input_delete_01
    val btnPasswordDelete02 = this.iv_join_password_input_delete_02
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

    this.setOnClickListener {
        viewModel.hideKeyboard(it)
    }

    btnPasswordDelete01.setOnClickListener {
        etInputPassword01.text?.clear()
    }

    btnPasswordDelete02.setOnClickListener {
        etInputPassword02.text?.clear()
    }

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

                ViewUtil.setTextColor(tvRule01, R.color.color_common_text_gray500)
                ViewUtil.setTextColor(tvRule02, R.color.color_common_text_gray500)
                ViewUtil.setTextColor(tvRule03, R.color.color_common_text_gray500)
                ViewUtil.setBackgroundDrawable(etInputPassword01, R.drawable.selector_btn_radius08_gray400_n_gray700)
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
                    ViewUtil.setBackgroundDrawable(etInputPassword01, R.drawable.selector_btn_radius08_gray400_n_gray700)
                }
                else {
                    ViewUtil.setBackgroundDrawable(etInputPassword01, R.drawable.common_box_radius08_white_border_red500)
                }
            }

            if (cnt == 0) {
                btnPasswordDelete01.invisible()
                etInputPassword01.isSelected = false
            }
            else {
                btnPasswordDelete01.visible()
                etInputPassword01.isSelected = true
            }
            etInputPassword01.setSelection(cnt)

            btnNext.isSelected = false
            btnNext.isEnabled = false

            ViewUtil.setBackgroundDrawable(etInputPassword02, R.drawable.selector_btn_radius08_gray400_n_gray700)
            val password02Text = etInputPassword02.text.toString()
            if (password02Text.isNotEmpty() && text != password02Text) {
                tvSameCheckHint.text = context.getString(R.string.join_password_same_check_error)
                ViewUtil.setTextColor(tvSameCheckHint, R.color.color_common_red500)
                ViewUtil.setBackgroundDrawable(etInputPassword02, R.drawable.common_box_radius08_white_border_red500)
            }
            else if (text.isNotEmpty() && text == password02Text) {
                val isVerify = tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected
                btnNext.isSelected = isVerify
                btnNext.isEnabled = isVerify

                tvSameCheckHint.text = context.getString(R.string.join_password_same_check)
                ViewUtil.setTextColor(tvSameCheckHint, R.color.color_common_green400)
            }
        }
    })

    etInputPassword02.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val cnt = s.toString().length
            if (cnt == 0) {
                btnPasswordDelete02.invisible()
                etInputPassword02.isSelected = false
            }
            else {
                btnPasswordDelete02.visible()
                etInputPassword02.isSelected = true
            }

            btnNext.isSelected = false
            btnNext.isEnabled = false

            tvSameCheckHint.text = context.getString(R.string.join_password_same_check)
            ViewUtil.setTextColor(tvSameCheckHint, R.color.color_common_text_gray500)

            btnPasswordView02.visible()
            etInputPassword02.setSelection(cnt)
            ViewUtil.setBackgroundDrawable(etInputPassword02, R.drawable.selector_btn_radius08_gray400_n_gray700)

            when {
                cnt < 1 -> {
                    btnPasswordView02.gone()
                }
                etInputPassword01.text.toString() != s.toString() -> {
                    tvSameCheckHint.text = context.getString(R.string.join_password_same_check_error)
                    ViewUtil.setTextColor(tvSameCheckHint, R.color.color_common_red500)
                    ViewUtil.setBackgroundDrawable(etInputPassword02, R.drawable.common_box_radius08_white_border_red500)
                }
                else -> {
                    val isVerify = tvRule01.isSelected && tvRule02.isSelected && tvRule03.isSelected
                    btnNext.isSelected = isVerify
                    btnNext.isEnabled = isVerify

                    ViewUtil.setTextColor(tvSameCheckHint, R.color.color_common_green400)
                }
            }
        }
    })
}

@SuppressLint("UseCompatLoadingForDrawables")
@BindingAdapter("setCheckUserInfo")
fun ConstraintLayout.setCheckUserInfo(viewModel: MemberViewModel) {
    val btnNext = this.btn_join_user_info_next

    val tvBirth = this.tv_join_user_info_birth
    val tvGender = this.tv_join_user_info_gender
    val tvBirthError = this.tv_join_user_info_birth_error_msg

    val ivBirthDropDown = this.iv_join_user_info_birth_drop_down

    if (Preference.loginType != 0) {
        checkBirthDayVerify(Preference.userSocialBirthDay, tvBirth, tvBirthError, ivBirthDropDown)
        if (Preference.userSocialGender.isNotEmpty()) {
            if (Preference.userSocialGender.contains("fe") || Preference.userSocialGender.contains("F")) viewModel.selectedGender.value = 0
            else viewModel.selectedGender.value = 1
        }
    }

    if (Preference.isCafe24MemberJoin) {
        checkBirthDayVerify(Preference.userBirth, tvBirth, tvBirthError, ivBirthDropDown)
    }


    this.setOnClickListener {
        viewModel.hideKeyboard(it)
    }

    btnNext.setOnClickListener {
        viewModel.navigateToJoinConfirm()
    }
}

fun checkBirthDayVerify(birth: String, tvBirth: AppCompatTextView, tvBirthError: AppCompatTextView, ivBirthDropDown: AppCompatImageView) {
    if (birth.isNotEmpty()) {
        tvBirth.text = birth

        if (FormatterUtil.isOver14YearsOld(birth)) {
            tvBirth.isSelected = true
            ivBirthDropDown.isSelected = true

            tvBirthError.gone()
        }
        else {
            tvBirthError.visible()
        }
    }
}

fun setErrorMsg(view: AppCompatTextView, msg: String) {
    view.text = msg
    view.visible()
}

@BindingAdapter("setJoinSuccessInfo")
fun ConstraintLayout.setJoinSuccessInfo(viewModel: MemberViewModel) {
    val llSubTitle = this.ll_join_success_sub_title_02

    val tvName = llSubTitle.tv_join_success_name
    val tvSubTitle01 = llSubTitle.tv_join_success_sub_title_02
    val tvSuvTitle02 = this.tv_join_success_sub_title_03
    val tvBenefits03Value = this.tv_join_success_benefits_03_value
    val tvBenefitsNoti = this.tv_join_success_benefits_noti

    val rlBenefits01 = this.ll_join_success_benefits_01
    val rlBenefits02 = this.ll_join_success_benefits_02
    val rlBenefits03 = this.ll_join_success_benefits_03
    val rlBenefits04 = this.ll_join_success_benefits_04
    val rlBenefits05 = this.ll_join_success_benefits_05

    val btnConfirm = this.btn_join_success_confirm

    tvName.text = Preference.userName

    rlBenefits01.gone()
    rlBenefits02.gone()
    rlBenefits03.gone()
    rlBenefits04.gone()
    rlBenefits05.gone()

    if (Preference.isLifeTimeMember) rlBenefits04.visible()

    if (Preference.isCafe24MemberJoin) {
        setString(context, tvSubTitle01, R.string.join_success_cafe24_title_01)
        setString(context, tvSuvTitle02, R.string.join_success_cafe24_title_02)
        setString(context, tvBenefitsNoti, R.string.join_success_cafe24_benefits_noti)

        rlBenefits03.visible()
        rlBenefits05.visible()

        if (Preference.userMoney > 0) {
            val money = FormatterUtil.convertCommaNumberPlusWon(Preference.userMoney)
            tvBenefits03Value.text = money
            setTextColorSpan(context, tvBenefits03Value, 0, money.length -1)
        }
    }
    else {
        setString(context, tvSubTitle01, R.string.join_success_sub_title_02)
        setString(context, tvSuvTitle02, R.string.join_success_sub_title_03)
        setString(context, tvBenefitsNoti, R.string.join_success_benefits_noti)

        rlBenefits01.visible()
        rlBenefits02.visible()
    }

    setTextColorSpan(context, tvBenefitsNoti, 15, 5)

    btnConfirm.setOnClickListener {
        viewModel.navigateToJoinSuccess()
    }
}

fun setString(context: Context, view: AppCompatTextView, id: Int) {
    view.text = context.getString(id)
}

fun setTextColorSpan(context: Context, view: AppCompatTextView, start: Int, end: Int) {
    ViewUtil.setTextColorSpannable(view, context.getColor(R.color.color_common_violet500), view.text.toString(), start, end)
}

@BindingAdapter("setTextColorSpannableStart", "setTextColorSpannableEnd")
fun AppCompatTextView.setTextColorSpannableStart(start: Int, end: Int) {
    setTextColorSpan(context, this, start, end)
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setAlreadyJoinUser", "setAlreadyDialog", "setAlreadyEmail", "setSocialsType")
fun ConstraintLayout.setAlreadyJoinUser(viewModel: MemberViewModel, dialog:BottomSheetDialog, email:String, socialsType: String) {
    val llJoinHint = this.ll_join_already_id_hint
    val llJoinSocialsHint = llJoinHint.ll_join_already_socials_hint

    val tvSocialsTitle = this.tv_join_already_socials_title
    val tvEmail = llJoinHint.tv_join_already_email_hint

    val ivKakao = llJoinSocialsHint.iv_join_already_sns_kakao
    val ivNaver = llJoinSocialsHint.iv_join_already_sns_naver
    val ivFacebook = llJoinSocialsHint.iv_join_already_sns_facebook

    val btnLogin = this.btn_join_already_login

    tvEmail.text = email

    ivKakao.isSelected = false
    ivNaver.isSelected = false
    ivFacebook.isSelected = false

    val sns = socialsType.split(",")
    var snsCnt = 0
    sns.forEach {
        when (it) {
            "KAKAO" -> {
                snsCnt++
                ivKakao.isSelected = true
            }
            "NAVER" -> {
                snsCnt++
                ivNaver.isSelected = true
            }
            "FACEBOOK" -> {
                snsCnt++
                ivFacebook.isSelected = true
            }
        }
    }

    tvSocialsTitle.text = "${context.getString(R.string.join_already_sns)} ($snsCnt)"

    btnLogin.setOnClickListener {
        dialog.dismiss()
        viewModel.navigateToLogin()
    }
}
