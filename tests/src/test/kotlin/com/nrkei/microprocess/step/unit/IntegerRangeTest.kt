/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerNeed
import com.nrkei.microprocess.step.needs.NeedLabel
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.unit.IntegerRangeTest.TestLabel.I
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class IntegerRangeTest {

    @Test fun `integer range check`() {
        IntegerNeed.range(I, 18, 64).also { need ->
            assertEquals(UNSATISFIED, need.state)
            need be 16
            assertEquals(PROBLEM, need.state)
            need be 25
            assertEquals(SATISFIED, need.state)
            need.reset()
            assertEquals(UNSATISFIED, need.state)
        }
    }

    private enum class TestLabel: NeedLabel {
        I
    }
}