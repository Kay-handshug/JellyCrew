package handshug.jellycrew.main.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import handshug.jellycrew.R
import handshug.jellycrew.TimeSynchronizer
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityMainBinding
import handshug.jellycrew.main.MainContract.Companion.ACTIVITY_SPLASH
import handshug.jellycrew.main.MainContract.Companion.APP_END_DELAY_TIME
import handshug.jellycrew.main.MainContract.Companion.DRAWER_MENU_OPEN
import handshug.jellycrew.main.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import handshug.jellycrew.home.view.HomeFragment
import handshug.jellycrew.main.MainContract
import handshug.jellycrew.utils.Log

class MainActivity : BindingActivity<ActivityMainBinding>() {

    private var shutdownMsg = ""

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MainViewModel = getViewModel()
        super.viewModel = viewModel

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.rootLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_SPLASH -> startActivity(SplashActivity::class.java)
                    DRAWER_MENU_OPEN -> binding.rootLayout.openDrawer(binding.mainNavigationView)
                }
            }
        })

        binding.rootLayout.addDrawerListener(object: DrawerLayout.DrawerListener{
            override fun onDrawerOpened(drawerView: View) {
                //drawerMenuHeaderBinding.date = FormatterUtil.getTodayDate()
            }
            override fun onDrawerClosed(drawerView: View) { }
            override fun onDrawerStateChanged(newState: Int) { }
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) { }
        })

        binding.mainNavigationView.setNavigationItemSelectedListener {
//            when(it.itemId) {
//                R.id.drawer_menu_01 -> startActivity(InfoActivity::class.java)
//            }
            true
        }

        val homeFragment = HomeFragment.newInstance()
        replaceFragment(homeFragment)

//        requestPermissions(this) { state ->
//            if (state) {
//                Toast.makeText(this, "# permission : checked", Toast.LENGTH_SHORT).show()
//            }
//            else {
//                Toast.makeText(this, "# permission : fail", Toast.LENGTH_SHORT).show()
//                showPermissionsSettingDialog(this)
//            }
//        }
    }

    // 뒤로가기 2회 클릭 시 종료
    private var appEndFlag = false
    override fun onBackPressed() {
        if(appEndFlag) {
            super.onBackPressed()
            return
        }

        if(binding.rootLayout.isDrawerOpen(binding.mainNavigationView)) {
            binding.rootLayout.closeDrawers()
            return
        }

        appEndFlag = true
        toast(getString(R.string.toast_app_finish))
        Handler().postDelayed({ appEndFlag = false }, APP_END_DELAY_TIME)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val root = binding.rootLayout
        val drawer = binding.mainNavigationView

        if (root.isDrawerOpen(drawer))
            root.closeDrawer(drawer)

        if (intent == null)
            return
    }

    fun finish(msg: String) {
        shutdownMsg = msg
        finish()
    }

    override fun onDestroy() {
        if (shutdownMsg.isNotEmpty()) {
            toast(shutdownMsg)
        }
//        Preference.forgetLoginInfo()
        TimeSynchronizer.release()
        super.onDestroy()
    }

    fun openDrawer() {
        binding.rootLayout.openDrawer(binding.mainNavigationView)
    }
}