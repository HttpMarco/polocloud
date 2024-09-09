Base path = `/polocloud/api/v1`

Path:<br>
`/user/create`
`/auth/login`

First request is the Admin user = no token needed<br>
All requests after that need a token

Token must be provider by Authorization header
```html
Bearer <token>
```

You will get the token by creating or log in (with password and username).

Example:
````json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpcCI6IjEyNy4wLjAuMSIsInVzZXJBZ2VudCI6Imluc29tbmlhLzkuMy4zIiwidXVpZCI6ImQzOTM5ODIwLTMzODItNDFjOC1hOTUwLTI2MjFlMGFhYThmNCIsImV4cCI6MTcyNjUwNTg2N30.uSW1q_kJ2U8BF0iSo5s57JsqeUQ-MevNx5QcuNbfkdoPPK7JCydL_g8_LEemna0kJ6NH_oLJzDBHkptoSaAvYw"
}
````

to create a user or login you need following json body:
````json
{
  "username": "your_username",
  "password": "your_password"
}
````

to verify a token u need to request to `/auth/verify` with the token in the header<br>
you will get a status **200** if the token is valid
if it is incorrect you will get a status **401**


