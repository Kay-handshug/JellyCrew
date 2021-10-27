package handshug.jellycrew.main.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.databinding.ActivitySplashBinding
import handshug.jellycrew.main.viewModel.MainViewModel
import handshug.jellycrew.member.view.LoginActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.DialogUtil

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivitySplashBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)

        val viewModel: MainViewModel = getViewModel()

        binding.lifecycleOwner = this
        Preference.loginKey = ""

        // test by pass
        Handler().postDelayed({
//            if(isLogin) startMainActivity()
//            else startLoginActivity()
            startLoginActivity()
        }, 1000)

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
//                when (event) {
//                    FAIL_LOAD_VERSION -> {
//                        mainLogic(viewModel)
//                    }
//                }
            }
        })
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

    private fun mainLogic(viewModel: MainViewModel) {
            startMainActivity()
        // todo 버전체크, 업데이트관리, 로그인, 토큰관리 등등
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