/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerValue
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.util.TestLabel.I
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class IntegerValueTest {

    @Test
    fun `integer value`() {
        IntegerValue(I, 25).also { need ->
            assertEquals(UNSATISFIED, need.state)
            need be 16
            assertEquals(PROBLEM, need.state)
            need be 25
            assertEquals(SATISFIED, need.state)
            need.reset()
            assertEquals(UNSATISFIED, need.state)
        }
    }
}