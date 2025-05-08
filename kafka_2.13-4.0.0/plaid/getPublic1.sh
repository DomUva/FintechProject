curl -X POST https://sandbox.plaid.com/sandbox/public_token/create \
  -H 'Content-Type: application/json' \
  -d '{
    "institution_id": "ins_109508",
    "initial_products": ["transactions"],
    "client_id": "67e438984ead690024065f1f",
    "secret": "426b1e079a5850f694123fcb5cbe82"
  }'
