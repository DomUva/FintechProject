curl -X POST http://localhost:8000/webhook \
	
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP",
  "account_owner": null,
  "amount": 150.00,
  "authorized_date": "2025-04-20",
  "authorized_datetime": null,
  "category": ["Entertainment", "Concerts"],
  "category_id": "18013000",
  "check_number": null,
  "counterparties": [],
  "date": "2025-04-20",
  "datetime": null,
  "iso_currency_code": "USD",
  "location": {
    "address": null,
    "city": "New York",
    "country": "US",
    "lat": null,
    "lon": null,
    "postal_code": "10001",
    "region": "NY",
    "store_number": null
  },
  "logo_url": null,
  "merchant_entity_id": null,
  "merchant_name": "Madison Square Garden",
  "name": "Madison Square Garden",
  "payment_channel": "in store",
  "payment_meta": {},
  "pending": false,
  "pending_transaction_id": null,
  "personal_finance_category": {
    "confidence_level": "MEDIUM",
    "detailed": "ENTERTAINMENT_CONCERTS",
    "primary": "ENTERTAINMENT"
  },
  "personal_finance_category_icon_url": "https://plaid-category-icons.plaid.com/PFC_ENTERTAINMENT.png",
  "transaction_code": null,
  "transaction_id": "txn_location_violation_003",
  "transaction_type": "place",
  "unofficial_currency_code": null,
  "website": null
}'

