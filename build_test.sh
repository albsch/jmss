#!/bin/bash

MODULES="jmss-api jmss-utils jmss-trail jmss-formula jmss-heuristics jmss-transition-system jmss-app"

mkdir -p build/classes/test build/resources/test

for module in $MODULES; do
  if [ -d "$module/src/test/java" ]; then
    echo "Compiling tests: $module"
    javac -d build/classes/test -cp "build/classes/main:lib/junit-4.12.jar" `find "$module/src/test/java" -name "*.java"`
  fi
  if [ -d "$module/src/test/resources" ]; then
    echo "Including resources: $module"
    cp -r "$module/src/test/resources/." "build/resources/test/"
  fi
done

echo "Creating jmss-test.jar..."
jar cf build/jar/jmss-test.jar -C build/classes/test . -C build/resources/test .
