package handshug.jellycrew.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.databinding.ActivitySplashBinding
import handshug.jellycrew.main.MainContract.Companion.RESULT_REQ_MULTIPLE_PERMISSIONS
import handshug.jellycrew.main.viewModel.MainViewModel
import handshug.jellycrew.member.view.LoginActivity
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.DialogUtil
import handshug.jellycrew.utils.Log
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        val viewModel: MainViewModel = getViewModel()

        binding.lifecycleOwner = this
        Preference.loginKey = ""

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
//                when (event) {
//                    FAIL_LOAD_VERSION -> {
//                        mainLogic(viewModel)
//                    }
//                }
            }
        })

        if (!checkPermissions()) return

        // test by pass
        Handler().postDelayed({
            mainLogic()
        }, 1000)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun showUpdateDialog() {
        DialogUtil.simpleDialog(this, "popupVertionUpdate", true) {
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=")
                    )
                )
            } catch (throwable: Throwable) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=")
                    )
                )
            }

            finish()
        }
    }

    private fun mainLogic() {
        // todo 버전체크, 업데이트관리, 로그인, 토큰관리 등등

        if (Preference.isLogin) startMainActivity()
        else startLoginActivity()
    }

    private fun startMainActivity() {
        ActivityUtil.removeAll()
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun startLoginActivity() {
        ActivityUtil.removeAll()
        Intent(this, LoginActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }

    private fun checkPermissions(): Boolean {
        // Common Permissions Check
        val noGrantedPermissions = getNoGrantedPermissions(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE
            )
        )

        return if (noGrantedPermissions.isNotEmpty()) {
            requestPermissions(noGrantedPermissions, RESULT_REQ_MULTIPLE_PERMISSIONS)
            false
        } else true
    }

    private fun getNoGrantedPermissions(permissions: ArrayList<String>): ArrayList<String> {

        val noGrantedPermissions = ArrayList<String>()

        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                noGrantedPermissions.add(permission)
            }
        }

        return noGrantedPermissions
    }

    private fun requestPermissions(noGrantedPermissions: ArrayList<String>, requestCode: Int) {
        Log.msg("# checkPermission : 3 ###################")
        ActivityCompat.requestPermissions(this, noGrantedPermissions.toTypedArray(), requestCode)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RESULT_REQ_MULTIPLE_PERMISSIONS -> run {
//                if (!checkPermissions()) return
                mainLogic()
            }
        }
    }
}