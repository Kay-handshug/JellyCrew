package handshug.jellycrew.base

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog


class BindingDialog<T : ViewDataBinding>(val activity: Activity, layoutRes: Int) {

    val binding: T = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutRes, null, false)

    fun getBottomSheetDialog(fullSize: Boolean = false): BottomSheetDialog {
        val dialog = BottomSheetDialog(activity)

        if (fullSize) {
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val parentLayout = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                parentLayout?.let { view ->
                    val behaviour = BottomSheetBehavior.from(view)
                    setupFullHeight(view)
                    behaviour.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        dialog.setContentView(binding.root)
        return dialog
    }

    fun getAlertDialog(rejectBackgroundTouchDismiss: Boolean = false): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setView(binding.root)
        if (rejectBackgroundTouchDismiss) {
            dialogBuilder.setCancelable(false)
        }
        return dialogBuilder.create()
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}