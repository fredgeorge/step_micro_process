/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.*
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED
import com.nrkei.microprocess.step.unit.StatusTest.TestLabels.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StatusTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var integerNeedC: IntegerNeed

    @BeforeEach fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
        integerNeedC = IntegerNeed.range(C, 18, 65)
    }

    @Test fun `Status is Unsatisfied if any Need is Unsatisfied`() {
        Status().also { status ->
            status inject stringNeedA
            status inject stringNeedB
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A"
            assertEquals(UNSATISFIED, status.state)
            stringNeedB be "B"
            assertEquals(SATISFIED, status.state)
            stringNeedA.reset()
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A"
            assertEquals(SATISFIED, status.state)
            stringNeedB be ""
            assertEquals(UNSATISFIED, status.state)
        }
    }

    @Test fun `Status is Problem if any Need is Problem`() {
        Status().also { status ->
            status inject stringNeedA
            status inject integerNeedC
            assertEquals(UNSATISFIED, status.state)
            stringNeedA be "A1"
            assertEquals(UNSATISFIED, status.state)
            integerNeedC be 25
            assertEquals(SATISFIED, status.state)
            stringNeedA.reset()
            assertEquals(UNSATISFIED, status.state)
            integerNeedC be 16
            assertEquals(PROBLEM, status.state)
            stringNeedB be "A2"
            assertEquals(PROBLEM, status.state)
            integerNeedC.reset()
            assertEquals(UNSATISFIED, status.state)
        }
    }

    private enum class TestLabels: NeedLabel {
        A, B, C
    }
}