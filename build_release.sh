#!/usr/bin/env bash
# -*- coding: utf-8 -*-

read -r -p "Wurde die Release-Version bereits in der .env-Datei aktualisiert und commitet? (y/n) " answer
if [[ "$answer" != "y" ]];
  then echo "Release abgebrochen"
  exit 1
fi

read -r -p "Release wirklich durchführen? (y/n) " answer
if [[ "$answer" != "y" ]];
  then echo "Release abgebrochen"
  exit 1
fi

mvn release:prepare || {
  echo "Release abgebrochen"
  exit 1
}

rm release.properties
echo "Release-Vorbereitung abgeschlossen. Das Tag wurde gepusht und eine Github-Action wird das Release bauen."
