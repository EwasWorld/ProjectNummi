package com.eywa.projectnummi.common

fun String.asCurrency() = "£$this"
fun Int.div100String() = "%.2f".format(this / 100.0)
