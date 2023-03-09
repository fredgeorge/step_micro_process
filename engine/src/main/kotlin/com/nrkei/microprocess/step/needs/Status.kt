/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.*

// Understands choices for a process
class Status internal constructor(private val needs: MutableMap<NeedLabel, Need>) {

    constructor() : this(mutableMapOf())

    infix fun inject(need: LabeledNeed) = needs.put(need.label, need)

    infix fun remove(label: NeedLabel) = needs.remove(label)

    val state: NeedState get() {
        if (needs.values.any { it.state == PROBLEM }) return PROBLEM
        if (needs.values.any { it.state == UNSATISFIED }) return UNSATISFIED
        return SATISFIED
    }

    operator fun get(label: NeedLabel) = needs[label]
        ?: throw IllegalArgumentException("Label ${label.name} is not currently in the Status")

    internal operator fun contains(label: NeedLabel) = needs.containsKey(label)

    fun snapshot() = Status(needs.map { it.key to it.value.clone() }.toMap().toMutableMap())

    infix fun diff(other: Status) = Changes().also { changes ->
        changes.additions = this.additionsTo(other)
        changes.deletions = other.additionsTo(this)
        changes.changes = valueDifferences(other)
    }

    private fun additionsTo(other: Status): List<Need> =
        (this.needs.keys - other.needs.keys).mapNotNull { this.needs[it] }

    private fun valueDifferences(other: Status): List<Change> =
        this.needs.keys
            .filter { other.needs.containsKey(it) }
            .filterNot { this.needs[it]?.currentValue() == other.needs[it]?.currentValue() }
            .map { Change(it, other.needs[it]?.currentValue(), this.needs[it]?.currentValue()) }

    inner class Changes internal constructor() {
        lateinit var additions: List<Need>
        lateinit var deletions: List<Need>
        lateinit var changes: List<Change>

        fun hasChanges() = additions.isNotEmpty() || deletions.isNotEmpty() || changes.isNotEmpty()
    }

    data class Change internal constructor(val label: NeedLabel, val originalValue: Any?, val currentValue: Any?)
}