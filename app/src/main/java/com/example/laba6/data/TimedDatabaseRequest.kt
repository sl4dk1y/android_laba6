package com.example.laba6.data

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** Simulates a database operation without blocking the caller or the UI thread. */
object TimedDatabaseRequest {
    const val DURATION_MILLIS = 10_000L

    suspend fun execute(): Boolean = withContext(Dispatchers.IO) {
        delay(DURATION_MILLIS)
        true
    }
}

data class TimedDatabaseRequestState(
    val isRunning: Boolean = false,
    val result: Boolean? = null
)

/** Owns the single in-flight request state used by the settings screen. */
class TimedDatabaseRequestController(
    private val executeRequest: suspend () -> Boolean = { TimedDatabaseRequest.execute() }
) {
    private val mutableState = MutableStateFlow(TimedDatabaseRequestState())
    val state: StateFlow<TimedDatabaseRequestState> = mutableState

    @Synchronized
    fun start(scope: CoroutineScope): Boolean {
        if (mutableState.value.isRunning) return false

        mutableState.value = TimedDatabaseRequestState(isRunning = true)
        scope.launch {
            try {
                val result = executeRequest()
                mutableState.value = TimedDatabaseRequestState(result = result)
            } catch (cancelled: CancellationException) {
                mutableState.value = TimedDatabaseRequestState()
                throw cancelled
            } catch (_: Exception) {
                mutableState.value = TimedDatabaseRequestState(result = false)
            }
        }
        return true
    }
}
