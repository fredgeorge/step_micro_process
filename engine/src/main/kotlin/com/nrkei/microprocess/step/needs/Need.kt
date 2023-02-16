/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

// Understands a choice
interface Need {
    val state: NeedState
}

// Enumerates possible choice outcomes
enum class NeedState {
    UNSATISFIED, SATISFIED, PROBLEM
}

// Identifies a specific choice
interface NeedLabel {
    val name: String
}

// A self-identifing choice
interface LabeledNeed: Need {
    val label: NeedLabel
}