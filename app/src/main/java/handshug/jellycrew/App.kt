package handshug.jellycrew

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.kakao.sdk.common.KakaoSdk
import com.nhn.android.naverlogin.OAuthLogin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import handshug.jellycrew.modules.apiModule
import handshug.jellycrew.modules.networkModule
import handshug.jellycrew.modules.viewModelModule
import kr.co.bootpay.BootpayAnalytics
import kotlin.random.Random

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                    apiModule,
                    networkModule,
                    viewModelModule
                )
            )
        }

        // 초기설정 - 해당 프로젝트(안드로이드)의 application id 값을 설정합니다. 결제와 통계를 위해 꼭 필요합니다.
        BootpayAnalytics.init(this, this.getString(R.string.BOOTPAY_APPLICATION_ID))

        // Kakao
        KakaoSdk.init(this, this.getString(R.string.KAKAO_APP_KEY))
        
        // Naver
        OAuthLogin.getInstance().init(
            this,
            this.getString(R.string.NAVER_CLIENT_ID),
            this.getString(R.string.NAVER_CLIENT_SECRET),
            this.getString(R.string.app_name)
        )

        // Facebook
        FacebookSdk.sdkInitialize(this)
//        FacebookSdk.setAutoInitEnabled(true)
//        AppEventsLogger.activateApp(this)

//        FirebaseAnalytics.getInstance(this).setUserId(userId)
    }
}