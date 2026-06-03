-- ====================================================================
--         FUNÇÃO AUTOMATIZADA PARA ATUALIZAÇÃO DE TIMESTAMPS
-- ====================================================================
CREATE OR REPLACE FUNCTION update_timestamp_column()
RETURNS TRIGGER AS $$
    BEGIN
        NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ language 'plpgsql'
^;

-- ====================================================================
--                        CRIAÇÃO DAS TABELAS
-- ====================================================================

-- 1. TIPOS DE STAGES (Dicionário de Referência estático)
CREATE TABLE IF NOT EXISTS stage_types (
    stage_key VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (stage_key)
)
^;

-- 2. CADASTRO DE CONTATOS (Informações Cadastrais do Usuário)
CREATE TABLE IF NOT EXISTS contacts (
    chat_id VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    push_name VARCHAR(150) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id)
)
^;

-- 3. ESTÁGIO ATUAL (Controle Dinâmico de Sessões do Chatbot)
CREATE TABLE IF NOT EXISTS whatsapp_stages (
    chat_id VARCHAR(255) NOT NULL,
    stage VARCHAR(100) NOT NULL DEFAULT '',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (chat_id),
    CONSTRAINT fk_stage_contact FOREIGN KEY (chat_id) REFERENCES contacts(chat_id) ON DELETE CASCADE
)
^;

-- 4. HISTORICO DE CONVERSAS (Logs de Trafego de Mensagens)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'message_direction') THEN
CREATE TYPE message_direction AS ENUM ('INBOUND', 'OUTBOUND');
END IF;
END $$
^;

CREATE TABLE IF NOT EXISTS chat_logs (
    id SERIAL NOT NULL,
    chat_id VARCHAR(255) NOT NULL,
    message_id VARCHAR(255) NOT NULL,
    direction message_direction NOT NULL,
    body TEXT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_log_contact FOREIGN KEY (chat_id) REFERENCES contacts(chat_id) ON DELETE CASCADE
)
^;

-- ====================================================================
-- 3. CRIACAO DOS TRIGGERS
-- ====================================================================

DROP TRIGGER IF EXISTS update_contacts_modtime ON contacts
^;
CREATE TRIGGER update_contacts_modtime
    BEFORE UPDATE ON contacts
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp_column()
^;

DROP TRIGGER IF EXISTS update_stages_modtime ON whatsapp_stages
^;
CREATE TRIGGER update_stages_modtime
    BEFORE UPDATE ON whatsapp_stages
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp_column()
^;

-- ====================================================================
-- 4. CARGA INICIAL DE DADOS (POPULAR OS STAGES DISPONÍVEIS)
-- ====================================================================
INSERT INTO stage_types (stage_key, description) VALUES
    ('', 'Menu Principal enviado. Aguardando escolha (01 ou 02)'),
    ('consultor', 'Fluxo de transbordo ou exibição de contatos oficiais'),
    ('menu_contratos', 'Sub-menu de contratos enviado. Aguardando escolha (01 ou 02)'),
    ('colher_codigo_cliente', 'Aguardando o usuário digitar o Código de Cliente'),
    ('colher_num_contrato', 'Aguardando o usuário digitar o Número do Contrato'),
    ('solicitar_contratacao_infos', 'Apresentação enviada. Aguardando o usuário digitar 1 para condições personalizadas')
ON CONFLICT (stage_key) DO UPDATE SET description = EXCLUDED.description
^;