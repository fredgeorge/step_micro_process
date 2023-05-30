/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerRange
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.util.TestLabel.I
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class IntegerRangeTest {

    @Test
    fun `integer range check`() {
        IntegerRange.range(I, 18, 64).also { need ->
            assertEquals(UNSATISFIED, need.state)
            need be 16
            assertEquals(PROBLEM, need.state)
            need be 25
            assertEquals(SATISFIED, need.state)
            need.reset()
            assertEquals(UNSATISFIED, need.state)
        }
    }

    @Test
    fun `positive range with maximum`() {
        IntegerRange.positiveWithMax(I, 64).also { need ->
            assertEquals(UNSATISFIED, need.state)
            need be 70
            assertEquals(PROBLEM, need.state)
            need be 25
            assertEquals(SATISFIED, need.state)
            need be 0
            assertEquals(PROBLEM, need.state)
            need.reset()
            assertEquals(UNSATISFIED, need.state)

        }
    }

    @Test
    fun `positive range with minimum`() {
        IntegerRange.positiveWithMin(I, 18).also { need ->
            assertEquals(UNSATISFIED, need.state)
            need be 16
            assertEquals(PROBLEM, need.state)
            need be 25
            assertEquals(SATISFIED, need.state)
            need be 70
            assertEquals(SATISFIED, need.state)
            need.reset()
            assertEquals(UNSATISFIED, need.state)
        }
    }
}