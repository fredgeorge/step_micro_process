/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValue
import com.nrkei.microprocess.step.steps.Process
import com.nrkei.microprocess.step.steps.Trace
import com.nrkei.microprocess.step.util.EverChangingStep
import com.nrkei.microprocess.step.util.ExpansionStep
import com.nrkei.microprocess.step.util.NeedSetStep
import com.nrkei.microprocess.step.util.TestLabel.*
import com.nrkei.microprocess.step.util.ValidLabelsStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ProcessTest {
    private lateinit var stringNeedA: StringValue
    private lateinit var stringNeedB: StringValue
    private lateinit var stringNeedC: StringValue
    private lateinit var stringNeedD: StringValue
    private lateinit var status: Status
    private lateinit var trace: Trace

    @BeforeEach
    fun setup() {
        stringNeedA = StringValue(A)
        stringNeedB = StringValue(B)
        stringNeedC = StringValue(C)
        stringNeedD = StringValue(D)
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

    @Test
    fun `Step added dynamically`() {
        Process(ValidLabelsStep(I), ExpansionStep(ValidLabelsStep(J))).also { process ->
            Trace().also { trace ->
                assertEquals(2, process.size)
                process.execute(Status(), trace)
                assertEquals(3, process.size)
            }
        }
    }
}
