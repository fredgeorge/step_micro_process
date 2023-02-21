/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.steps

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.Status

// Understands work that needs to be completed
class Process(private val steps: List<Step>) {
    constructor(vararg steps: Step): this(steps.toList())

    infix fun execute(status: Status) {
        steps.forEach {
            if (readyToExecute(it, status)) it execute status
        }
    }

    private fun readyToExecute(step: Step, status: Status) =
        step.requiredLabels.all { it in status } &&
                step.forbiddenLabels.all { it !in status } &&
                step.validLabels.all { it in status && status[it].state == SATISFIED }
}