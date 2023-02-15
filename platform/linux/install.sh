#!/usr/bin/env bash
# -*- coding: utf-8 -*-
source .env
INSTALL_LOCATION="$HOME/$APP_NAME"
SHORTCUT_PATH="$HOME/.local/share/applications"
SHORTCUT_LOCATION="$HOME/.local/share/applications/$APP_NAME.desktop"
ICON_PATH="$INSTALL_LOCATION/lib/$APP_NAME.png"
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
