package com.eywa.projectnummi.utils

import kotlin.math.*

data class Cartesian(
        val x: Float,
        val y: Float,
) {
    fun toPolar() = Polar(
            sqrt(x.pow(2) + y.pow(2)),
            atan2(y, x),
    )
}

/**
 * @param theta in radians
 */
data class Polar(
        val r: Float,
        val theta: Float,
) {
    fun toCartesian() = Cartesian(
            r * cos(theta),
            r * sin(theta),
    )

    fun addDegrees(degrees: Float) = copy(theta = Math.toRadians(degrees.toDouble()).toFloat() + theta)
}
