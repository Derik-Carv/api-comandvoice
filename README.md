# API ComandVoice 🚀

Este projeto é uma API desenvolvida em **Java 21** com o framework **Spring Boot 4**, projetada para atuar como um orquestrador de comandos de voz. A aplicação integra automações via **n8n** e centraliza o envio de logs estruturados (formato ECS) para o **Grafana Cloud Loki** através do coletor **Promtail**.

Toda a arquitetura foi desenhada utilizando o conceito de **Injeção de Configurações por Ambiente**, garantindo que nenhuma chave, token ou URL de produção fique exposta no histórico do código-fonte ou no GitHub.

---

# 🎯 Arquitetura de Configuração e Fluxo de Dados

O fluxo de dados e injeção de variáveis funciona da seguinte forma:

```text
[ Front-end / Cliente ]
        │
        └──(Porta 8080)──>

[ Docker Container ]
        │
        └──(Injeta .env)──>

[ Spring Boot API ]
        │
        └──(Gera application.log)
        ▼

[ Grafana Cloud Loki ]
        ▲
        │
[ Container Promtail ] <──(Lê Volume)─── [ Pasta /logs ]
```

1. **`src/main/resources/application.yaml`**: Mapeia as propriedades internas do Spring para variáveis do Sistema Operacional usando a sintaxe `${NOME_DA_VARIAVEL:valor_padrao}`.
2. **`docker-compose.yml`**: Lê as variáveis reais do seu arquivo local `.env` e as injeta dinamicamente dentro dos containers da API e do Promtail no momento da execução.

---

# 🛠️ Tecnologias e Pré-requisitos

Para rodar a stack completa na sua máquina local, você precisa apenas de:

1. **Git**: Para clonar e gerenciar o repositório.
2. **Docker** (versão 20.10+): Motor de containerização.
3. **Docker Compose** (versão 2.0+): Ferramenta para gerenciar o ecossistema multi-container.

> 💡 **Vantagem do Docker Multi-Stage:** Você **não precisa** ter o Java JDK ou o Maven instalados fisicamente na sua máquina. O arquivo `Dockerfile` realiza o build da aplicação de forma isolada dentro do container utilizando uma imagem oficial do Maven e exportando apenas o binário final leve para a execução.

---

# 🔑 Onde Adquirir as Chaves e Tokens?

A aplicação necessita de duas integrações externas configuradas no seu arquivo `.env` local:

## 1. URL do Webhook do n8n

- **O que é:** O endereço HTTP que receberá as cargas de dados processadas pela API.
- **Como obter:**
    1. Acesse o seu painel do **n8n** (self-hosted ou cloud).
    2. Crie ou abra o seu Workflow de comandos de voz.
    3. Adicione um nó do tipo **Webhook**, configure o método HTTP para `POST` e copie a **Webhook URL** gerada.

> Recomenda-se utilizar a URL de *Test* durante o desenvolvimento local.

---

## 2. URL do Grafana Loki (HTTP Basic Auth)

- **O que é:** O ponto de entrada seguro para onde o Promtail enviará os logs.
- **Como obter:**
    1. Acesse sua conta no Grafana Cloud.
    2. No painel principal do seu Workspace, localize o card do **Loki** e clique em **Details**.
    3. Copie o campo **URL**.
    4. Gere um **Access Policy Token** com permissão `logs:write`.
    5. Monte sua URL final do Loki utilizando autenticação HTTP Basic:

```text
https://<USER_ID>:<API_TOKEN>@<LOKI_URL_SEM_HTTP>/loki/api/v1/push
```

---

# 🚀 Como Executar o Projeto Localmente

## Passo 1: Clonar o Repositório

```bash
git clone git@github.com:Derik-Carv/api-comandvoice.git
cd api-comandvoice
```

---

## Passo 2: Configurar o Arquivo de Ambiente (.env)

Como o arquivo `.env` original contém seus tokens privados e é permanentemente ignorado pelo Git (via `.gitignore`), crie uma cópia local a partir do modelo de exemplo:

```bash
cp .env.example .env
```

Abra o arquivo `.env` gerado e preencha com as suas chaves reais:

```env
# URL completa do webhook configurado no seu painel do n8n
N8N_WEBHOOK_URL=https://n8n.seu-dominio.com/webhook/comand-voice

# URL de Push do Loki com as credenciais embutidas (User ID + API Token)
GRAFANA_LOKI_URL=https://123456:glc_TOKEN_AQUI@logs-prod-us-central1.grafana.net/loki/api/v1/push
```

---

## Passo 3: Inicializar a Stack com o Docker

Execute o comando abaixo para compilar a aplicação em Java 21 e subir os serviços da API e do coletor de logs em segundo plano:

```bash
docker compose up -d --build
```

---

# 📁 Estrutura do Arquivo `.env.example`

O arquivo de exemplo disponibilizado no repositório serve como guia público de quais variáveis a aplicação espera para rodar:

```env
# ==============================================================================
# CONFIGURAÇÕES DA API COMANDVOICE (EXEMPLO)
# ==============================================================================

# Insira a URL do webhook do n8n responsável por processar o comando recebido
N8N_WEBHOOK_URL=

# Insira a URL completa do endpoint Loki do seu Grafana Cloud com as credenciais
GRAFANA_LOKI_URL=
```

---

# 🛠️ Comandos Úteis de Manutenção (Cheat Sheet)

Use os comandos abaixo na pasta raiz do projeto para gerenciar e inspecionar a aplicação:

| Objetivo | Comando |
|---|---|
| Subir ou atualizar a stack inteira em background | `docker compose up -d --build` |
| Derrubar e parar todos os containers da aplicação | `docker compose down` |
| Visualizar logs da API Java em tempo real | `docker compose logs -f api-comandvoice` |
| Visualizar logs do Promtail (Coletor de logs) | `docker compose logs -f promtail` |
| Verificar o status e mapeamento de portas atual | `docker compose ps` |
| Inspecionar as variáveis de ambiente ativas no container | `docker exec -it api-comandvoice-container env` |
| Forçar limpeza de caches antigos de build do Docker | `docker builder prune -a` |

---

# 📂 Persistência de Logs Local

Ao rodar a aplicação, o Docker criará automaticamente uma pasta chamada `/logs` na raiz do seu projeto local através de volumes espelhados:

```text
./logs:/app/logs
```

O arquivo `application.log` gerado pelo Spring Boot será gravado nessa pasta, permitindo que o container do Promtail faça a leitura dele de forma assíncrona e segura em modo somente-leitura (`:ro`), garantindo a integridade dos dados antes do envio ao Grafana Cloud.
