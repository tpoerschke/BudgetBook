#!/usr/bin/env bash
# -*- coding: utf-8 -*-

read -r -p "Wurde die Release-Version bereits in der .env aktualisiert und commitet? (y/n) " answer
if [[ "$answer" != "y" ]];
  then echo "Release-Vorbereitung abgebrochen"
  exit 1
fi

mvn release:prepare || {
  echo "Release-Vorbereitung abgebrochen"
  exit 1
}

echo "Release-Vorbereitung abgeschlossen. Beim nächsten Push wird eine Github-Action das Release abwickeln."
