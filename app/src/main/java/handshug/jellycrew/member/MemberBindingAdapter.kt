package handshug.jellycrew.member

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import handshug.jellycrew.R
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.Log
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.visible
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

@BindingAdapter("setCheckClickEvent")
fun ConstraintLayout.setCheckClickEvent(viewModel: MemberViewModel) {
    val btnReqVerify = this.btn_join_phone_request_verify

    val tlInputLayout = this.tl_join_phone_input_layout
    val etInputVerify = tlInputLayout.et_join_phone_input

    val tlInputVerifyLayout = this.tl_join_phone_input_verify_layout
    val etInputVerifyNumber = tlInputVerifyLayout.et_join_phone_input_verify_number

    val tvInputVerifyCountDown = this.tv_join_phone_input_verify_number_countdown

    btnReqVerify.setOnClickListener {
        btnReqVerify.text = context.getString(R.string.join_phone_request_verify_retry)

        viewModel.showDialogToast()
        tlInputVerifyLayout.visible()
        tvInputVerifyCountDown.visible()
        viewModel.getCountDownTimer()
    }
}

@BindingAdapter("setTitleIndex")
fun ConstraintLayout.setTitleIndex(index: Int) {
    val llIndex01 = this.ll_index_01
    val llIndex02 = this.ll_index_02
    val llIndex03 = this.ll_index_03
    val llIndex04 = this.ll_index_04
    val llIndex05 = this.ll_index_05

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
