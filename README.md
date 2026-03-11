# SAP COMMERCE B2C POC 🚀

## Índice 🧭

- [Guia de configuração 📘](#guia-de-configuração-)
  - [Versão - hybris v2211-JDK21 🧩](#versão---hybris-v2211-jdk21-)
  - [Antes de começar ✅](#antes-de-começar-)
  - [Organizando o ambiente 🛠️](#organizando-o-ambiente-)
  - [Desenvolvimento 👩‍💻](#desenvolvimento-)
  - [Testes automatizados com Karate 🧪](#testes-automatizados-com-karate-)

## Guia de configuração 📘

### Versão - hybris v2211-JDK21 🧩

https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/75d4c3895cb346008545900bffe851ce/236dcbe0ff5d4bd0bdf177b7f151cc66.html

### Antes de começar ✅

1. Instale a versão 21 do Java
   https://sapmachine.io/docs/installation

2. Descompacte o Standard do SAP Commerce versão 2211, arquivo CCL2211J2100P_8-80009731.zip do projeto

3. Na pasta onde baixou o projeto, sugestão /opt/poc, extraia o conteúdo do ZIP baixado no passo 2.

### Organizando o ambiente 🛠️

Dentro da pasta installer, após clonar o projeto e mesclá-lo ao conteúdo descompactado do ZIP, segue a lista de comandos:

Criar local.properties e localextensions.xml:
./install.sh -r poc -A initAdminPassword=nimda setup

Rodar initialize - Inicialização do ambiente 🧱:
./install.sh -r poc -A initAdminPassword=nimda initialize

Rodar updatesystem - Atualização do ambiente 🔄:
./install.sh -r poc -A initAdminPassword=nimda update

Rodar build do sistema 🏗️:
./install.sh -r poc -A initAdminPassword=ama1relo buildSystem

Subir o servidor 🚀:
./install.sh -r poc -A initAdminPassword=nimda start

Descer o servidor 🛑:
./install.sh -r poc -A initAdminPassword=nimda stop

### Desenvolvimento 👩‍💻

Você também pode executar os comandos abaixo para suas atividades do dia a dia.

Dentro da pasta hybris/bin/platform, execute os comandos:

Execute . ./setantenv.sh antes de qualquer um dos comandos abaixo

Rodar initialize - Inicialização do ambiente 🧱:
ant initialize

Rodar updatesystem - Atualização do ambiente 🔄:
ant updatesystem

Rodar build do sistema 🏗️:
ant clean all

Subir o servidor 🚀:
./hybrisserver.sh

## Testes automatizados com Karate 🧪

### Instalação do maven
Para rodar os testes automatizados, é necessário ter a versão 3.8.7 ou superior do maven instalado na máquina. Para instalar, utilize os comandos:

1- sudo apt update

2- sudo apt install maven

### Execução dos testes
Com o servidor rodando, você pode executar os testes automatizados com o comando abaixo, dentro da pasta hybris/bin/platform

1- . ./setantenv.sh

2- ant karatetest -Denv={ENVIRONMENT} (onde {ENVIRONMENT} refere-se ao ambiente onde os testes serão executados, exemplo: local)
