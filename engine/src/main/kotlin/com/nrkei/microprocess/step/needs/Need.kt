/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

interface Need {
    val state: NeedState
}

enum class NeedState {
    UNSATISFIED, SATISFIED, PROBLEM
}

interface NeedLabel {
    val name: String
}

interface LabeledNeed: Need {
    val label: NeedLabel
}