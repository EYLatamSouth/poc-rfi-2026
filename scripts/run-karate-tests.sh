#!/bin/bash

# Verifica se o parâmetro env foi fornecido
if [ "$#" -ne 2 ]; then
  echo "Uso: $0 <env>"
  exit 1
fi

ENV="$1"
DIR="$2"
# Entra no diretório do projeto Karate

echo "$DIR"
cd "$DIR" || { echo "Diretório não encontrado"; exit 2; }

# Executa o comando mvn test com o parâmetro env
mvn test -Dkarate.env="$ENV" -Dtest=PocTestsRunner
