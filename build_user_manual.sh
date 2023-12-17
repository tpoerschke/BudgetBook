while read line; do
  key=$(echo $line | cut -d= -f1)
  key=${key//_/}
  value=$(echo $line | cut -d= -f2)
  echo "\\\newcommand{\\$key}{$value}"
done < .env > user_manual/env.tex

mkdir -p target/latex
cd user_manual
pdflatex -synctex=1 -interaction=nonstopmode -output-directory=../target/latex main.tex
pdflatex -synctex=1 -interaction=nonstopmode -output-directory=../target/latex main.tex