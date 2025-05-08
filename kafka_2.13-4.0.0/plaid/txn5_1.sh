curl -X POST http://localhost:8000/webhook \
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP",
  "account_owner": null,
  "amount": 1500.00,
  "authorized_date": "2025-04-18",
  "authorized_datetime": null,
  "category": ["Shops", "Clothing and Accessories"],
  "category_id": "19012000",
  "check_number": null,
  "counterparties": [],
  "date": "2025-04-18",
  "datetime": null,
  "iso_currency_code": "EUR",
  "location": {
    "address": "Via Monte Napoleone, 5",
    "city": "Milan",
    "country": "IT",
    "lat": null,
    "lon": null,
    "postal_code": "20121",
    "region": "Lombardy",
    "store_number": null
  },
  "logo_url": null,
  "merchant_entity_id": null,
  "merchant_name": "Gucci",
  "name": "Gucci Online",
  "payment_channel": "online",
  "payment_meta": {
    "by_order_of": null,
    "payee": null,
    "payer": null,
    "payment_method": "credit card",
    "payment_processor": "Adyen",
    "ppd_id": null,
    "reason": null,
    "reference_number": "GUCCI-EU-20250418-789456"
  },
  "pending": false,
  "pending_transaction_id": null,
  "personal_finance_category": {
    "confidence_level": "HIGH",
    "detailed": "SHOPPING_CLOTHING_AND_ACCESSORIES",
    "primary": "SHOPPING"
  },
  "personal_finance_category_icon_url": "https://plaid-category-icons.plaid.com/PFC_SHOPPING.png",
  "transaction_code": null,
  "transaction_id": "TxGucciEUR1500Online",
  "transaction_type": "digital",
  "unofficial_currency_code": null,
  "website": "https://www.gucci.com"
}'
