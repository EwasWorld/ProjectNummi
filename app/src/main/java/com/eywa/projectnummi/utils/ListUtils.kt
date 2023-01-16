package com.eywa.projectnummi.utils

object ListUtils {
    fun <T> List<T?>.toggle(item: T?) = if (contains(item)) minus(item) else plus(item)
}
