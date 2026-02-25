
# SAP COMMERECE B2C POC

## Guia de configuração

### Versão - hybris v2211-JDK21

https://help.sap.com/docs/SAP_COMMERCE_CLOUD_PUBLIC_CLOUD/75d4c3895cb346008545900bffe851ce/236dcbe0ff5d4bd0bdf177b7f151cc66.html

### Antes de começar

1. Instale a versão 21 do Java
   https://sapmachine.io/docs/installation

2. Descompacte o Standard do SAPCommerce versão 2211, arquivo CCL2211J2100P_8-80009731.tar.gz localizado na pasta files do repositório

3. Na pasta onde baixou o projeto, sugestão /opt/poc, extrair o conteúdo do ZIP baixados no passo 2.

### Organizando o ambiente

Dentro da pasta installer, segue lista de commandos:

Criar local.properties e localextensions.xml:
./install.sh -r poc -A initAdminPassword=nimda setup

Rodar initialize - Inicializacao do ambiente:
./install.sh -r poc -A initAdminPassword=nimda initialize

Rodar updatesystem - Atualizacao do ambiente:
./install.sh -r poc -A initAdminPassword=nimda update

Rodar build do sistema:
./install.sh -r poc -A initAdminPassword=ama1relo buildSystem

Subir o servidor:
./install.sh -r poc -A initAdminPassword=nimda start

Descer o servidor:
./install.sh -r poc -A initAdminPassword=nimda stop

### Desenvolvimentos 

Voce pode executar tambem os comandos abaixo para suas atividades do dia a dia

Dentro da pasta hybris/bin/platoform executar os comandos

executar . ./setantenv.sh para qualquer um dos comandos abaixo 

Rodar initialize - Inicializacao do ambiente:
ant initialize

Rodar updatesystem - Atualizacao do ambiente:
ant updatesystem

Rodar build do sistema:
ant clean all

Subir o servidor:
./hybrisserver.sh
