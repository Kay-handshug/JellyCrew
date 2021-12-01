package handshug.jellycrew.member.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.annotation.LayoutRes
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityLoginBinding
import handshug.jellycrew.main.view.MainActivity
import handshug.jellycrew.member.MemberContract
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_TERMS
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_HOME
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_PAST_ORDERS
import handshug.jellycrew.member.MemberContract.Companion.FACEBOOK
import handshug.jellycrew.member.MemberContract.Companion.LOGIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_FACEBOOK
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER
import handshug.jellycrew.member.viewModel.MemberViewModel
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.Log
import org.json.JSONException
import org.koin.androidx.viewmodel.ext.android.getViewModel


class LoginActivity : BindingActivity<ActivityLoginBinding>() {

    private val FACEBOOK_SIGN_IN = 64206
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
                    ACTIVITY_CLOSE -> finishAffinity() // finish()
                    ACTIVITY_MAIN -> goToMainActivity()
                    ACTIVITY_LOGIN_HOME -> goToMainActivity()
                    ACTIVITY_LOGIN_EMAIL -> goToLoginEmail()
                    ACTIVITY_PAST_ORDERS -> goToPastOrders()
                    FRAGMENT_JOIN_TERMS -> goToJoinTerms()
                    START_LOGIN_KAKAO -> viewModel.getStartLoginKakao() // startLoginKakao(viewModel)
                    START_LOGIN_NAVER -> viewModel.getStartLoginNaver(this)
                    START_LOGIN_FACEBOOK -> startLoginFacebook(viewModel)
                    LOGIN_SUCCESS -> goToMainActivity()
                }
            }
        })
    }

    // CallbackManager 사용 문제로 viewModel로 이동X
    private fun startLoginFacebook(viewModel: MemberViewModel) {
        callbackFacebook = CallbackManager.Factory.create()

        val loginManager = LoginManager.getInstance()
        loginManager.logInWithReadPermissions(
                this,
                listOf("public_profile", "email", "user_birthday", "user_gender")
        )

        loginManager.registerCallback(callbackFacebook, object: FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Preference.loginType = 3
                Preference.userSocialType = FACEBOOK

                result?.accessToken?.apply {
                    Preference.userSocialId = userId
                    Preference.socialAccessToken = token

                    Log.msg("# Login Facebook accessToken : $token")

                    val request = GraphRequest.newMeRequest(this) { json, response ->
                        Log.msg("# Login Facebook Success : $response")

                        try {
                            Log.msg("# Login Facebook Success : json data :: $json")

                            Preference.userSocialName = json.optString("name")
                            Preference.userSocialEmail = json.optString("email")
                            Preference.userSocialGender = json.optString("gender")
                            Preference.userSocialBirthDay = json.optString("birthday")


                        } catch (e: JSONException) {
                            throw RuntimeException("Facebook login error : $e")
                        }
                    }
                    request.executeAsync()

                    viewModel.loginSocial(FACEBOOK)
                }
            }

            override fun onCancel() {
                Log.msg("# login facebook cancel")
            }

            override fun onError(error: FacebookException?) {
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

    private fun goToJoinTerms() {
        Intent(this, JoinActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        ActivityUtil.removeActivity(this)
        finish()
    }
    
    private fun goToLoginEmail() {
        Intent(this, LoginEmailActivity::class.java).apply {
            startActivity(this)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
        ActivityUtil.removeActivity(this)
        finish()
    }

    private fun goToPastOrders() {
        val url = "http://www.naver.com"
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            startActivity(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == FACEBOOK_SIGN_IN && callbackFacebook != null) {
            callbackFacebook!!.onActivityResult(requestCode, resultCode, data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
