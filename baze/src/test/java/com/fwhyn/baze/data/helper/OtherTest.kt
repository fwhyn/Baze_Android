package com.fwhyn.baze.data.helper

import com.fwhyn.baze.data.model.Exzeption
import org.junit.Test

class OtherTest {
    fun main() {
        try {
            throw Exzeption()
        } catch (e: Exzeption) {
            println("Exzeption: ${e.message}")
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        }
    }

    @Test
    fun test() {
        main()
    }
}