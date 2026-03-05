@customer @reviews
Feature: 04_Customer_Reviews - Avaliações de Clientes

  Background:
    * url baseOccUrl

    * def productCode = karate.get('productCode') ? karate.get('productCode') : '23355'

    * def uid = karate.get('userUid') ? karate.get('userUid') : userUid
    * def pwd = karate.get('userPassword') ? karate.get('userPassword') : userPwd
    * def login = {'uid': '#(uid)', 'pwd': '#(pwd)'}

    * def userAuth = callonce read('classpath:features/02_token_usuario/token_usuario.feature') login
    * def userToken = 'Bearer ' + userAuth.token

    * def title = karate.get('headline') ? karate.get('headline') : 'Excelente produto'
    * def comment = karate.get('comment') ? karate.get('comment') : 'O produto superou minhas expectativas. Muito bom no dia-a-dia'
    * def rating = karate.get('rating') ? karate.get('rating') : 3
    * configure headers = { 'Content-Type': 'application/json', 'Authorization': '#(userToken)' }

@smoke @customer @reviews
Scenario: Cria review para um produto com sucesso

    # 1) Cria review para um produto
    Given path 'products', productCode, 'reviews'
    And request {'headline': '#(title)', 'comment': '#(comment)', 'rating': '#(rating)'}
    When method post
    Then status 201
    * def id = response.id
    * match id == '#string'

* def result = true