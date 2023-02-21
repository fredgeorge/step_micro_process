/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.ChoiceNeed
import com.nrkei.microprocess.step.needs.NeedState
import com.nrkei.microprocess.step.util.TestLabel.A
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ChoiceNeedTest {

    @Test fun `valid choice made`() {
        ChoiceNeed(A, "A1", "A2", "A3").also { need ->
            assertEquals(NeedState.UNSATISFIED, need.state)
            need be "A1"
            assertEquals(NeedState.SATISFIED, need.state)
            need be "B1"
            assertEquals(NeedState.PROBLEM, need.state)
            need be "A2"
            assertEquals(NeedState.SATISFIED, need.state)
            need.reset()
            assertEquals(NeedState.UNSATISFIED, need.state)
        }
    }
}