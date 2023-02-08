# SIMPLE API REST WITH CLOJURE AND MYSQL USING PEDESTAL

FIXME

## Endpoints

1. GET :

 * /api/v1/services/users - list registered users
 * /api/v1/services/:cpf - list registered users by cpf

  response -
 [
   	{
		"id": "3467c9ed-e931-4d31-9093-54ba0a1169eb",
		"client_name": "Marcos",
		"client_lastname": "Pereira",
		"client_email": "marcos@gmail.com",
		"client_cpf": "67656234512",
		"client_account_id": "9f78eda5-15c8-442c-8e28-ceb39f6dcb8b",
		"created_at": "2023-01-03T02:39:38Z",
		"deleted_at": null
	}
 ]
 * /api/v1/services/accounts - list registered accounts 
 * /api/v1/services/accounts/:cpf - list registered accounts by cpf

response - 
[
   	{
		"id": "29518edf-e795-4cc7-a9fe-3c8ee89d58a8",
		"account_id": "9f78eda5-15c8-442c-8e28-ceb39f6dcb8b",
		"type_account": "debit",
		"account_limit": 7220.0,
		"account_status_id": 0,
		"created_at": "2023-01-03T02:39:38Z",
		"deleted_at": null
	}
] 

2. POST :

* /api/v1/services/users - endpoint for creating new users

{"name":"Isaias",
 "lastname":"Vasconcelos",
 "email":"isaiaskeyboards@gmail.com",
 "cpf":"908888888888"}

Case success -
{
   "message":"User created successfully!"
}
Case error -
{
   "message":"Error!"
}

*The script will generate the id which is in UUID format and your account id
*An account will be generated from this informed data.

* /api/v1/services/transact/new - this endpoint is responsible for sending money from one account to another in a very simple way.

{
 "cpf": "67656234512",
 "destinatary":"e391a8d2-bcbc-4528-8627-e0211c91622b",
 "value":520,
 "reason" :"gift"}

cpf - from who is sending , from your cpf we will identify your account.
destinatary - the id of the account that will receive the amount.
value - value to send.
reason - reason for transaction.

Case success -
{
   "message":"successfully transaction!"
}
Case error -
{
   "message":"Error!"
}

*by doing this you will be sending a certain value from your account to another account.
a more complete record of your transaction will be generated with all the information of that transaction if it was approved or denied among others...
 
*api/v1/services/transact/reversal - this endpoint is responsible for reversing the transactions or reversal call in the financial market in case of any problem.

{"cpf":"67656234512",
 "transaction":"159744cc-c0cf-45f6-8f20-8e678272b11f",
 "reason_reversal":"value send error"}

cpf - check if you are one of the users who participated in the transaction.
transaction - receives the id of the transaction to be reversed.
reason_reversal - the reason for the reversal.

Case success  - 
{
	"message": "reversal performed successfully!"
} 

Case error -
{
   "message": "Error!"
}


This was a basic example of a rest api developed in clojure for Study.