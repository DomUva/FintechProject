curl -X POST http://localhost:8000/webhook \
  -H 'Content-Type: application/json' \
  -d '{
  "account_id": "ojBrKa8q3ACaogpzkGAriEq5Mjzj73tpjALwP",
  "account_owner": null,
  "amount": 33.75,
  "authorized_date": "2025-04-21",
  "authorized_datetime": null,
  "category": ["Food and Drink", "Beer, Wine and Liquor"],
  "category_id": "13009000",
  "check_number": null,
  "counterparties": [],
  "date": "2025-04-21",
  "datetime": null,
  "iso_currency_code": "USD",
  "location": {
    "address": null,
    "city": "Boston",
    "country": "US",
    "lat": null,
    "lon": null,
    "postal_code": "02110",
    "region": "MA",
    "store_number": null
  },
  "logo_url": null,
  "merchant_entity_id": null,
  "merchant_name": "Total Wine",
  "name": "Total Wine",
  "payment_channel": "in store",
  "payment_meta": {},
  "pending": false,
  "pending_transaction_id": null,
  "personal_finance_category": {
    "confidence_level": "HIGH",
    "detailed": "FOOD_AND_DRINK_BEER_WINE_AND_LIQUOR",
    "primary": "FOOD_AND_DRINK"
  },
  "personal_finance_category_icon_url": "https://plaid-category-icons.plaid.com/PFC_FOOD_AND_DRINK.png",
  "transaction_code": null,
  "transaction_id": "txn_blacklisted_category_004",
  "transaction_type": "place",
  "unofficial_currency_code": null,
  "website": null
}
'

