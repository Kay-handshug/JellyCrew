package handshug.jellycrew.member.viewModel

import android.annotation.SuppressLint
import android.app.Activity
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.api.member.MemberPhoneCheckMigrationResponse
import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.member.MemberContract
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_FIND_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_HOME
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_PAST_ORDERS
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_RESET_PASSWORD
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_START
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_STOP
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PASSWORD
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PHONE
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_TERMS
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_USER_INFO
import handshug.jellycrew.member.MemberContract.Companion.KAKAO
import handshug.jellycrew.member.MemberContract.Companion.LOGIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.MEMBER_LOGIN
import handshug.jellycrew.member.MemberContract.Companion.NAVER
import handshug.jellycrew.member.MemberContract.Companion.REQ_PHONE_VERIFY_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_DATE_PICKER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_FINISH
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_GENDER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_REQUEST_VERIFY_SEND_FAIL
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TERMS_DETAIL_01
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TERMS_DETAIL_02
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST_VERIFY_SEND
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_USER_INFO_NOTI
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_FACEBOOK
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER
import handshug.jellycrew.member.model.MemberApi
import handshug.jellycrew.utils.*
import kotlinx.coroutines.launch

class MemberViewModel(private val memberApi: MemberApi) : BaseViewModel(), MemberContract {

    private val _isPhoneVerifySendSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isPhoneVerifySendSuccess: LiveData<Boolean>
        get() = _isPhoneVerifySendSuccess

    private val _isPhoneVerifyConfirmSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isPhoneVerifyConfirmSuccess: LiveData<Boolean>
        get() = _isPhoneVerifyConfirmSuccess

    private val _alreadyJoinData: MutableLiveData<MemberPhoneCheckMigrationResponse> = MutableLiveData()
    val alreadyJoinData: LiveData<MemberPhoneCheckMigrationResponse>
        get() = _alreadyJoinData

    private val _sameCheckEmail: MutableLiveData<Boolean> = MutableLiveData()
    val sameCheckEmail: LiveData<Boolean>
        get() = _sameCheckEmail

    private val _isJoinSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isJoinSuccess: LiveData<Boolean>
        get() = _isJoinSuccess

    private val _isLoginSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isLoginSuccess: LiveData<Boolean>
        get() = _isLoginSuccess

    val selectedGender: MutableLiveData<Int> = MutableLiveData()

    fun memberLogin() = viewEvent(MEMBER_LOGIN)
    fun loginSuccess() = viewEvent(LOGIN_SUCCESS)

    fun activityClose() = viewEvent(ACTIVITY_CLOSE)
    fun navigateToMain() = viewEvent(ACTIVITY_MAIN)
    fun navigateToLogin() = viewEvent(ACTIVITY_LOGIN)
    fun navigateToLoginHome() = viewEvent(ACTIVITY_LOGIN_HOME)
    fun navigateToLoginEmail() = viewEvent(ACTIVITY_LOGIN_EMAIL)
    fun navigateToLoginFindEmail() = viewEvent(ACTIVITY_FIND_EMAIL)
    fun navigateToLoginResetPassword() = viewEvent(ACTIVITY_RESET_PASSWORD)

    fun navigateToJoinConfirm() = viewEvent(ACTIVITY_JOIN_CONFIRM)
    fun navigateToJoinSuccess() = viewEvent(ACTIVITY_JOIN_SUCCESS)
    fun navigateToPastOrders() = viewEvent(ACTIVITY_PAST_ORDERS)

    fun navigateToJoinTerms() = viewEvent(FRAGMENT_JOIN_TERMS)
    fun navigateToJoinPhone() = viewEvent(FRAGMENT_JOIN_PHONE)
    fun navigateToJoinPassword() = viewEvent(FRAGMENT_JOIN_PASSWORD)
    fun navigateToJoinUserInfo() = viewEvent(FRAGMENT_JOIN_USER_INFO)

    fun startLoginKakao() = viewEvent(START_LOGIN_KAKAO)
    fun startLoginNaver() = viewEvent(START_LOGIN_NAVER)
    fun startLoginFacebook() = viewEvent(START_LOGIN_FACEBOOK)

    fun reqPhoneVerifyConfirm() = viewEvent(REQ_PHONE_VERIFY_CONFIRM)

