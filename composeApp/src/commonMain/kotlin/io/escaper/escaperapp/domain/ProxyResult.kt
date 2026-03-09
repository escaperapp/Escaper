package io.escaper.escaperapp.domain

sealed interface ProxyStartResult {
    data class Success(val strategy: Strategy) : ProxyStartResult
    data class Error(val errorMessage: String) : ProxyStartResult
}

sealed interface ProxyStopResult {
    object Success : ProxyStopResult
    data class Error(val errorMessage: String) : ProxyStopResult
}