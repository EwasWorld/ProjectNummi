package com.eywa.projectnummi.utils

import kotlin.reflect.KClass

/**
 * A collection of items where only one instance of each class is possible
 */
class UniqueClassSet<E : Any> private constructor(
        private val map: Map<KClass<out E>, E>,
) {
    constructor() : this(emptyMap())

    fun plusOrReplace(item: E) = UniqueClassSet(map.plus(item::class to item))
    fun minus(item: KClass<out E>) = UniqueClassSet(map.minus(item))

    @Suppress("UNCHECKED_CAST")
    fun <T : E> get(item: KClass<T>): T? = map[item] as? T

    fun forEach(block: (E) -> Unit) {
        map.values.forEach(block)
    }
}
