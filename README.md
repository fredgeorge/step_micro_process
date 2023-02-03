### step_micro_process

Copyright (c) 2023 by Fred George  
author: Fred George  fredgeorge@acm.org  
Licensed under the MIT License; see LICENSE file in root.

## Step MicroProcess Framework

This framework assists in modeling processes that are 
somewhat order independent. That is, while all of the 
Steps must run successfully, the order in which they 
run is not fixed. This is useful in processes which 
are often associated with checklists

### Installation

Kotlin is relatively easy to setup with IntelliJ IDEA. 
Gradle is used for building and testing the project, and is a 
prerequisite. Install if necessary.
The following instructions are for installing the code 
in IntelliJ 2022 by JetBrains. 
Adapt as necessary for your environment.

Note: This implementation was setup to use:

- IntelliJ 2022.3.2 (Ultimate Edition)
- Kotlin 1.8.0
- Java SDK 19 (Oracle)
- Gradle 7.6
- JUnit 5.9.2 for testing

Open the reference code:

- Download the source code from github.com/fredgeorge
    - Clone, or pull and extract the zip
- Open IntelliJ
- Choose "Open" (it's a Gradle project)
- Navigate to the reference code root, and enter

Source and test directories should already be tagged as such,
with test directories in green.

Confirm that everything builds correctly (and necessary 
libraries exist). There is a sample class, Rectangle, with 
a corresponding test, RectangleTest. The test should run 
successfully from the Gradle __test__ task.

### Design and Implementation

There are two fundamental concepts modeled:

- __Step:__ Something that must be done, and
- __Status:__ The current state of the process.

#### Status

We'll start with Status first. Status consists of a set of 
information _Needs_. Each Need could be in one of three
states:

- Has no value yet. We refer to this as _Unsatisfied_,
something that requires a value to complete the process.
- Has an invalid value. We refer to this as a _Problem_, 
something that requires correction to complete the process.
- Has a valid value. We refer to this as _Satisified_.

A Need may get a value from a Step, or it may be provided
by external sources (an application, for example).

#### Step

Each Step manipulates one or more Needs. It may optionally 
specify requirements prior to executing:

- Requirements: Satisified Needs that must exist prior 
to execution, and
- Inhibitors: Needs that should not exist yet. Inhibitors can
keep a Step from continually running when it has already run successfully.

#### Order Independent Flow

While Steps may be completely independent of each other, a
Step may not be able to run until some other Step has satisfied
some specific Need the Step requires. This is managed by specifying
the Requirements (and Inhibitors) for the Step. Thus when asked to
execute, the Step may decline due to unmet Requirements.

Steps can be processed in any order. The hypervisor will continually
execute Steps in a loop until nothing in Status changes. At this 
point, a steady state has been reached, and any further execution 
is pointless. 

So if Step A needs information from Step B, but Step
A runs before Step B, the Steps will be processed twice. In 
the first pass, Step A can't run since a Need is Unsatisfied.
In that same pass, however, Step B does run, and updates the
prerequisite Need. The Status has changed, and another pass 
will occur. Now Step A has the information necessary, and can 
execute.

Once the Status has reached a steady state (ie, no Step has 
changed anything during a pass), that ending Status at the 
end of the process determines the next action.
If all the Needs have been Satisified, the process is complete. If
any Need is now a Problem, the process will not complete until a
correction has been made, or the process abandoned.

If any Need is Unsatisfied, the process must wait for additional
outside input, or may simply need to be re-attempted when whatever
blocked the process has been resolved. In general, a Problem Need 
is reset to Unsatisifed prior to restarting the Step processing.
