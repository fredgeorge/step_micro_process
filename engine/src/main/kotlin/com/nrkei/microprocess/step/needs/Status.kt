/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.needs

import com.nrkei.microprocess.step.needs.NeedState.*

// Understands
class Status internal constructor(private val needs: MutableMap<NeedLabel, Need>) {

    constructor() : this(mutableMapOf())

    infix fun inject(need: LabeledNeed) = needs.put(need.label, need)

    val state: NeedState get() {
        if (needs.values.any { it.state == UNSATISFIED }) return UNSATISFIED
        return SATISFIED
    }
}