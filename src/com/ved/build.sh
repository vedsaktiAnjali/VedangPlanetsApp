#!/bin/bash
set -e

# Setup environment paths
export JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-aarch64
export PATH=$JAVA_HOME/bin:$PATH

JMODS=$JAVA_HOME/jmods
SRC_DIR=src
MODS_DIR=mods
BUILD_DIR=build
OUT_JAR=VedangPlanets.jar
MAIN_CLASS=com.ved.Main

# Clean old artifacts
echo "Cleaning old builds..."
rm -rf $MODS_DIR $BUILD_DIR image $OUT_JAR
mkdir -p $MODS_DIR $BUILD_DIR

echo "Compiling Java sources..."
javac \
  --module-path $JMODS \
  -d $MODS_DIR \
  $(find $SRC_DIR -name "*.java")
echo $MODS_DIR
echo "BEFORE COPPING"
echo "Copying resources into compiled modules directory..."
if [ -d "resources" ]; then
  cp -r resources/* $MODS_DIR/
fi

echo "--- Verifying contents of $MODS_DIR ---"
ls -la $MODS_DIR
echo "--------------------------------------"
# Detect actual module name defined inside module-info.java
MODULE_NAME=$(grep -E '^\s*module\s+' $SRC_DIR/com/ved/module-info.java | awk '{print $2}' | tr -d '{')
echo "Detected Module Name: $MODULE_NAME"

echo "Creating Modular JAR..."
jar --create \
  --file $OUT_JAR \
  --main-class=$MAIN_CLASS \
  -C $MODS_DIR .

echo "Building custom runtime image with jlink..."
jlink \
  --module-path $MODS_DIR:$JMODS \
  --add-modules $MODULE_NAME \
  --launcher launch=$MODULE_NAME/$MAIN_CLASS \
  --compress=2 \
  --no-header-files \
  --no-man-pages \
  --output image

echo ""
echo "=========================================="
echo "Build complete successfully!"
echo "Run using bundled runtime: ./image/bin/launch"
echo "Or direct java execution:   ./image/bin/java -m $MODULE_NAME/$MAIN_CLASS"
echo "=========================================="