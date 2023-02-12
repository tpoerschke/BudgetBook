#!/usr/bin/env bash
# -*- coding: utf-8 -*-

source .env

rm -rf $APP_NAME
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

mkdir target/input
cp target/budget-book-1.0-SNAPSHOT.jar target/input/budget-book-1.0-SNAPSHOT.jar

jpackage --input target/input \
  --name $APP_NAME \
  --icon $icon \
  --main-jar budget-book-1.0-SNAPSHOT.jar \
  --type app-image --verbose

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
