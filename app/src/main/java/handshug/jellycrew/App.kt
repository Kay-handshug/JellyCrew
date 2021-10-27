package handshug.jellycrew

import android.app.Application
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
        BootpayAnalytics.init(this, "616e6b217b5ba4f7e3529b99")

        // Kakao
        KakaoSdk.init(this, "aed9b518e0077c6a7c0b1a367bca9f10")
        
        // Naver
        OAuthLogin.getInstance().init(
            this,
            "u5KRBxmTD3QCsM7l3wF2",
            "07YMCEwPuh",
            getString(R.string.app_name)
        )

//        FirebaseAnalytics.getInstance(this).setUserId(userId)
    }
}