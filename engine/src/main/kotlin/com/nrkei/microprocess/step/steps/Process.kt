/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.steps

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.Status

// Understands work that needs to be completed
class Process(private val steps: MutableList<Step>) : MutableList<Step> by steps {
    constructor(vararg steps: Step) : this(steps.toList().toMutableList())

    fun execute(status: Status, trace: Trace) {
        var snapshot: Status
        var cycleCount = 0
        do {
            cycleCount += 1
            trace registerPass cycleCount
            snapshot = status.snapshot()
            val newSteps = mutableListOf<Step>()
            steps.forEach {
                trace.startStep(it, status)
                if (readyToExecute(it, status)) it.execute(status).also { newSteps.addAll(it) }
                trace.endStep(status)
            }
            if (newSteps.isNotEmpty()) {  // new Steps created
                steps.addAll(newSteps)
                cycleCount = 0
            }
            if (cycleCount > steps.size + 1) throw IllegalStateException("Status not converging; suspect unstable Step")
        } while ((status diff snapshot).hasChanges())
    }

    private fun readyToExecute(step: Step, status: Status) =
        step.requiredLabels.all { it in status } &&
                step.forbiddenLabels.all { it !in status } &&
                step.validLabels.all { it in status && status[it].state == SATISFIED } &&
                step.requiredValues.all { it.key in status && status[it.key].currentValue() == it.value }
}