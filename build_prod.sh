#!/bin/bash

mkdir -p build/classes/main build/jar

# Define module compilation order based on dependencies
MODULES="jmss-api jmss-utils jmss-trail jmss-formula jmss-heuristics jmss-transition-system jmss-app"

for module in $MODULES; do
  echo "Compiling main sources: $module"
  javac -d build/classes/main -cp build/classes/main `find "$module/src/main/java" -name "*.java"`
done

jar cf build/jar/jmss.jar -C build/classes/main .
