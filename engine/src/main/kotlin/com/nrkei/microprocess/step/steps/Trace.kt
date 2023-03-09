/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.steps

import com.nrkei.microprocess.step.needs.Status

// Understands the sequence and consequences of Steps
class Trace{
    private val log = StringBuilder()
    private lateinit var currentStep: Step
    private lateinit var initialStatus: Status

    internal infix fun registerPass(passNumber: Int) { log.append("Pass $passNumber\n") }

    override fun toString() = log.toString()

    fun reset() { log.clear() }

    internal fun startStep(step: Step, status: Status) {
        currentStep = step
        initialStatus = status.snapshot()
    }

    internal infix fun endStep(status: Status) {
        log.append("\t\tStep <${currentStep.name}> made ")
        log.append(if (status.diff(initialStatus).isEmpty()) "no changes" else "changes")
        log.append("\n")
    }
}