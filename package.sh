#!/bin/bash
set -e

JAVA_HOME=/usr/lib/jvm/bellsoft-java17-full-aarch64
APP_NAME="vedang-planets"
APP_VERSION="1.0"
MAIN_CLASS="com.ved.Main"
MODULE_NAME="vedang.planets"
OUT_DIR="package"
ICON_PATH="resources/Logo/tht.png"

rm -rf $OUT_DIR
mkdir -p $OUT_DIR

echo "Building package with app icon..."

$JAVA_HOME/bin/jpackage \
  --type deb \
  --name $APP_NAME \
  --app-version $APP_VERSION \
  --module $MODULE_NAME/$MAIN_CLASS \
  --runtime-image image \
  --dest $OUT_DIR \
  --icon $ICON_PATH \
  --linux-shortcut \
  --linux-menu-group "Education"

echo "Package created inside $OUT_DIR with logo set!"