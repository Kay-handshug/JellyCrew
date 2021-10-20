package handshug.jellycrew.main.view.dialog

import android.app.Activity
import android.os.Parcel
import android.os.Parcelable
import handshug.jellycrew.base.BaseDialog
import handshug.jellycrew.main.viewModel.MainViewModel

class MainDialog(
    private val activity: Activity,
    private val mainViewModel: MainViewModel
) : BaseDialog(), Parcelable {
    constructor(parcel: Parcel) : this(
        TODO("activity"),
        TODO("mainViewModel")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainDialog> {
        override fun createFromParcel(parcel: Parcel): MainDialog {
            return MainDialog(parcel)
        }

        override fun newArray(size: Int): Array<MainDialog?> {
            return arrayOfNulls(size)
        }
    }

}