package com.eywa.projectnummi.common

fun Int.pennyAmountAsString() = "£%.2f".format(this / 100.0)
