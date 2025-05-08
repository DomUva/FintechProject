curl -X POST http://localhost:8000/webhook \
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP",
  "account_owner": null,
  "amount": 19.99,
  "authorized_date": "2025-04-19",
  "authorized_datetime": null,
  "category": ["Food and Drink", "Fast Food"],
  "category_id": "13005000",
  "check_number": null,
  "counterparties": [],
  "date": "2025-04-19",
  "datetime": "2025-04-19T01:23:00Z",
  "iso_currency_code": "USD",
  "location": {
    "address": null,
    "city": "Boston",
    "country": "US",
    "lat": null,
    "lon": null,
    "postal_code": "02115",
    "region": "MA",
    "store_number": null
  },
  "logo_url": null,
  "merchant_entity_id": null,
  "merchant_name": "Taco Bell",
  "name": "Taco Bell",
  "payment_channel": "in store",
  "payment_meta": {},
  "pending": false,
  "pending_transaction_id": null,
  "personal_finance_category": {
    "confidence_level": "HIGH",
    "detailed": "FOOD_AND_DRINK_FAST_FOOD",
    "primary": "FOOD_AND_DRINK"
  },
  "personal_finance_category_icon_url": "https://plaid-category-icons.plaid.com/PFC_FOOD_AND_DRINK.png",
  "transaction_code": null,
  "transaction_id": "txn_datetime_violation_002",
  "transaction_type": "place",
  "unofficial_currency_code": null,
  "website": null
}'
