/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.*
import com.nrkei.microprocess.step.needs.NeedState.SATISFIED
import com.nrkei.microprocess.step.needs.NeedState.UNSATISFIED
import com.nrkei.microprocess.step.unit.StatusTest.TestLabels.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StatusTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed

    @BeforeEach fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
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

    private enum class TestLabels: NeedLabel {
        A, B
    }
}