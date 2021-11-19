package handshug.jellycrew.member.view

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.viewpager2.widget.ViewPager2
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingActivity
import handshug.jellycrew.databinding.ActivityJoinBinding
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_SUCCESS
import handshug.jellycrew.member.view.adapter.JoinPagerAdapter
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class JoinActivity : BindingActivity<ActivityJoinBinding>() {

    lateinit var viewPager: ViewPager2

    @LayoutRes
    override fun getLayoutResId() = R.layout.activity_join

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: MemberViewModel = getViewModel()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val pagerAdapter = JoinPagerAdapter(this)

        viewPager = binding.vpJoin
        viewPager.adapter = pagerAdapter
        binding.vpJoin.isUserInputEnabled = false

        binding.vpJoin.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.titleIndex = position
            }
        })

        viewModel.viewEvent.observe(this, {
            it.getContentIfNotHandled()?.let { event ->
                val currentIndex = binding.vpJoin.currentItem
                when (event) {
                    ACTIVITY_CLOSE -> onBackPressed()
                    ACTIVITY_JOIN_SUCCESS -> this.finish()
                }
            }
        })
    }

    fun moveChangePosition(position: Int) {
        viewPager.setCurrentItem(position, true)
    }

    override fun onBackPressed() {
        val currentIndex = binding.vpJoin.currentItem
        if (currentIndex == 0) {
            startActivity(LoginActivity::class.java)
            finish()
        } else {
            moveChangePosition(currentIndex - 1)
        }
    }
}
