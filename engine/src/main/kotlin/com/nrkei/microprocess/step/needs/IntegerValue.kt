/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED

// Understands a text-based choice
class IntegerValue(override val label: NeedLabel, private val expectedValue: Int): LabeledNeed {
    private var value: Int? = null

    override val state: NeedState get() = when(value) {
        null -> UNSATISFIED
        expectedValue -> SATISFIED
        else -> NeedState.PROBLEM
    }

    infix fun be(value: Int) { this.value = value }

    override fun currentValue() = value

    fun reset() { this.value = null }

    override fun clone() = IntegerValue(label, expectedValue).also { it.value = value }

    override fun equals(other: Any?) = this === other || other is IntegerValue && this.equals(other)

    private fun equals(other: IntegerValue) = this.label == other.label && this.value == other.value

    override fun hashCode() = value.hashCode() * 37 + label.hashCode()
}