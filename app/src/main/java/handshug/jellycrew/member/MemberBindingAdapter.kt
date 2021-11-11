package handshug.jellycrew.member

import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import handshug.jellycrew.utils.Log
import kotlinx.android.synthetic.main.activity_join_terms.view.*


@BindingAdapter("setCheckBoxAll")
fun ConstraintLayout.setCheckBoxAll(isUsed: Boolean = false) {
    if(!isUsed) return

    val cbAgreeAll = this.cb_join_terms_agree_all
    val cbAgreeItem01 = this.cb_join_terms_agree_item_01
    val cbAgreeItem02 = this.cb_join_terms_agree_item_02
    val cbAgreeItem03 = this.cb_join_terms_agree_item_03
    val cbAgreeItem04 = this.cb_join_terms_agree_item_04

    cbAgreeAll.setOnCheckedChangeListener { _, state ->
        if (state) {
            cbAgreeItem01.isChecked = true
            cbAgreeItem02.isChecked = true
            cbAgreeItem03.isChecked = true
            cbAgreeItem04.isChecked = true
        }
        else {
            if (checkItemLastOne(cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04) > 3) {
                cbAgreeItem01.isChecked = false
                cbAgreeItem02.isChecked = false
                cbAgreeItem03.isChecked = false
                cbAgreeItem04.isChecked = false
            }
        }
    }

    cbAgreeItem01.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, state)
    }

    cbAgreeItem02.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, state)
    }

    cbAgreeItem03.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, state)
    }

    cbAgreeItem04.setOnCheckedChangeListener { _, state ->
        checkItemState(cbAgreeAll, cbAgreeItem01, cbAgreeItem02, cbAgreeItem03, cbAgreeItem04, state)
    }
}

fun checkItemState(cbAll: AppCompatCheckBox, cb01: AppCompatCheckBox, cb02: AppCompatCheckBox, cb03: AppCompatCheckBox, cb04: AppCompatCheckBox, state: Boolean) {
    if (state) {
        if (checkItemLastOne(cb01, cb02, cb03, cb04) > 3) cbAll.isChecked = true
    }
    else {
        if (checkItemLastOne(cb01, cb02, cb03, cb04) > 2) cbAll.isChecked = false
    }
}

fun checkItemLastOne(cb01: AppCompatCheckBox, cb02: AppCompatCheckBox, cb03: AppCompatCheckBox, cb04: AppCompatCheckBox): Int {
    var checkCount = 0
    if (cb01.isChecked) checkCount = checkCount.plus(1)
    if (cb02.isChecked) checkCount = checkCount.plus(1)
    if (cb03.isChecked) checkCount = checkCount.plus(1)
    if (cb04.isChecked) checkCount = checkCount.plus(1)

    return checkCount
}
