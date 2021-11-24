package handshug.jellycrew.member.viewModel

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.api.member.MemberPhoneCheckMigrationResponse
import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.member.MemberContract
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_SUCCESS
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN_HOME
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_PAST_ORDERS
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_START
import handshug.jellycrew.member.MemberContract.Companion.COUNT_DOWN_TIMER_STOP
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_EMAIL
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PASSWORD
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_PHONE
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_TERMS
import handshug.jellycrew.member.MemberContract.Companion.FRAGMENT_JOIN_USER_INFO
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
import handshug.jellycrew.utils.EtcUtil
import handshug.jellycrew.utils.ResponseCode.SUCCESS
import handshug.jellycrew.utils.visible
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

    val selectedGender: MutableLiveData<Int> = MutableLiveData()

    fun activityClose() = viewEvent(ACTIVITY_CLOSE)
    fun navigateToMain() = viewEvent(ACTIVITY_MAIN)
    fun navigateToLogin() = viewEvent(ACTIVITY_LOGIN)
    fun navigateToLoginHome() = viewEvent(ACTIVITY_LOGIN_HOME)
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
                _isPhoneVerifySendSuccess.value = this.code == SUCCESS
            }
        }
    }

    fun phoneVerifyConfirm(phoneNumber: String, verifyCode: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.phoneVerifyConfirm(phoneNumber, verifyCode).apply {
                _isPhoneVerifyConfirmSuccess.value = this.code == SUCCESS
            }
        }
    }

    fun alreadyJoinCheckMigration(phoneNumber: String, userName: String) {
        viewModelScope.launch(exceptionHandler) {
            memberApi.checkMigration(phoneNumber, userName).apply {
                if (this.code == SUCCESS) {
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
                if (this.code == SUCCESS) {
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
                    "DIRECT"
            ).apply {
                _isJoinSuccess.value = this.code == SUCCESS
            }
        }
    }

    private fun getJoinUserInfoParams() = mutableMapOf<String, Any>().apply {
        this["email"] = Preference.userEmail
        this["password"] = Preference.userPassword
        this["mobile"] = Preference.userPhoneNumber
        this["birth"] = Preference.userBirth
        this["genderType"] = Preference.userGender
        this["name"] = Preference.userName
        this["accountReferType"] = "DIRECT"
        this["nickname"] = Preference.userNickname
        this["recommendFriend"] = Preference.userFriend
        this["marketingAgreement"] = Preference.isMarketingAgree
        this["lifeTimeMember"] = Preference.isLifeTimeMember
        this["socialType"] = Preference.userSocialType
        this["socialId"] = Preference.userSocialId
        this["socialEmail"] = Preference.userSocialEmail
        this["accessToken"] = ""
        this["refreshToken"] = ""
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