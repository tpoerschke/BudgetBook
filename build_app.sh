#!/usr/bin/env bash
# -*- coding: utf-8 -*-

source .env
APP_VERSION="${APP_VERSION//BETA-/0.}"
echo ${APP_VERSION}

BUILD_DIR="./BUILD"


# -------- BASE METHODS --------

jpackage_win() {
  echo "starting JPackage..."
  jpackage --input target/input \
    --name "${APP_NAME}" \
    --icon "${icon}" \
    --main-jar "${MAIN_JAR}" \
    --app-version "${APP_VERSION}" \
    --dest "${BUILD_DIR}" \
    --type msi \
    --win-per-user-install
    --verbose
}

jpackage_linux_rpm() {
  echo "starting JPackage..."
  jpackage --input target/input \
    --name "${APP_NAME}" \
    --icon "${icon}" \
    --main-jar "${MAIN_JAR}" \
    --app-version "${APP_VERSION}" \
    --type msi \
    --verbose
}

clean_output_dir() {
  rm -rf $BUILD_DIR
}

run_maven_build() {
  mvn clean package || {
    echo "\n   (╯°□°）╯︵ ┻━┻\n"
    exit 1
  }
}

make_build_dir_and_copy_target() {
  mkdir target/input
  cp bb.application/target/$MAIN_JAR target/input/$MAIN_JAR
}

# -------- MAIN --------

main() {
  clean_output_dir
  # run_maven_build
  make_build_dir_and_copy_target


  if [[ "$OSTYPE" == "linux-gnu"* ]]; then
    # Linux
    icon="images/icon-linux.png"
    jpackage_linux_rpm
  elif [[ "$OSTYPE" == "darwin"* ]]; then
    # Mac OSX
    icon="images/mac-icon.icns"
  elif [[ "$OSTYPE" == "msys" ]]; then
    # Windows (MinGW / git bash)
    jpackage_win
  fi

  jpackage_status=$?
  if [ $jpackage_status -eq 0 ]; then
    exit 0
  fi

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

  exit 1

}

main