/*
 * Copyright (c) 2023 by Fred George
 * @author Fred George  fredgeorge@acm.org
 * Licensed under the MIT License; see LICENSE file in root.
 */

package com.nrkei.microprocess.step.steps

import com.nrkei.microprocess.step.needs.NeedLabel
import com.nrkei.microprocess.step.needs.Status

// Understands something that should be executed when feasible
interface Step {
    val requiredLabels: List<NeedLabel> get() = emptyList()
    val validLabels: List<NeedLabel> get() = emptyList()
    val requiredValues: Map<NeedLabel, Any> get() = emptyMap()
    val forbiddenLabels: List<NeedLabel> get() = emptyList()

    infix fun execute(status: Status)
}