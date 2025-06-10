Feature: User registration and login

Scenario: User signs up and logs in
  Given generate random credentials
  When the user signs up with valid credentials
  And the user logs in with valid credentials
  Then the login token should be returned

Scenario: Sign up with an existing email
  Given the email "EXISTING@mail.com" and password "anypass"
  When the user signs up with those credentials
  Then the API should return status 400 and message "User already exists"

Scenario: Login with wrong credentials
  Given the email "WRONG@mail.com" and password "badpass"
  When the user logs in with those credentials
  Then the API should return status 401 and message "Invalid credentials"