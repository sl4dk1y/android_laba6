package com.example.laba6

import com.example.laba6.data.TimedDatabaseRequest
import com.example.laba6.data.TimedDatabaseRequestController
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.flow.first
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TimedDatabaseRequestTest {
    @Test
    fun simulatedRequestReturnsTrueAndTakesAtLeastTenSeconds() = runBlocking {
        val startedAt = System.nanoTime()

        val result = TimedDatabaseRequest.execute()

        val elapsedMillis = (System.nanoTime() - startedAt) / 1_000_000
        assertTrue(result)
        assertTrue("Request completed early after $elapsedMillis ms", elapsedMillis >= 10_000)
    }

    @Test
    fun controllerShowsLoadingThenResultAndRejectsDuplicateStart() = runBlocking {
        val controller = TimedDatabaseRequestController {
            delay(100)
            true
        }

        assertFalse(controller.state.value.isRunning)
        assertTrue(controller.start(this))
        assertTrue(controller.state.value.isRunning)
        assertEquals(null, controller.state.value.result)
        assertFalse("A second request must be rejected while one is running", controller.start(this))

        val completedState = withTimeout(1_000) {
            controller.state.first { !it.isRunning }
        }
        assertEquals(true, completedState.result)
        assertFalse(completedState.isRunning)
    }
}
