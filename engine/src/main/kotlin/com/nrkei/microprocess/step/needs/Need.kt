/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.StandardRole.ANYONE

// Understands a choice
interface Need {
    val state: NeedState
    fun clone(): Need
    fun currentValue(): Any?
}

// Enumerates possible choice outcomes
enum class NeedState {
    UNSATISFIED, SATISFIED, PROBLEM
}

// Identifies a specific choice
interface NeedLabel {
    val name: String
    val role: Role get() = ANYONE
}

// A self-identifing choice
interface LabeledNeed: Need {
    val label: NeedLabel
}

// A participant in providing information
interface Role {
    val name: String
}

enum class StandardRole: Role {
    ANYONE
}