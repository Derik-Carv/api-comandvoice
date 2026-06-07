# API ComandVoice 🚀

`api-comandvoice` é uma API em **Java 21** com **Spring Boot 4** que atua como orquestrador de comandos de voz. A aplicação recebe payloads HTTP, encaminha para webhooks do **n8n** e envia logs estruturados em formato ECS para o **Grafana Cloud Loki** via **Promtail**.

---

# Visão Geral da Arquitetura

```text
[ Cliente / Front-end ]
        │
        ▼
[ api-comandvoice (Spring Boot) ]
        ├─ POST /api/comands
        │    └─ encaminha para n8n webhook principal
        ├─ POST /api/comands/receptionist
        │    └─ encaminha para n8n webhook receptionist
        └─ GET /api/stage
             └─ consulta dados de estágio no PostgreSQL

[ api-comandvoice ]
        └─ grava logs ECS em ./logs/application.log
                ▼
[ Promtail ]
        └─ envia logs para Grafana Cloud Loki
```

## O que está incluso

- endpoints HTTP para envio de comandos de voz
- integração com n8n via webhooks configuráveis
- persistência em PostgreSQL
- logs estruturados ECS escritos em um volume local
- coleta de logs com Promtail e envio para Loki

---

# Requisitos

- Docker 20.10+
- Docker Compose 2+
- Git

> Não é necessário ter Maven ou JDK instalados localmente. O build ocorre dentro do container Docker.

---

# Configuração de Ambiente

A aplicação usa variáveis de ambiente definidas em `src/main/resources/application.yaml`.

## Criar o arquivo `.env`

```bash
cp ".env .example" .env
```

## Variáveis obrigatórias

```env
N8N_WEBHOOK_URL="https://<seu-n8n>/webhook/<id>"
N8N_WEBHOOK_URL_RECEPTIONIST="https://<seu-n8n>/webhook/<id>"
GRAFANA_LOKI_URL="https://<USER_ID>:<API_TOKEN>@<seu-loki-host>/loki/api/v1/push"
DB_NAME=comandvoice_db
DB_USER=exampleuser
DB_PASSWORD=examplepassword
```

## Observação de segurança

Nunca adicione valores reais de secret keys, URLs de webhook ou credenciais ao Git.

O seu `.env` local deve permanecer privado e veio com dados reais no ambiente atual:
- `N8N_WEBHOOK_URL`
- `N8N_WEBHOOK_URL_RECEPTIONIST`
- `GRAFANA_LOKI_URL`
- `DB_PASSWORD`

---

# Como executar

```bash
docker compose up -d --build
```

### Parar a stack

```bash
docker compose down
```

---

# Endpoints da API

## POST `/api/comands`

Encaminha um comando de voz para o webhook principal do n8n.

### Exemplo de request

```json
{
  "comand": "ligar a luz",
  "system": "whatsapp",
  "plataform": "whatsapp"
}
```

## POST `/api/comands/receptionist`

Encaminha um payload de recepcionista para o webhook receptionist.

### Exemplo de request

```json
{
  "body": "solicitar suporte",
  "platform": "whatsapp",
  "system": "whatsapp"
}
```

## GET `/api/stage`

Retorna todos os registros de estágio armazenados no banco de dados.

---

# Configuração do Banco de Dados

O projeto usa PostgreSQL e inicializa o schema a partir de `src/main/resources/schema.sql`.

A configuração do Spring Boot em `application.yaml` define:

```yaml
spring:
  sql:
    init:
      mode: always
      separator: "^;"
```

---

# Serviços Docker

- `api-comandvoice`: build da API com Maven + Java 21
- `promtail`: coleta logs do diretório `./logs` e envia para Loki
- `db`: PostgreSQL 16

---

# Comandos úteis

| Objetivo | Comando |
|---|---|
| Subir a stack | `docker compose up -d --build` |
| Parar a stack | `docker compose down` |
| Ver logs da API | `docker compose logs -f api-comandvoice` |
| Ver logs do Promtail | `docker compose logs -f promtail` |
| Ver containers ativos | `docker compose ps` |
| Inspecionar ambiente | `docker exec -it api-comandvoice-container env` |
| Limpar cache Docker | `docker builder prune -a` |

---

# Arquivo `.env.example`

O modelo do repositório descreve as variáveis esperadas:

```env
# Configurações do n8n
N8N_WEBHOOK_URL="YOUR_URL_TEST_IN_N8N"
N8N_WEBHOOK_URL_RECEPTIONIST="YOUR_URL_TEST_IN_N8N_RECEPTIONIST"

# Configurações do Grafana / Promtail
GRAFANA_LOKI_URL='YOUR_URL_IN_GRAFANA_CLOUD'

# DATABASE
DB_NAME=exampledb
DB_USER=exampleuser
DB_PASSWORD=123
```

---

# Observações finais

- `application.yaml` usa `DB_PASSWORD` para a conexão com o PostgreSQL.
- `docker-compose.yml` define `DB_PASS=${DB_PASSWORD}` e `DB_PASSWORD=${DB_PASSWORD}`; a aplicação Spring usa `DB_PASSWORD`.
- O arquivo `AuthController.java` existe no código, mas atualmente não contém rotas ativas.

---

# Referências

- `Dockerfile`: build multi-stage
- `docker-compose.yml`: serviços e rede Docker
- `promtail-config.yml`: configuração de envio de logs para Loki
- `application.yaml`: configurações de datasource, JPA e n8n
