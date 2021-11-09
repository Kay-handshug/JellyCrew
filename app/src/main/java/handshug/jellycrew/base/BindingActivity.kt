package handshug.jellycrew.base

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.provider.Settings
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import handshug.jellycrew.R
import handshug.jellycrew.utils.*


abstract class BindingActivity<T : ViewDataBinding> : AppCompatActivity() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    lateinit var binding: T
        private set

    var viewModel: BaseViewModel? = null

    private var progressBar: LinearLayout? = null

    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding = DataBindingUtil.setContentView(this, getLayoutResId())
        progressBar = findViewById(R.id.progress_bar)

        ActivityUtil.addActivity(this)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    }

    fun hideKeyboard(view: View, editText: EditText? = null) {
        val token = view.windowToken
        inputMethodManager.hideSoftInputFromWindow(token, 0)

        editText?.let {
            editText.clearFocus()
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val token = view.windowToken

            inputMethodManager.hideSoftInputFromWindow(token, 0)
            view.clearFocus()
        }
    }

    fun <T> startActivity(cls: Class<T>) {
        Intent(this, cls).apply {
            startActivity(this)
        }
    }

    fun <T> startActivityWithBoolean(cls: Class<T>, key: String, value: Boolean) {
        Intent(this, cls).apply {
            putExtra(key, value)
            startActivity(this)
        }
    }

    fun <T> startActivityWithId(cls: Class<T>, id: Long) {
        Intent(this, cls).apply {
            putExtra("id", id)
            startActivity(this)
        }
    }

    fun <T> startActivityWithItem(cls: Class<T>, item: Parcelable) {
        Intent(this, cls).apply {
            putExtra("item", item)
            startActivity(this)
        }
    }

    fun getReceivedItem(): Any? {
        return intent.extras?.get("item")
    }

    fun getReceivedId(): Long {
        return intent.getLongExtra("id", -1L)
    }

    fun getReceivedData(): Bundle? {
        return intent.extras
    }

    fun toast(msg: String) {
        if (!TextUtils.isEmpty(msg)) Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun toast(id: Int) {
        if (id != -1) Toast.makeText(this, getString(id), Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar() {
        progressBar?.visible()
        hideKeyboard()
    }

    fun hideProgressBar() {
        progressBar?.gone()
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.frame_layout, fragment).commit()
    }

    override fun onDestroy() {
        ActivityUtil.removeActivity(this)
        super.onDestroy()
    }

    // 런타임 퍼미션 관리
    companion object {
        const val RUNTIME_CODE_CAMERA = 10
        const val RUNTIME_CODE_CALL_PHONE = 11
        const val RUNTIME_CODE_LOCATION = 12

        const val RUNTIME_EVENT_CAMERA = 1000
        const val RUNTIME_EVENT_CALL_PHONE = 1001
        const val RUNTIME_EVENT_LOCATION = 1002
        const val RUNTIME_EVENT_NONE = 1010

        const val RUNTIME_PERMISSION_CAMERA = Manifest.permission.CAMERA
        const val RUNTIME_PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE
        const val RUNTIME_PERMISSION_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    }

    private val _runtimePermissionEvent = MutableLiveData<Event<Any>>()
    val runtimePermissionEvent: LiveData<Event<Any>>
        get() = _runtimePermissionEvent

    fun requestPermission(permission: String, requestCode: Int) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RUNTIME_CODE_CAMERA -> {
                if (checkPermission(RUNTIME_PERMISSION_CAMERA)) {
                    setRuntimeEvent(RUNTIME_EVENT_CAMERA)
                } else {
                    setRuntimeEvent(RUNTIME_EVENT_NONE)
                }
            }
            RUNTIME_CODE_CALL_PHONE -> {
                if (checkPermission(RUNTIME_PERMISSION_CALL_PHONE)) {
                    setRuntimeEvent(RUNTIME_EVENT_CALL_PHONE)
                } else {
                    setRuntimeEvent(RUNTIME_EVENT_NONE)
                }
            }
            RUNTIME_CODE_LOCATION -> {
                if (checkPermission(RUNTIME_PERMISSION_LOCATION)) {
                    setRuntimeEvent(RUNTIME_EVENT_LOCATION)
                } else {
                    setRuntimeEvent(RUNTIME_EVENT_NONE)
                }
            }
        }
    }

    private fun setRuntimeEvent(event: Int) {
        _runtimePermissionEvent.value = Event(event)
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this, permission) == PERMISSION_GRANTED
    }

    fun verifyPermission(permission: String, requestCode: Int): Boolean {
        return if (!checkPermission(permission)) {
            requestPermission(permission, requestCode)
            false
        } else true
    }

    fun requestPermissions(context: Context, callback: (Boolean) -> Unit) {
        val permissions = arrayListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
        )

        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(object: MultiplePermissionsListener {
                override fun onPermissionsChecked(permission: MultiplePermissionsReport?) {
                    if (permission != null) {
                        if (permission.areAllPermissionsGranted()) {
                            Log.msg("# permission : areAllPermissionsGranted")
                            callback(true)
                        } else {
                            Log.msg("# permission : areAllPermissionsDenied")
                            callback(false)
                        }
                    }
                    else {
                        Log.msg("# permission : unchecked")
                        callback(false)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    Toast.makeText(
                        context,
                        "# permission : onPermissionRationaleShouldBeShown :: ${permission?.size}",
                        Toast.LENGTH_SHORT
                    ).show()
                    callback(false)
                }

            })
            .check()
    }

    fun showPermissionsSettingDialog(context: Context) {
        val dialog = AlertDialog.Builder(context)
        dialog.setTitle(getString(R.string.popup_permission_setting_title))
        dialog.setMessage(getString(R.string.popup_permission_setting_message))
        dialog.setPositiveButton(getString(R.string.btn_ok)) { dialog, _ ->
            showSettingOS()
            dialog.dismiss()
        }
        dialog.setNegativeButton(getString(R.string.btn_cancel)) { dialog, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showSettingOS() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, 111)
    }
}

