package org.turter.patrocl.presentation.error

import io.ktor.client.network.sockets.SocketTimeoutException
import org.turter.patrocl.domain.exception.ApiHttpException
import org.turter.patrocl.domain.exception.WebSocketSessionCloseException

sealed class ErrorType {
    object NetworkError : ErrorType()
    object WsCloseError : ErrorType()
    data class WsFailureError(val cause: Throwable?) : ErrorType()
    object ServerError : ErrorType()
    object NotBindWaiterError : ErrorType()
    object NotBindEmployeeError : ErrorType()
    data class UnknownError(val cause: Throwable?) : ErrorType()

    companion object Factory {
        fun from(throwable: Throwable) =
            when(val ex = throwable) {
//                is ApiHttpException, is SocketTimeoutException -> NetworkError
//                is WebSocketSessionCloseException -> WsCloseError
//                is WebSocketFailureException, is ProtocolException -> WsFailureError(cause = ex.cause)
                else -> UnknownError(ex)
            }
    }

    fun getMessage() =
        when (this) {
            is NetworkError -> "NetworkError"
            is ServerError -> "ServerError"
            is WsCloseError -> "WsCloseError"
            is WsFailureError -> "WsFailureError"
            is NotBindWaiterError -> "NotBindWaiterError"
            is NotBindEmployeeError -> "NotBindEmployeeError"
            is UnknownError -> "Error"
            else -> "Unknown error"
        }

    fun getAdvice() =
        when (this) {
            is NotBindWaiterError -> "NotBindWaiterError"
            is NotBindEmployeeError -> "NotBindEmployeeError"
            is UnknownError -> this.cause?.message?:"Call to support"
            else -> "Call to support"
        }
}