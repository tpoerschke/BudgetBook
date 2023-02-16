#!/usr/bin/env bash
# -*- coding: utf-8 -*-
source .env
INSTALL_LOCATION="$HOME/$APP_NAME"
SHORTCUT_PATH="$HOME/.local/share/applications"
SHORTCUT_LOCATION="$HOME/.local/share/applications/$APP_NAME.desktop"
ICON_PATH="$INSTALL_LOCATION/lib/$APP_NAME.png"

while test $# -gt 0; do
    case "$1" in
    -h | --help)
        echo "Script for installing/removing $APP_NAME"
        echo " "
        echo "Usage: ./install.sh [options] [command]"
        echo " "
        echo "options:"
        echo "-h, --help        show this brief help"
        echo " "
        echo "commands:"
        echo "install           install $APP_NAME"
        echo "remove            remove $APP_NAME from this system"
        exit 0
        ;;
    install)
        install
        ;;
    remove)
        remove
        ;;
    esac
done

install() {
    rm -rf "$SHORTCUT_LOCATION"
    mkdir -p "$SHORTCUT_PATH"
    mkdir -p $INSTALL_LOCATION

    cp -r "bin" "$INSTALL_LOCATION"
    cp -r "lib" "$INSTALL_LOCATION"

    dot_desktop=(
        "[Desktop Entry]"
        "Type=Application"
        "Name=$APP_NAME"
        "Icon=$ICON_PATH"
        "Exec=$INSTALL_LOCATION/bin/$APP_NAME"
        "Categories=Office"
    )
    printf "%s\n" "${dot_desktop[@]}" >"$SHORTCUT_LOCATION"

    chmod +x "$SHORTCUT_LOCATION"

    xdg-desktop-menu forceupdate
}

remove() {
    echo "Will remove $APP_NAME from this system"
}
