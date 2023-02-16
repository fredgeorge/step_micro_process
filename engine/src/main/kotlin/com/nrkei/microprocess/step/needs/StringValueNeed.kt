/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED

// Understands a text-based choice
class StringValueNeed(override val label: NeedLabel): LabeledNeed {
    private var value: String? = null

    override val state get() = if (value.isNullOrEmpty()) UNSATISFIED else SATISFIED

    infix fun be(value: String) {
        this.value = value
    }

    fun reset() {
        this.value = null
    }

    override fun clone() = StringValueNeed(label).also { it.value = value }

    override fun equals(other: Any?) = this === other || other is StringValueNeed && this.equals(other)

    private fun equals(other: StringValueNeed) = this.label == other.label && this.value == other.value

    override fun hashCode() = value.hashCode() * 37 + label.hashCode()
}