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

    infix fun registerPass(passNumber: Int) { log.append("Pass $passNumber\n") }

    override fun toString() = log.toString()

    fun reset() { log.clear() }

    infix fun log(changes: Status.Changes) {
        log.append("\tChanges were${if (changes.isEmpty()) " not" else ""} made\n")
    }
}