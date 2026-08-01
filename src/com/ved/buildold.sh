#!/bin/bash
set -e

# Paths
JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-aarch64
JMODS=$JAVA_HOME/jmods
SRC=src
MODS=mods
OUT_JAR=VedangPlanets.jar
MAIN_CLASS=com.ved.Main
MODULE_NAME=vedang.planets

# Clean old builds
rm -rf $MODS image $OUT_JAR
mkdir -p $MODS

echo "Compiling sources..."
$JAVA_HOME/bin/javac \
  --module-path $JMODS \
  -d $MODS \
  $(find $SRC -name "*.java")

echo "Creating modular JAR..."
$JAVA_HOME/bin/jar --create \
  --file $OUT_JAR \
  --main-class=$MAIN_CLASS \
  -C $MODS . \
  -C resources .


echo "Running jlink..."
$JAVA_HOME/bin/jlink \
  --module-path $MODS:$JMODS \
  --add-modules $MODULE_NAME,javafx.base,javafx.controls,javafx.fxml,javafx.graphics \
  --output image


echo "Build complete. Run with:"
echo "./image/bin/java -m $MODULE_NAME/$MAIN_CLASS"