    fun showDialogFinish() = viewEvent(SHOW_DIALOG_FINISH)
    fun showDialogUserInfoNoti() = viewEvent(SHOW_DIALOG_USER_INFO_NOTI)
    fun showDialogToastSend() = viewEvent(SHOW_DIALOG_TOAST_VERIFY_SEND)
    fun showDialogDatePicker() = viewEvent(SHOW_DIALOG_DATE_PICKER)
    fun showDialogGender() = viewEvent(SHOW_DIALOG_GENDER)
    fun showDialogTermsDetail01() = viewEvent(SHOW_DIALOG_TERMS_DETAIL_01)
    fun showDialogTermsDetail02() = viewEvent(SHOW_DIALOG_TERMS_DETAIL_02)
    fun showDialogRequestVerifySendFail() = viewEvent(SHOW_DIALOG_REQUEST_VERIFY_SEND_FAIL)

    fun countDownTimerStart() = viewEvent(COUNT_DOWN_TIMER_START)
    fun countDownTimerStop() = viewEvent(COUNT_DOWN_TIMER_STOP)


    fun verifyPhoneNumber(phoneNumber: String) = phoneNumber.startsWith("01")
    fun verifyEmail(email: String) = regexPattern(REGEX_PATTERN_EMAIL, email)
    fun verifyAuthCode(code: String) = regexPattern(REGEX_PATTERN_AUTH_CODE, code)

    fun verifyPasswordAlphabet(password: String) = regexPattern(REGEX_PATTERN_ALPHABET, password)
    fun verifyPasswordNumber(password: String) = regexPattern(REGEX_PATTERN_NUMBER, password)
    fun verifyPasswordSpecialCharacters(password: String) = regexPattern(REGEX_PATTERN_SPECIAL_CHARACTERS, password)

    fun verifyName(name: String) = regexPattern(REGEX_PATTERN_TEXT, name)

