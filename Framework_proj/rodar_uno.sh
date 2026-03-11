#!/bin/bash
# Limpa o conflito de bibliotecas do Snap no Ubuntu
export LD_LIBRARY_PATH=""

# Executa o Java apontando para a pasta bin e a classe MainUno
/usr/lib/jvm/java-21-openjdk-amd64/bin/java -cp bin cartas.uno.MainUno
