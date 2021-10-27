package handshug.jellycrew.member.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityLoginBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.LOGIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.Log
import org.koin.androidx.viewmodel.ext.android.getViewModel


class LoginActivity : BindingActivity<ActivityLoginBinding>() {

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
                }
            }
        })
    }

    private fun startLoginKakao() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if(error != null) {
                Log.msg("# Login fail : $error")
            }
            else if(token != null) {
                Log.msg("# Login success : ${token.accessToken}")
            }
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
                if (success) {
                    val accessToken: String = module.getAccessToken(applicationContext)
                    val refreshToken: String = module.getRefreshToken(applicationContext)
                    val expiresAt: Long = module.getExpiresAt(applicationContext)
                    val tokenType: String = module.getTokenType(applicationContext)
                } else {
                    val errorCode: String = module.getLastErrorCode(applicationContext).code
                    val errorDesc: String = module.getLastErrorDesc(applicationContext)
                }
            }
        }

        module.startOauthLoginActivity(this, handler)
    }

    private fun goToMainActivity() {
        TimeSynchronizer.sync()
        Intent(this, MainActivity::class.java).apply {
            startActivity(this)
        }
        ActivityUtil.removeActivity(this)
        finish()
    }
}
