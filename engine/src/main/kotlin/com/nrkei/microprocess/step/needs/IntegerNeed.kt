/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.*

// Understands a choice within a range of values
class IntegerNeed private constructor(
    override val label: NeedLabel,
    private val minimum: Int,
    private val maximum: Int
) : LabeledNeed {

    companion object {
        fun range(label: NeedLabel, minimum: Int, maximum: Int) =
            IntegerNeed(label, minimum, maximum)

        fun positiveWithMax(label: NeedLabel, maximum: Int) =
            IntegerNeed(label, 1, maximum)

        fun positiveWithMin(label: NeedLabel, minimum: Int) =
            IntegerNeed(label, minimum, Int.MAX_VALUE)
    }
    private var value: Int? = null

    override val state: NeedState get() {
        return when(value) {
            null -> UNSATISFIED
            in (minimum..maximum) -> SATISFIED
            else -> PROBLEM
        }
    }

    infix fun be(value: Int) {
        this.value = value
    }

    fun reset() {
        value = null
    }

}