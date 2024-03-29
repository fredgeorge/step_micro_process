/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.util

import com.nrkei.microprocess.step.needs.NeedLabel
import com.nrkei.microprocess.step.needs.Role
import com.nrkei.microprocess.step.needs.Status
import com.nrkei.microprocess.step.needs.StringValue
import com.nrkei.microprocess.step.steps.Step
import com.nrkei.microprocess.step.util.TestLabel.EXPAND
import com.nrkei.microprocess.step.util.TestRole.*

internal enum class TestLabel(override val role: Role) : NeedLabel {
    A(PLAYER_1), B(PLAYER_2), C(PLAYER_3), D(PLAYER_1),
    I(PLAYER_1), J(PLAYER_2),
    EXPAND(PLAYER_1)
}

internal enum class TestRole: Role {
    PLAYER_1, PLAYER_2, PLAYER_3
}

internal class RequiredLabelsStep(vararg requiredLabels: NeedLabel) : Step {
    override val requiredLabels = requiredLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        return emptyList()
    }
}

internal class ForbiddenLabelsStep(vararg forbiddenLabels: NeedLabel) : Step {
    override val forbiddenLabels = forbiddenLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        return emptyList()
    }
}

internal class ValidLabelsStep(vararg validLabels: NeedLabel) : Step {
    override val validLabels = validLabels.toList()

    internal var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        return emptyList()
    }
}

internal class ValuesStep(vararg values: Pair<NeedLabel, Any>) : Step {
    override val requiredValues = values.toMap()

    internal var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        return emptyList()
    }
}

internal class NeedSetStep(
    requiredLabel: NeedLabel,
    private val forbiddenLabel: NeedLabel
) : Step {
    override val validLabels = listOf(requiredLabel)
    override val forbiddenLabels = listOf(forbiddenLabel)
    override val name = "Require ${requiredLabel.name} to set ${forbiddenLabel.name}"

    private var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        status inject StringValue(forbiddenLabel).also { need -> need be "${forbiddenLabel.name}$executionCount" }
        return emptyList()
    }
}

internal class EverChangingStep(
    private val need: StringValue
) : Step {
    private var executionCount = 0

    override fun execute(status: Status): List<Step> {
        executionCount += 1
        need be "${need.label.name}$executionCount"
        return emptyList()
    }
}

internal class ExpansionStep(private val injectedStep: Step) : Step {
    private var executionCount = 0

    override val forbiddenLabels = listOf(EXPAND)

    override fun execute(status: Status): List<Step> {
        status inject StringValue(EXPAND)
        executionCount += 1
        return listOf(injectedStep)
    }
}
