#!/usr/bin/env bash
# -*- coding: utf-8 -*-

source .env
LIB_DIR="lib/"

mvn clean package || {
  echo "\n   (╯°□°）╯︵ ┻━┻\n"
  exit 1
}

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
  # Linux
  icon="images/icon-linux.png"
elif [[ "$OSTYPE" == "darwin"* ]]; then
  # Mac OSX
  icon="images/mac-icon.icns"
elif [[ "$OSTYPE" == "msys" ]]; then
  echo # Windows (MinGW / git bash)
fi

mkdir target/input/
mkdir target/input/$LIB_DIR
cp bb.application/target/$MAIN_JAR target/input/$MAIN_JAR
cp bb.application/target/$LIB_DIR* target/input/$LIB_DIR

jpackage --input target/input \
  --name $APP_NAME \
  --icon $icon \
  --app-version 1.0.0 \
  --main-jar $MAIN_JAR \
  --java-options "--module-path \$APPDIR/$LIB_DIR" \
  --java-options "--add-modules=javafx.controls,javafx.fxml,javafx.swing" \
  --java-options "--add-reads=org.xerial.sqlitejdbc=ALL-UNNAMED" \
  --type app-image \
  --verbose

jpackage_status=$?

if [[ "$OSTYPE" == "linux-gnu"* ]]; then
  cp "platform/linux/install.sh" $APP_NAME/
  cp ".env" $APP_NAME/
  tar_name="$APP_NAME.tar.gz"
  rm -rf $tar_name
  tar -czf $tar_name $APP_NAME
elif [[ "$OSTYPE" == "darwin"* ]]; then
  echo
elif [[ "$OSTYPE" == "msys" ]]; then
  echo
fi

echo "Clean up..."
rm -rf target/input

if [ $jpackage_status -eq 0 ]; then
  exit 0
fi
exit 1