    fun phoneVerifySend(phoneNumber: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.phoneVerifySend(phoneNumber).apply {
                _isPhoneVerifySendSuccess.value = checkSuccess(this.code)
            }
        }
    }

    fun phoneVerifyConfirm(phoneNumber: String, verifyCode: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.phoneVerifyConfirm(phoneNumber, verifyCode).apply {
                _isPhoneVerifyConfirmSuccess.value = checkSuccess(this.code)
            }
        }
    }

    fun alreadyJoinCheckMigration(phoneNumber: String, userName: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.checkMigration(phoneNumber, userName).apply {
                if (checkSuccess(this.code)) {
                    // 기회원 -> dialog show
                    _alreadyJoinData.value = this.data
                }
                else {
                    // 미가입 회원 -> 계속 가입 진행
                    viewEvent(FRAGMENT_JOIN_EMAIL)
                }
            }
        }
    }

    fun sameCheckEmail(email: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.checkEmail(email).apply {
                if (checkSuccess(this.code)) {
                    _sameCheckEmail.value = this.data
                }
                else {
                    _sameCheckEmail.value = true
                }
            }
        }
    }

    fun joinConfirm() {
        viewModelScope.launch(exceptionHandler) {
            memberApi.joinConfirm(
                    Preference.userEmail,
                    Preference.userPassword,
                    Preference.userPhoneNumber,
                    Preference.userBirth,
                    Preference.userGender,
                    Preference.userName,
                    "DIRECT",
                    Preference.userNickname,
                    Preference.userFriend,
                    Preference.isMarketingAgree,
                    Preference.isLifeTimeMember,
                    Preference.userSocialType,
                    Preference.userSocialId,
                    Preference.userSocialEmail,
                    Preference.socialAccessToken,
                    Preference.socialRefreshToken
            ).apply {
                _isJoinSuccess.value = checkSuccess(this.code)
            }
        }
    }

    fun loginEmail() {
        viewModelScope.launch(exceptionHandler) {
            memberApi.loginEmail(setLoginEmailParams()).apply {

                Preference.isLogin = false

                if (checkSuccess(this.code)) {
                    val data = this.data.token
                    Preference.isLogin = true
                    Preference.accessToken = data.access_token
                    Preference.refreshToken = data.refresh_token
                    Preference.userId = data.accountId

                    Log.msg("# JWT accessToken : ${Preference.accessToken}")
                    Log.msg("# JWT refreshToken : ${Preference.refreshToken}")
                    Log.msg("# JWT userId : ${Preference.userId}")

                    JwtUtil.decoded(Preference.accessToken, true)
                    JwtUtil.decoded(Preference.refreshToken, false)

                    _isLoginSuccess.value = true
                }
                else {
                    toast(this.message)
                }
            }
        }
    }

    fun loginSocial(socialType: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.loginSocial(setLoginSocialParams(socialType)).apply {
                if (checkSuccess(this.code)) {
                    EtcUtil.login(this.data)
                    loginSuccess()
                }
            }
        }
    }

    private fun setLoginEmailParams() = mutableMapOf<String, Any>().apply {
        this["email"] = Preference.userEmail
        this["password"] = Preference.userPassword
    }

    private fun setLoginSocialParams(socialType: String) = mutableMapOf<String, Any>().apply {
        this["appType"] = "AOS"
        this["socialType"] = socialType
        this["token"] = Preference.socialAccessToken
    }

    fun getStartLoginKakao() {
        viewModelScope.launch(exceptionHandler) {
            val client = UserApiClient.instance
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                var logMsg = ""
                if (error != null) {
                    logMsg = "# Login kakao fail : $error"
                    Log.msg(logMsg)
                } else token?.apply {
                    Preference.loginType = 1
                    Preference.userSocialType = KAKAO
                    Preference.socialAccessToken = accessToken
                    Preference.socialAccessTokenExpiredAt = accessTokenExpiresAt.time
                    Preference.socialRefreshToken = refreshToken
                    Preference.socialRefreshTokenExpiredAt = refreshTokenExpiresAt.time

                    logMsg = "# Login kakao success : $accessToken"
                    Log.msg(logMsg)

                    client.me { user, _ ->
                        if (user != null) {
                            Log.msg("# Login kakao user info access success : ${user.kakaoAccount?.email}")

                            val userAccount = user.kakaoAccount
                            val userBirth = FormatterUtil.getBirthStringFormat(userAccount?.birthyear?: "", userAccount?.birthday ?: "")
                            Preference.userSocialId = user.id.toString()
                            Preference.userSocialEmail = userAccount?.email ?: ""
                            Preference.userSocialPhoneNumber = userAccount?.phoneNumber ?: ""
                            Preference.userSocialNickname = userAccount?.profile?.nickname ?: ""
                            Preference.userSocialPhoto = userAccount?.profile?.thumbnailImageUrl ?: ""
                            Preference.userSocialGender = userAccount?.gender?.name ?: ""
                            Preference.userSocialBirthDay = userBirth
                        }

                        loginSocial(KAKAO)
                    }
                    toast(logMsg)
                }
            }

            if (client.isKakaoTalkLoginAvailable(context)) {
                client.loginWithKakaoTalk(context, callback = callback)
            } else {
                client.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    @SuppressLint("HandlerLeak")
    fun getStartLoginNaver(activity: Activity) {
        viewModelScope.launch(exceptionHandler) {
            val module = OAuthLogin.getInstance()

            val handler: OAuthLoginHandler = object : OAuthLoginHandler() {
                override fun run(success: Boolean) {

                    module.apply {
                        with(context) {
                            var logMsg = ""
                            if (success) {
                                Preference.loginType = 2
                                Preference.userSocialType = NAVER
                                Preference.socialAccessToken = getAccessToken(this)
                                Preference.socialRefreshToken = getRefreshToken(this)
                                Preference.socialAccessTokenExpiredAt = getExpiresAt(this)
                                Preference.socialRefreshTokenExpiredAt = -1L

                                logMsg = "# Login naver success : ${Preference.socialAccessToken}"
                                Log.msg(logMsg)

                                GetUserInfoTask(this).start()

                                loginSocial(NAVER)
                            } else {
                                val errorCode: String = getLastErrorCode(this).code
                                val errorDesc: String = getLastErrorDesc(this)

                                logMsg = "# login naver error : $errorCode / $errorDesc"
                                Log.msg(logMsg)
                            }
                            toast(logMsg)
                        }
                    }
                }
            }

            module.startOauthLoginActivity(activity, handler)
        }
    }

    fun countDownTimer(textView: AppCompatTextView, errorMsg: AppCompatTextView,
                       btnRequestVerify: AppCompatButton, btnNext: AppCompatButton): CountDownTimer {
        return object: CountDownTimer(3 * 3 * 1000L, 1000L) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millis: Long) {
                val min = convertTimeFormat(getMinutesFromMillis(millis))
                val sec = convertTimeFormat(getSecondsFromMillis(millis))

                textView.text = "$min:$sec"
            }

            override fun onFinish() {
                textView.text = "00:00"
                errorMsg.text = context.getString(R.string.join_phone_error_05)
                errorMsg.visible()

                btnRequestVerify.isSelected = true
                btnNext.isSelected = false
                btnNext.isEnabled = false
            }
        }
    }

    private fun convertTimeFormat(time: Long) = if (time < 10) "0$time" else "$time"

    private fun getMinutesFromMillis(millis: Long) = millis / 1000 / 60

    private fun getSecondsFromMillis(millis: Long) = millis / 1000 % 60
}