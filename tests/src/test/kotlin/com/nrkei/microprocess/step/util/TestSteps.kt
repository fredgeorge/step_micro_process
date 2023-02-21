/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.util

import com.nrkei.microprocess.step.needs.NeedLabel
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.steps.Step

internal enum class TestLabel: NeedLabel {
    A, B, I
}

internal class RequiredLabelsStep(vararg requiredLabels: NeedLabel): Step {
    override val requiredLabels = requiredLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status) {
        executionCount += 1
    }
}

internal class ForbiddenLabelsStep(vararg forbiddenLabels: NeedLabel): Step {
    override val forbiddenLabels = forbiddenLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status) {
        executionCount += 1
    }
}

internal class ValidLabelsStep(vararg validLabels: NeedLabel): Step {
    override val validLabels = validLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status) {
        executionCount += 1
    }
}

internal class ValuesStep(vararg values: Pair<NeedLabel, Any>): Step {
    override val requiredValues = values.toMap()

    internal var executionCount = 0

    override fun execute(status: Status) {
        executionCount += 1
    }
}
