package handshug.jellycrew.member.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityLoginBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.LOGIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_FACEBOOK
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.Log
import org.koin.androidx.viewmodel.ext.android.getViewModel


class LoginActivity : BindingActivity<ActivityLoginBinding>() {

    private var callbackFacebook: CallbackManager? = null

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()
        this.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.toastMessage.observe(this, {
            toast(it)
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> finish()
                    LOGIN_SUCCESS -> goToMainActivity()
                    START_LOGIN_KAKAO -> startLoginKakao()
                    START_LOGIN_NAVER -> startLoginNaver()
                    START_LOGIN_FACEBOOK -> startLoginFacebook()
                    ACTIVITY_JOIN_EMAIL -> {
                        Intent(this, JoinTermsActivity::class.java).apply {
                            startActivity(this)
                        }
                    }
                }
            }
        })
    }

    private fun startLoginKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            var logMsg = ""
            if(error != null) {
                logMsg = "# Login kakao fail : $error"
                Log.msg(logMsg)
            }
            else if(token != null) {
                Preference.loginType = 1
                logMsg = "# Login kakao success : ${token.accessToken}"
                Log.msg(logMsg)

                goToMainActivity()
            }
            toast(logMsg)
        }

        val client = UserApiClient.instance
        if(client.isKakaoTalkLoginAvailable(this)) {
            client.loginWithKakaoTalk(this, callback = callback)
        } else {
            client.loginWithKakaoAccount(this, callback = callback)
        }
    }

    @SuppressLint("HandlerLeak")
    private fun startLoginNaver() {
        val module = OAuthLogin.getInstance()

        val handler: OAuthLoginHandler = object : OAuthLoginHandler() {
            override fun run(success: Boolean) {
                var logMsg = ""
                if (success) {
                    Preference.loginType = 2

                    val accessToken: String = module.getAccessToken(applicationContext)
                    val refreshToken: String = module.getRefreshToken(applicationContext)
                    val expiresAt: Long = module.getExpiresAt(applicationContext)
                    val tokenType: String = module.getTokenType(applicationContext)

                    logMsg = "# Login naver success : $accessToken"
                    Log.msg(logMsg)

                    goToMainActivity()
                } else {
                    val errorCode: String = module.getLastErrorCode(applicationContext).code
                    val errorDesc: String = module.getLastErrorDesc(applicationContext)

                    logMsg = "# login naver error : $errorCode / $errorDesc"
                    Log.msg(logMsg)
                }
                toast(logMsg)
            }
        }

        module.startOauthLoginActivity(this, handler)
    }

    private fun startLoginFacebook() {
        callbackFacebook = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackFacebook, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Preference.loginType = 3
                toast("# Login facebook success : ${result?.accessToken}")
                Log.msg("# login facebook result : ${result?.accessToken}")

                goToMainActivity()
            }

            override fun onCancel() {
                toast("# login facebook cancel")
                Log.msg("# login facebook cancel")
            }

            override fun onError(error: FacebookException?) {
                toast("# login facebook error : $error")
                Log.msg("# login facebook error : $error")
            }
        })

    }

    private fun goToMainActivity() {
        Preference.isLogin = true

        TimeSynchronizer.sync()
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        ActivityUtil.removeActivity(this)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(callbackFacebook != null) callbackFacebook!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
