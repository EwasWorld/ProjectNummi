package com.eywa.projectnummi.utils

import com.eywa.projectnummi.model.objects.Transaction

object ComparatorUtils {
    /**
     * Sorts transactions as follows (in order):
     * - Nulls first
     * - Latest [Transaction.date] first
     * - Higher [Transaction.order] first
     */
    val TRANSACTION_DATE_COMPARATOR: Comparator<Transaction?> = object : Comparator<Transaction?> {
        override fun compare(t0: Transaction?, t1: Transaction?): Int {
            if (t0 == null && t1 == null) return 0
            if (t1 == null) return 1
            if (t0 == null) return -1

            val descendingDateComparison = t1.date.compareTo(t0.date)
            if (descendingDateComparison != 0) return descendingDateComparison

            return t1.order.compareTo(t0.order)
        }
    }
}
