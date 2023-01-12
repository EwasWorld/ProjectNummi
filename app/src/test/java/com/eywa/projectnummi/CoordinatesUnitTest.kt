package com.eywa.projectnummi

import com.eywa.projectnummi.utils.Cartesian
import com.eywa.projectnummi.utils.Polar
import org.junit.Assert.assertEquals
import org.junit.Test

class CoordinatesUnitTest {
    private val equivalents = listOf(
            Cartesian(2.16f, 3.37f) to Polar(4f, 1f),
            Cartesian(-1.06f, 2.26f) to Polar(2.5f, 2.01f),
            Cartesian(-0.6f, -1.27f) to Polar(1.4f, 4.27f),
            Cartesian(2.54f, -2.1f) to Polar(3.29f, 5.59f),
    )

    @Test
    fun test() {
        equivalents.forEach { (cartesian, polar) ->
            val asPolar = cartesian.toPolar()
            assertEquals(
                    "Cartesian: (${cartesian.x},${cartesian.y}) - Bad r\n",
                    polar.r,
                    asPolar.r,
                    0.1f,
            )
            assertEquals(
                    "Cartesian: (${cartesian.x},${cartesian.y}) - Bad theta\n",
                    polar.theta,
                    (asPolar.theta.mod(2 * Math.PI.toFloat())),
                    0.1f,
            )

            val asCartesian = polar.toCartesian()
            assertEquals(
                    "Polar: (${polar.r},${polar.theta}) - Bad x\n",
                    cartesian.x,
                    asCartesian.x,
                    0.1f,
            )
            assertEquals(
                    "Cartesian: (${cartesian.x},${cartesian.y}) - Bad y\n",
                    cartesian.y,
                    asCartesian.y,
                    0.1f,
            )
        }
    }
}
