package handshug.jellycrew.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AlertDialog
import handshug.jellycrew.R

object DialogUtil {

    fun simpleDialog(
        context: Context,
        message: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setMessage(message)
        builder.setPositiveButton(R.string.btn_ok) { _, _ ->
            function.invoke()
        }
        builder.show()
    }

    fun simpleDialog(
        context: Context,
        message: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit,
        dismiss: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setMessage(message)
        builder.setPositiveButton(R.string.btn_ok) { _, _ ->
            function.invoke()
        }
        builder.setOnDismissListener {
            dismiss.invoke()
        }
        builder.show()
    }

    fun simpleDialog(
        context: Context,
        message: String,
        positiveButton: String,
        negativeButton: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit
    ) {

        val builder = AlertDialog.Builder(context)
        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { _, _ ->
            function.invoke()
        }
        builder.setNegativeButton(negativeButton) { _, _ ->}
        builder.show()
    }

    fun simpleDialog(
        context: Context,
        message: String,
        positiveButton: String,
        negativeButton: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit,
        dismiss: () -> Unit
    ) {

        val builder = AlertDialog.Builder(context)
        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { _, _ ->
            function.invoke()
        }
        builder.setNegativeButton(negativeButton) { _, _ ->}
        builder.setOnDismissListener {
            dismiss.invoke()
        }
        builder.show()
    }

    fun simpleDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { _, _ ->
            function.invoke()
        }
        builder.setNegativeButton(negativeButton) { _, _ -> }
        builder.show()
    }

    fun simpleDialog(
        context: Context,
        title: String,
        message: String,
        positiveButton: String,
        negativeButton: String,
        rejectBackgroundTouchDismiss: Boolean = false,
        function: () -> Unit,
        dismiss: () -> Unit
    ) {
        val builder = AlertDialog.Builder(context)

        if (rejectBackgroundTouchDismiss) {
            builder.setCancelable(false)
        }

        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveButton) { _, _ ->
            function.invoke()
        }
        builder.setNegativeButton(negativeButton) { _, _ -> }
        builder.setOnDismissListener {
            dismiss.invoke()
        }
        builder.show()
    }

    fun webViewDialog(
        context: Context,
        title: String,
        html: String,
    ) {
        WebView(context).apply {
            initWebView(this)
            loadDataWithBaseURL(null, html, "text/html; charset=utf-8", "utf-8",null)

            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setView(this)
                .setPositiveButton(R.string.btn_ok) { _, _ -> }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    dialog.show()
                }
            }
        }

    }

    fun webViewUrlDialog(
        context: Context,
        title: String,
        url: String,
    ) {

        WebView(context).apply {
            initWebView(this)

            loadUrl(url)
            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setView(this)
                .setPositiveButton(R.string.btn_ok) { _, _ -> }

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    dialog.show()
                }
            }
        }

    }

    fun shareDialog(context: Context, url: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(webView: WebView) {
        val webViewSettings = webView.settings
        webViewSettings.defaultTextEncodingName = "UTF-8"
    }

}