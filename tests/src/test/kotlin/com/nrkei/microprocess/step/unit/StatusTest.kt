/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.unit.StatusTest.TestLabels.*
import com.nrkei.microprocess.step.needs.NeedLabel
import com.nrkei.microprocess.step.needs.NeedState
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValueNeed
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StatusTest {

    @Test fun `Status is Unsatisfied if any Need is Unsatisfied`() {
        Status().also { status ->
            status inject StringValueNeed(A)
            status inject StringValueNeed(B)
            assertEquals(NeedState.UNSATISFIED, status.state)
        }
    }

    private enum class TestLabels: NeedLabel {
        A, B
    }
}