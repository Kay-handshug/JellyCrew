package handshug.jellycrew.main.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

//        if (!checkPermissions()) return

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

//        if (Preference.isLogin) startMainActivity()
//        else startLoginActivity()

        startLoginActivity()
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
}