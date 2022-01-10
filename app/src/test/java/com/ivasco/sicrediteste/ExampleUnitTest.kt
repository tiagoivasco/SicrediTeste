package com.ivasco.sicrediteste

import com.ivasco.sicrediteste.util.Util
import org.junit.Test

import org.junit.Assert.*
import java.lang.Integer.sum

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun plusOperationTest() {
      assertEquals(4, sum(2, 2))
    }
}