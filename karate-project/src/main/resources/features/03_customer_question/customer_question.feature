@customer @question
Feature: 03_Customer_Question

  Background:
    * url baseOccUrl
    * def productCode = karate.get('productCode') ? karate.get('productCode') : '23355'

    * def uid = karate.get('userUid') ? karate.get('userUid') : userUid
    * def pwd = karate.get('userPassword') ? karate.get('userPassword') : userUid
    * def login = {'uid': '#(uid)', 'pwd': '#(pwd)'}

    * def auth = callonce read('classpath:features/02_token_usuario/token_usuario.feature') login

    * def token = auth.token
    * def bearerToken = 'Bearer ' + token
    * def product = karate.get('productCode') ? karate.get('productCode') : productCode
    * def question = karate.get('question') ? karate.get('question') : 'Quanto tempo de garantia tem o produto? Karate-test'
    * configure headers = { 'Content-Type': 'application/json', 'Authorization': '#(bearerToken)' }

@smoke @customer @question
Scenario: Login -> Cliente faz pergunta sobre o produto

    # 1) Cliente faz pergunta sobre o produto
    Given path 'users', 'current', 'products', product, 'question'
    And request {'question': '#(question)'}
    When method post
    Then status 201

* def result = true