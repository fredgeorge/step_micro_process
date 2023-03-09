/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.unit

import com.nrkei.microprocess.step.needs.IntegerNeed
import com.nrkei.microprocess.step.needs.Need
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValueNeed
import com.nrkei.microprocess.step.util.TestLabel.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SnapshotTest {
    private lateinit var stringNeedA: StringValueNeed
    private lateinit var stringNeedB: StringValueNeed
    private lateinit var integerNeedC: IntegerNeed
    private lateinit var current: Status
    private lateinit var snapshot: Status

    @BeforeEach
    fun setup() {
        stringNeedA = StringValueNeed(A)
        stringNeedB = StringValueNeed(B)
        integerNeedC = IntegerNeed.range(I, 18, 65)
        current = Status().also {
            it inject stringNeedA
            it inject integerNeedC
        }
        snapshot = current.snapshot()
    }

    @Test fun `No changes after snapshot`() {
        assertFalse((current diff snapshot).hasChanges())
    }

    @Test fun `Additions detected`() {
        current inject stringNeedB
        (current diff snapshot).also { changes ->
            assertEquals(listOf(stringNeedB), changes.additions)
            assertEquals(emptyList<Need>(), changes.deletions)
            assertEquals(emptyList<Status.Change>(), changes.changes)
        }
    }

    @Test fun `Deletions detected`() {
        current remove A
        (current diff snapshot).also { changes ->
            assertEquals(emptyList<Need>(), changes.additions)
            assertEquals(listOf(stringNeedA), changes.deletions)
            assertEquals(emptyList<Status.Change>(), changes.changes)
        }
    }

    @Test fun `Changes detected`() {
        stringNeedA be "A2"
        (current diff snapshot).also { changes ->
            assertEquals(emptyList<Need>(), changes.additions)
            assertEquals(emptyList<Need>(), changes.deletions)
            assertEquals(1, changes.changes.size)
            assertNull(changes.changes.first().originalValue)
            assertEquals("A2", changes.changes.first().currentValue)
        }
    }
}