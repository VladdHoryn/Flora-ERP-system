TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjMiLCJyb2xlcyI6WyJBRE1JTiJdfQ.YSkrRUOtMNzOq9VSvu1_f2ypYJx-GxKlL9UMUDDubVM"

for i in {1..100}; do
  curl -s -o /dev/null -w "%{http_code}\n" \
    -H "Authorization: Bearer $TOKEN" \
    http://localhost:8085/api/v1/order-details/1 &
done
wait

echo "Done!"
read -p "Press enter to exit"