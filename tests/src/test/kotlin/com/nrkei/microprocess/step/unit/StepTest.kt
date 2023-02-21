/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerNeed
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValueNeed
import com.nrkei.microprocess.step.steps.Process
import com.nrkei.microprocess.step.util.ForbiddenLabelsStep
import com.nrkei.microprocess.step.util.RequiredLabelsStep
import com.nrkei.microprocess.step.util.TestLabel.*
import com.nrkei.microprocess.step.util.ValidLabelsStep
import com.nrkei.microprocess.step.util.ValuesStep
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StepTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var integerNeedI: IntegerNeed
    private lateinit var status: Status

    @BeforeEach
    fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
        integerNeedI = IntegerNeed.range(I, 18, 65)
        status = Status().also { status ->
            status inject stringNeedA
        }
    }

    @Test fun `label exists`() {
        RequiredLabelsStep(A).also { step ->
            Process(step).also { process ->
                process execute status
                assertEquals(1, step.executionCount)
            }
        }
    }

    @Test fun `label initially missing`() {
        RequiredLabelsStep(A, B).also { step ->
            Process(step).also { process ->
                process execute status
                assertEquals(0, step.executionCount)
                status inject stringNeedB
                process execute status
                assertEquals(1, step.executionCount)
            }
        }
    }

    @Test fun `label initially invalid`() {
        ValidLabelsStep(I).also { step ->
            Process(step).also { process ->
                process execute status
                assertEquals(0, step.executionCount)
                status inject integerNeedI
                process execute status
                assertEquals(0, step.executionCount)  // Exists, but not set
                integerNeedI be 16
                process execute status
                assertEquals(0, step.executionCount) // Exists, but invalid
                integerNeedI be 22
                process execute status
                assertEquals(1, step.executionCount) // Exists and valid
                integerNeedI be 67
                process execute status
                assertEquals(1, step.executionCount) // Exists but invalid again
            }
        }
    }

    @Test fun `label must have specific value`() {
        ValuesStep(A to "A2").also { step ->
            Process(step).also { process ->
                process execute status
                assertEquals(0, step.executionCount) // No value yet
                stringNeedA be "A1"
                process execute status
                assertEquals(0, step.executionCount)  // Wrong value
                stringNeedA be "A2"
                process execute status
                assertEquals(1, step.executionCount)  // Correct value
                stringNeedA be "A3"
                process execute status
                assertEquals(1, step.executionCount)  // Wrong value again
            }
        }
    }

    @Test fun `label initially exists blocking Step`() {
        ForbiddenLabelsStep(A, B).also { step ->
            Process(step).also { process ->
                process execute status
                assertEquals(0, step.executionCount)
                status remove A
                process execute status
                assertEquals(1, step.executionCount)
                status inject stringNeedB
                process execute status
                assertEquals(1, step.executionCount) // Count does not increase
                status remove B
                process execute status
                assertEquals(2, step.executionCount)
            }
        }
    }
}