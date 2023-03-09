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
    constructor(vararg steps: Step) : this(steps.toList())

    fun execute(status: Status, trace: Trace) {
        var snapshot: Status
        val maxCycleCount = steps.size + 2
        var cycleCount = 0
        do {
            cycleCount += 1
            trace registerPass cycleCount
            snapshot = status.snapshot()
            steps.forEach {
                trace.startStep(it, status)
                if (readyToExecute(it, status)) it execute status
                trace.endStep(status)
            }
            if (cycleCount > maxCycleCount) throw IllegalStateException("Status not converging; suspect unstable Step")
        } while (!(status diff snapshot).isEmpty())
    }

    private fun readyToExecute(step: Step, status: Status) =
        step.requiredLabels.all { it in status } &&
                step.forbiddenLabels.all { it !in status } &&
                step.validLabels.all { it in status && status[it].state == SATISFIED } &&
                step.requiredValues.all { it.key in status && status[it.key].currentValue() == it.value }
}