@oauth
Feature: 01_token_sistemico - Gerar token sistemico

  Background:

@oauth @token
  Scenario: get token
      Given url tokenUrl
      And header Content-Type = 'application/x-www-form-urlencoded'
      And header Accept = 'application/json, text/plain, */*'
      And form field grant_type = systemGrantType
      And form field client_id = clientId
      And form field client_secret = clientSecret
      
      When method post
          Then status 200
              * def token = response.access_token
              * match token == '#string'