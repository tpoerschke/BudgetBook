#!/usr/bin/env bash
# -*- coding: utf-8 -*-

# Hinweis und Abfrage, ob zuerst die Versionen in der .env aktualisiert und commitet wurden
# mvn release:prepare ausführen

read -r -p "Wurde die Release-Version bereits in der .env aktualisiert und commitet? (y/n) " answer
if [[ "$answer" != "y" ]];
  then echo "Release-Vorbereitung abgebrochen"
  exit 1
fi

mvn clean package || {
  echo "Release-Vorbereitung abgebrochen"
  exit 1
}

echo "Release-Vorbereitung abgeschlossen. Beim nächsten Push wird eine Github-Action das Release abwickeln"
