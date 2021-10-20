package handshug.jellycrew.utils.recyclerViewDecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import handshug.jellycrew.Preference.context
import handshug.jellycrew.utils.dpToPx


class RecyclerViewDecoration (
    private val topDP: Int,
    private val bottomDP: Int,
    private val leftDP: Int,
    private val rightDP: Int
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = dpToPx(context, topDP.toFloat())
        outRect.bottom = dpToPx(context, bottomDP.toFloat())
        outRect.left = dpToPx(context, leftDP.toFloat())
        outRect.right = dpToPx(context, rightDP.toFloat())
    }
}