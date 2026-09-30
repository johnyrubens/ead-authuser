# EAD-MICROSERVICES
Projeto back-end para estudos aplicado à micro-services

### micro-service : ead-authuser
## Tecnologias usadas
- Linguagem : Java
- Framework : Sprig Boot
- Base de dados : Postgresql 16
- Docker 

## Data base
- ead_authuser_db
### Tabelas
- tb_users

## Ambiente de desenvolvimento


Os bancos de dados dos microservices são executados em containers Docker.

O arquivo `docker-compose.yml` está localizado no diretório do microservice `ead/authuser` e configura os bancos de dados de ambos os serviços.

Para iniciar os bancos, execute no diretório `ead/authuser`:

```bash
docker compose up -d
```