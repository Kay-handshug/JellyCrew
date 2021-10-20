package handshug.jellycrew.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import handshug.jellycrew.R
import handshug.jellycrew.utils.gone
import handshug.jellycrew.utils.visible

abstract class BindingFragment<T: ViewDataBinding> : Fragment() {

    @LayoutRes
    abstract fun getLayoutResId(): Int

    lateinit var binding: T

    private var activity: Activity? = null
    private var progressBar: LinearLayout? = null

    private lateinit var inputMethodManager: InputMethodManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val name = this.javaClass.simpleName

//        Bundle().apply {
//            this.putString(FirebaseAnalytics.Param.SCREEN_CLASS, name)
//            FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, this)
//        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutResId(), container, false)
        progressBar = binding.root.findViewById(R.id.progress_bar)

        inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        return binding.root
    }

    fun hideKeyboard(view: View) {
        val token = view.windowToken
        inputMethodManager.hideSoftInputFromWindow(token, 0)
    }

    fun <T> startActivity(cls: Class<T>) {
        Intent(context, cls).apply { startActivity(this) }
    }

    fun <T> startActivityWithId(cls: Class<T>, id: Long) {
        Intent(context, cls)
            .apply { putExtra("id", id) }
            .apply { startActivity(this) }
    }

    fun <T> startActivityWithData(cls: Class<T>, bundle: Bundle) {
        Intent(context, cls)
            .apply { putExtras(bundle) }
            .apply { startActivity(this) }
    }

    fun <T> startActivityForResult(cls: Class<T>, id: Long, requestCode: Int) {
        Intent(context, cls)
            .apply { putExtra("id", id) }
            .apply { startActivityForResult(this, requestCode) }
    }

    fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showProgressBar() {
        progressBar?.visible()
    }

    fun hideProgressBar() {
        progressBar?.gone()
    }
}