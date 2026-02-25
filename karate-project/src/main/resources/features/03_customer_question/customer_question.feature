@customer @question
Feature: 03_Customer_Question - PIX contrato e fluxo

  Background:
    * url baseOccUrl
    * def productCode = '23355'
    * def auth = callonce read('classpath:features/02_token_usuario/token_usuario.feature')
    * def token = auth.token
    * def bearerToken = 'Bearer ' + token
    * configure headers = { 'Content-Type': 'application/json', 'Authorization': '#(bearerToken)' }

@smoke @customer @question
Scenario: Login -> Cliente faz pergunta sobre o produto

    # 1) Cliente faz pergunta sobre o produto
    Given path 'users', 'current', 'products', productCode, 'question'
    And request {"question": "Quanto tempo de garantia tem o produto? Karate-test"}
    When method post
    Then status 201