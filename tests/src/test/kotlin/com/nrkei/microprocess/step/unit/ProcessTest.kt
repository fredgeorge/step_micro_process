/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValueNeed
import com.nrkei.microprocess.step.steps.Process
import com.nrkei.microprocess.step.steps.Trace
import com.nrkei.microprocess.step.util.EverChangingStep
import com.nrkei.microprocess.step.util.NeedSetStep
import com.nrkei.microprocess.step.util.TestLabel.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProcessTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var stringNeedC: StringValueNeed
    private lateinit var stringNeedD: StringValueNeed
    private lateinit var status: Status
    private lateinit var trace: Trace

    @BeforeEach
    fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
        stringNeedC = StringValueNeed(C)
        stringNeedD = StringValueNeed(D)
        status = Status().also { status ->
            status inject stringNeedA
        }
        trace = Trace()
    }

    @Test
    fun `steps in order`() {
        Process(NeedSetStep(A, B), NeedSetStep(B, C), NeedSetStep(C, D)).also { process ->
            process.execute(status, trace)
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A1"
            process.execute(status, trace)
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue())
            process.execute(status, trace)
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue()) // Because of forbiddenLabels, D not updated!
        }
    }

    @Test
    fun `steps out of order`() {
        Process(NeedSetStep(C, D), NeedSetStep(B, C), NeedSetStep(A, B)).also { process ->
            process.execute(status, trace)
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A1"
            trace.reset()
            process.execute(status, trace)
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue())
            println(trace)
            trace.reset()
            process.execute(status, trace)
            assertEquals(SATISFIED, status.state)
            assertEquals("D1", status[D].currentValue()) // Because of forbiddenLabels, D not updated!
        }
    }

    @Test
    fun `infinite Step loop detected`() {
        Process(NeedSetStep(B, C), NeedSetStep(A, B), EverChangingStep(stringNeedD)).also { process ->
            status inject stringNeedD
            assertThrows<IllegalStateException> { process.execute(status, trace) }
        }
    }
}