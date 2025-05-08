curl -X POST http://localhost:8000/webhook \
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP",
  "account_owner": null,
  "amount": 620.75,
  "authorized_date": "2025-04-18",
  "authorized_datetime": null,
  "category": ["Travel", "Airlines and Aviation Services"],
  "category_id": "22001000",
  "check_number": null,
  "counterparties": [],
  "date": "2025-04-18",
  "datetime": null,
  "iso_currency_code": "EUR",
  "location": {
    "address": "1 Airport Blvd",
    "city": "Frankfurt",
    "country": "DE",
    "lat": null,
    "lon": null,
    "postal_code": "60549",
    "region": "HE",
    "store_number": null
  },
  "logo_url": null,
  "merchant_entity_id": null,
  "merchant_name": "Lufthansa",
  "name": "Lufthansa Flight Booking",
  "payment_channel": "in store",
  "payment_meta": {
    "by_order_of": null,
    "payee": null,
    "payer": null,
    "payment_method": null,
    "payment_processor": null,
    "ppd_id": null,
    "reason": null,
    "reference_number": null
  },
  "pending": false,
  "pending_transaction_id": null,
  "personal_finance_category": {
    "confidence_level": "LOW",
    "detailed": "TRAVEL_FLIGHTS",
    "primary": "TRAVEL"
  },
  "personal_finance_category_icon_url": "https://plaid-category-icons.plaid.com/PFC_TRAVEL.png",
  "transaction_code": null,
  "transaction_id": "TxFraudEur001",
  "transaction_type": "place",
  "unofficial_currency_code": null,
  "website": "https://www.lufthansa.com"
}'


