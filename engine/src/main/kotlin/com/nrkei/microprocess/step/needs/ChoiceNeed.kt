/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.*

// Understands a multiple value choice
class ChoiceNeed (override val label: NeedLabel, private val validValues: List<String>) : LabeledNeed {

    constructor(label: NeedLabel, vararg validValues: String) : this(label, validValues.toList())

    private var value: String? = null

    override val state: NeedState get() = when(value) {
        null -> UNSATISFIED
        in validValues -> SATISFIED
        else -> PROBLEM
    }

    infix fun be(value: String) { this.value = value }

    fun reset() { value = null }

    override fun clone() = ChoiceNeed(label, validValues).also { it.value = value }

    override fun equals(other: Any?) = this === other || other is ChoiceNeed && this.equals(other)

    private fun equals(other: ChoiceNeed) =
        this.label == other.label && this.validValues == other.validValues && this.value == other.value

    override fun hashCode() = label.hashCode() * 37 + validValues.hashCode() * 43 + value.hashCode()
}