path "secret/data/application" {
  capabilities = ["read"]
}

path "secret/data/sales-service" {
  capabilities = ["read"]
}

# path "auth/token/renew-self" {
#   capabilities = ["update"]
# }
#
# path "auth/token/lookup-self" {
#   capabilities = ["read"]
# }