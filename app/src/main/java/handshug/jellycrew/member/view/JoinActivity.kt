package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.viewpager2.widget.ViewPager2
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityJoinBinding
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_CONFIRM
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_LOGIN
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_FINISH
import handshug.jellycrew.member.view.adapter.JoinPagerAdapter
import handshug.jellycrew.member.view.dialog.MemberDialog
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class JoinActivity : BindingActivity<ActivityJoinBinding>() {

    lateinit var viewPager: ViewPager2
    lateinit var showFinishDialog: AlertDialog

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_join

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val dialog = MemberDialog(this, viewModel)
        showFinishDialog = dialog.showLoginFinishDialog()

        val isSocialJoin = intent.getBooleanExtra("isSocialJoin", false)
        val pagerAdapter = JoinPagerAdapter(this, isSocialJoin)

        viewPager = binding.vpJoin
        viewPager.adapter = pagerAdapter
        binding.vpJoin.isUserInputEnabled = false
        binding.title = ""

        binding.vpJoin.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.titleIndex = position
            }
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    ACTIVITY_CLOSE -> {
                        showFinishDialog.dismiss()
                        startActivity(LoginActivity::class.java)
                        finish()
                    }
                    ACTIVITY_LOGIN -> goToLogin()
                    ACTIVITY_JOIN_CONFIRM -> this.finish()
                    SHOW_DIALOG_FINISH -> showFinishDialog.show()
                }
            }
        })

        hideProgressBar()
    }

    fun moveChangePosition(position: Int) {
        viewPager.setCurrentItem(position, true)
    }

    fun goToLogin() {
        startActivity(LoginActivity::class.java)
        finish()
    }

    override fun onBackPressed() {
//        val currentIndex = binding.vpJoin.currentItem
//        if (currentIndex == 0) {
//            goToLogin()
//        } else {
//            moveChangePosition(currentIndex - 1)
//        }

        showFinishDialog.show()
    }
}
