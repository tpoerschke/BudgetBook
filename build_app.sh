mvn clean package || { echo "\n   (╯°□°）╯︵ ┻━┻\n"; exit 1; }

mkdir target/input
cp target/budget-book-1.0-SNAPSHOT.jar target/input/budget-book-1.0-SNAPSHOT.jar

jpackage --input target/input \
  --name JBudgetBook \
  --icon images/mac-icon.icns \
  --main-jar budget-book-1.0-SNAPSHOT.jar \
  --type app-image --verbose

jpackage_status=$?

echo "Clean up..."
rm -rf target/input

if [ $jpackage_status -eq 0 ]; then
    exit 0
fi
exit 1