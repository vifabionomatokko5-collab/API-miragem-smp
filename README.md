# Miragem API

API central da Miragem SMP.

## Requisitos

- Java 21+
- Maven 3.9+

## Rodar localmente

```bash
mvn spring-boot:run
```

ou:

```bash
mvn clean package
java -jar target/miragem-api-1.0.0.jar
```

## Endpoints iniciais

- `GET /api/v1/health`
- `GET /api/v1/server`
- `GET /actuator/health`

A API usa a porta `8080` por padrão. Em ambientes como Discloud, a variável `PORT` é respeitada.

## Próximas etapas

1. Banco PostgreSQL
2. Autenticação
3. API Keys para Minecraft
4. Integração com Discord
5. Jogadores e rankings
6. Creators e divulgadores
7. Loja e pedidos
8. Painel administrativo
