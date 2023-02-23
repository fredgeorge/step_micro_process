/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerNeed
import com.nrkei.microprocess.step.needs.NeedState
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValueNeed
import com.nrkei.microprocess.step.steps.Process
import com.nrkei.microprocess.step.util.NeedSetStep
import com.nrkei.microprocess.step.util.TestLabel
import com.nrkei.microprocess.step.util.TestLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ProcessTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var stringNeedC: StringValueNeed
    private lateinit var stringNeedD: StringValueNeed
    private lateinit var status: Status

    @BeforeEach
    fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
        stringNeedC = StringValueNeed(C)
        stringNeedD = StringValueNeed(D)
        status = Status().also { status ->
            status inject stringNeedA
        }
    }

    @Test
    fun `steps in order`() {
        Process(NeedSetStep(A, B), NeedSetStep(B, C), NeedSetStep(C, D)).also { process ->
            process execute status
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A1"
            process execute status
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue())
            process execute status
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue()) // Because of forbiddenLabels, D not updated!
        }
    }
}