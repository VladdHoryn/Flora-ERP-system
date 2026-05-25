#!/bin/sh
set -e

export VAULT_ADDR=${VAULT_ADDR:-http://vault:8200}
export VAULT_TOKEN=${VAULT_TOKEN:-dev-root-token}

echo "Seed vault secrets"

echo "🔐 Seeding Vault secrets..."

vault secrets list | grep "secret/" || vault secrets enable -path=secret kv-v2

# -------- GLOBAL (shared) --------
vault kv put secret/application/dev \
  spring.rabbitmq.username=guest \
  spring.rabbitmq.password=guest

vault kv put secret/application/prod \
  spring.rabbitmq.username=guest \
  spring.rabbitmq.password=guest

# -------- PRODUCTION SERVICE --------
vault kv put secret/production-service/dev \
  spring.datasource.url=jdbc:mariadb://production-db:3306/production_service_db \
  spring.datasource.username=root \
  spring.datasource.password=12345

vault kv put secret/production-service/prod \
  spring.datasource.url=jdbc:mariadb://production-db:3306/production_service_db \
  spring.datasource.username=root \
  spring.datasource.password=12345

# (optional fallback без profile)
vault kv put secret/production-service \
  spring.datasource.url=jdbc:mariadb://production-db:3306/production_service_db \
  spring.datasource.username=root \
  spring.datasource.password=12345

# -------- INVENTORY SERVICE --------
vault kv put secret/inventory-service/dev \
  spring.datasource.password=12345

# -------- SALES SERVICE --------
vault kv put secret/sales-service/dev \
  spring.datasource.password=12345

vault kv put secret/api-gateway \
  spring.security.oauth2.resourceserver.jwt.secret-key=my-secret-key-which-should-be-veeeeeeery-long

for svc in book-service borrowing-service notification-service api-gateway; do
  vault policy write "${svc}" "/vault/init/policies/${svc}.hcl"
  echo "  policy '${svc}' written"
done

# generate app roles for each service

# vault auth enable approle
#
# for svc in book-service borrowing-service notification-service api-gateway; do
#   vault write "auth/approle/role/${svc}" \
#     token_policies="${svc}" \
#     token_ttl=1h \
#     token_max_ttl=4h \
#     secret_id_ttl=720h \
#     secret_id_num_uses=0
# done

# generate secret-ids, (cannot read secrets)
# vault policy write ci-deployer /vault/init/policies/ci-deployer.hcl

# CI/CD auth via GitHub Actions JWT
#
# vault auth enable jwt
#
# vault write auth/jwt/config \
#   bound_issuer="https://token.actions.githubusercontent.com" \
#   oidc_discovery_url="https://token.actions.githubusercontent.com"
#
# vault write auth/jwt/role/ci-deployer \
#   role_type=jwt \
#   bound_audiences="https://github.com/<org>" \
#   bound_claims_type=glob \
#   bound_claims="/repository=<org>/librabryMicro" \
#   user_claim=repository \
#   token_policies=ci-deployer \
#   token_ttl=10m



# audit logging
# vault audit enable file file_path=/vault/logs/audit.log



# docker exec -e VAULT_TOKEN=dev-root-token vault vault kv get secret/application