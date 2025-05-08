const express = require('express');
const bodyParser = require('body-parser');
const { Configuration, PlaidApi, PlaidEnvironments } = require('plaid');

const app = express();
app.use(bodyParser.json());

const PORT = 8000;

// Setup Plaid client
const plaidClient = new PlaidApi(
  new Configuration({
    basePath: PlaidEnvironments.sandbox,
    baseOptions: {
      headers: {
        'PLAID-CLIENT-ID': '67e438984ead690024065f1f',
        'PLAID-SECRET': '426b1e079a5850f694123fcb5cbe82',
      },
    },
  })
);

// Store access token globally for now (just for testing)
let ACCESS_TOKEN = '';

// Exchange public token for access token
app.post('/get_access_token', async (req, res) => {
  const publicToken = req.body.public_token;

  try {
    const response = await plaidClient.itemPublicTokenExchange({
      public_token: publicToken,
    });
    ACCESS_TOKEN = response.data.access_token;
    console.log('Access Token:', ACCESS_TOKEN);
    res.json({ access_token: ACCESS_TOKEN });
  } catch (error) {
    console.error('Error exchanging token:', error);
    res.status(500).send('Token exchange failed');
  }
});

// Webhooks in order to create our own transactions
app.post('/webhook', (req, res) => {
  // Log the webhook data as a single line
  console.log('Webhook received:', JSON.stringify(req.body));

  res.status(200).send('Webhook received');
});

// List transactions simulated by Plaid (past 30 days)
app.get('/transactions', async (req, res) => {
  try {
    const today = new Date();
    const start = new Date();
    start.setDate(today.getDate() - 30);

    const response = await plaidClient.transactionsGet({
      access_token: ACCESS_TOKEN,
      start_date: start.toISOString().split('T')[0],
      end_date: today.toISOString().split('T')[0],
    });

    console.log('Transactions:', JSON.stringify(response.data.transactions));
    res.json(response.data.transactions);
  } catch (error) {
    console.error('Error getting transactions:', error);
    res.status(500).send('Could not fetch transactions');
  }
});

// Start server
app.listen(PORT, () => {
  console.log(`Server running on http://localhost:${PORT}`);
});

