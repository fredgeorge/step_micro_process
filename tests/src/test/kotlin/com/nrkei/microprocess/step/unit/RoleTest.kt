/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerRange
import com.nrkei.microprocess.step.needs.IntegerValue
import com.nrkei.microprocess.step.needs.NeedState.*
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.util.TestLabel.I
import com.nrkei.microprocess.step.util.TestLabel.J
import com.nrkei.microprocess.step.util.TestRole.PLAYER_1
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class RoleTest {
    private lateinit var amount: IntegerRange
    private lateinit var shortfall: IntegerValue

    @BeforeEach
    fun setup() {
        amount = IntegerRange.positiveWithMax(I, 100)
        shortfall = IntegerValue(J, 0)
    }

    @Test fun `remove Needs based on Role`() {
        Status().also { status ->
            status inject amount
            status inject shortfall
            assertEquals(UNSATISFIED, status.state)

            amount be 50
            shortfall be 20
            assertEquals(PROBLEM, status.state)

            status.keepOnly(PLAYER_1)
            assertEquals(SATISFIED, status.state)
            assertEquals(50, status[I].currentValue())
            assertThrows<IllegalArgumentException> { status[J] }
        }
    }
}