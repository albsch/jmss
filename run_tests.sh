#!/bin/bash

set -eu

CLASSPATH="lib/hamcrest-core-1.3.jar:lib/hamcrest-library-1.3.jar:lib/junit-4.12.jar:build/classes/main:build/classes/test:build/resources/test"

# Other
java -cp "$CLASSPATH" org.junit.runner.JUnitCore jmss.heaps.VariableHeapIntTest
java -cp "$CLASSPATH" org.junit.runner.JUnitCore jmss.scores.ActivitiesVariableTest

# Highlevel tests
java -cp "$CLASSPATH" org.junit.runner.JUnitCore jmss.HighlevelTests

