/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.steps.Process
import com.nrkei.microprocess.step.needs.StringValueNeed
import com.nrkei.microprocess.step.util.ForbiddenLabelsStep
import com.nrkei.microprocess.step.util.RequiredLabelsStep
import com.nrkei.microprocess.step.util.TestLabel.A
import com.nrkei.microprocess.step.util.TestLabel.B
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class StepTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var status: Status

    @BeforeEach
    fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
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