package handshug.jellycrew

import android.graphics.Bitmap
import android.graphics.Paint
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import handshug.jellycrew.utils.ImageUtil
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.recyclerViewDecoration.RecyclerViewDecoration
import handshug.jellycrew.utils.visible

//@BindingAdapter("strikeThrough")
//fun TextView.strikeThrough(strikeThrough: Boolean) {
//    if (strikeThrough) {
//        this.paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//    } else {
//        this.paintFlags = this.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//    }
//}
//
//@BindingAdapter("underline")
//fun TextView.underline(underline: Boolean) {
//    if (underline) {
//        this.paintFlags = this.paintFlags or Paint.UNDERLINE_TEXT_FLAG
//    } else {
//        this.paintFlags = this.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
//    }
//}
//
//@BindingAdapter("setImage")
//fun ImageView.setImage(url: String?) {
//    url?.let {
//        ImageUtil.setImage(this, url)
//    }
//}
//
//@BindingAdapter("setImageForReview")
//fun ImageView.setImageForReview(url: String?) {
//    url?.let {
//        ImageUtil.setImageForReview(this, url)
//    }
//}
//
//@BindingAdapter(value = ["setImageWithScale", "width", "height"])
//fun ImageView.setImageWithScale(url: String?, width: Int, height: Int) {
//    url?.let {
//        ImageUtil.setImageWithScale(this, url, width, height)
//    }
//}
//
//@BindingAdapter("setImage")
//fun ImageView.setImage(bitmap: Bitmap?) {
//    bitmap?.let {
//        this.setImageBitmap(it)
//    }
//}
//
//@BindingAdapter("setImageCircle")
//fun ImageView.setImageCircle(url: String?) {
//    url?.let {
//        ImageUtil.setImageCircle(this, url)
//    }
//}
//
//@BindingAdapter("setImageOriginal")
//fun ImageView.setImageOriginal(url: String?) {
//    url?.let {
//        ImageUtil.setImageOriginal(this, url)
//    }
//}
//
//@BindingAdapter("reviewZeroVisibility")
//fun View.reviewZeroVisibility(reviewCount: Int) {
//    if (reviewCount == 0) {
//        this.gone()
//    } else {
//        this.visible()
//    }
//}
//
//@BindingAdapter(value = ["checkSoldOutCount", "conditionOfSoldOutCount"])
//fun View.checkSoldOutCount(remainCount: Int, conditionOfSoldOutCount: Int) {
//    if (remainCount <= conditionOfSoldOutCount) {
//        this.visible()
//    } else {
//        this.gone()
//    }
//
//}
//
//@BindingAdapter(value = ["checkStockCount", "conditionOfShowStockCount"])
//fun View.checkStockCount(remainCount: Int, conditionOfShowStockCount: Int) {
//    if (remainCount <= conditionOfShowStockCount) {
//        if (remainCount > 0) {
//            this.visible()
//        } else {
//            this.gone()
//        }
//    } else {
//        this.gone()
//    }
//
//}
//
//@BindingAdapter("isViewShow")
//fun View.isViewShow(isShow: Boolean) {
//    if (isShow) {
//        this.visible()
//    } else {
//        this.gone()
//    }
//}
//
//@BindingAdapter("isListEmptyShow")
//fun View.isListEmptyShow(items: List<Any>?) {
//    items?.let {
//        if (it.isEmpty()) {
//            this.visible()
//        } else {
//            this.gone()
//        }
//    } ?: run {
//        this.visible()
//    }
//
//}
//
//@BindingAdapter("isListEmptyHide")
//fun View.isListEmptyHide(items: List<Any>?) {
//    items?.let {
//        if (it.isEmpty()) {
//            this.gone()
//        } else {
//            this.visible()
//        }
//    } ?: run {
//        this.gone()
//    }
//
//}
//
//@BindingAdapter("checkMemberVisible")
//fun View.checkMemberVisible(flag: Boolean) {
//    if (flag) {
//        if (Preference.isLogin) {
//            this.visible()
//        } else {
//            this.gone()
//        }
//    }
//}
//
//@BindingAdapter("checkMemberGone")
//fun View.checkMemberGone(flag: Boolean) {
//    if (flag) {
//        if (Preference.isLogin) {
//            this.gone()
//        } else {
//            this.visible()
//        }
//    }
//}
//
//@BindingAdapter("showMemberView")
//fun View.showMemberView(isMember: Boolean) {
//    if (isMember) {
//        if (Preference.isLogin) {
//            this.visible()
//        } else {
//            this.gone()
//        }
//    } else {
//        if (!Preference.isLogin) {
//            this.visible()
//        } else {
//            this.gone()
//        }
//    }
//}
//
//@BindingAdapter("blockEnter")
//fun EditText.blockEnter(flag: Boolean) {
//    if (flag) {
//        this.setOnKeyListener { _, keyCode, _ ->
//            keyCode == KeyEvent.KEYCODE_ENTER
//        }
//    }
//}
//
//@BindingAdapter("addVerticalItemDecoration")
//fun RecyclerView.addVerticalItemDecoration(dp: Int) {
//    this.addItemDecoration(RecyclerViewDecoration(0,dp,0,0))
//}
//
//@BindingAdapter("addHorizontalItemDecoration")
//fun RecyclerView.addHorizontalItemDecoration(dp: Int) {
//    this.addItemDecoration(RecyclerViewDecoration(0, 0,0,dp))
//}