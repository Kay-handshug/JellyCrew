package handshug.jellycrew.base

import handshug.jellycrew.utils.ErrorHandler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers

object BaseCoroutine {

    val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            ErrorHandler.errorHandle(throwable)
        }

    val ioDispatchers = Dispatchers.IO + exceptionHandler
    val uiDispatchers = Dispatchers.Main + exceptionHandler
    val defaultDispatchers = Dispatchers.Default + exceptionHandler
}