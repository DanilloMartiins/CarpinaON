# Plano B: Render + Supabase (provisório)

O **plano A (Oracle Cloud)** continua sendo o principal. Enquanto o cadastro na Oracle
está travado no ticket de suporte, o app roda aqui pra validar com o Flutter.

Quando o Oracle desbloquear: subir as VMs, desligar o Render/Supabase e apagar esta pasta.

## O que existe

| Arquivo | O quê |
|---------|-------|
| `../render.yaml` | Blueprint do Render (na raiz do repo, onde o Render procura) |

## Passo a passo pra subir

### 1. Supabase
- Projeto `CarpinaON` criado (região São Paulo)
- Usar a connection string do **Session pooler** (porta **5432**, IPv4 free):
  - O Transaction pooler (6543) é IPv6 por padrão e precisa de add-on pago no Render free
- Guardar: `SPRING_DATASOURCE_USERNAME` = `postgres.<project-ref>` e a senha do banco

### 2. Render
1. Dashboard → **New** → **Blueprint**
2. Conectar a conta GitHub → escolher o repo `CarpinaON`
3. O Render lê o `render.yaml` (na raiz) e cria o serviço `carpinaon-api`
4. No serviço criado → **Environment** → preencher os `sync: false`:
   - `SPRING_DATASOURCE_PASSWORD` = senha do Supabase
   - `JWT_SECRET` = chave forte (gerar: `openssl rand -base64 64`)
5. Deploy acontece automático (build do `Dockerfile`)
6. Testar: `https://<nome>.onrender.com/actuator/health` → 200
7. Testar auth: `POST /api/v1/auth/cadastrar` e `POST /api/v1/auth/login`

### 3. Entregar pro Flutter
- URL base pro Isael: `https://carpinaon-api.onrender.com/api/v1`

## Variáveis de ambiente (resumo)

| Variável | Valor |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres` |
| `SPRING_DATASOURCE_USERNAME` | `postgres.<project-ref>` |
| `SPRING_DATASOURCE_PASSWORD` | (dashboard) |
| `JWT_SECRET` | (dashboard) |
| `SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE` | `10` |

## Observações
- **Sem Redis**: Render free não tem; o código não usa cache de verdade ainda
- **Sem Prometheus/Grafana**: Render tem dashboard nativo (CPU, memória, logs)
- `server.port` já lê `PORT` do Render (env automática)