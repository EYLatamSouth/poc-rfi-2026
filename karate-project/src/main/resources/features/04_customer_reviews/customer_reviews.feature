@customer @reviews
Feature: 04_Customer_Reviews - Avaliações de Clientes

  Background:
    * url baseOccUrl

    * def productCode = karate.get('productCode') ? karate.get('productCode') : '2278102'

    * def uid = karate.get('userUid') ? karate.get('userUid') : userUid
    * def pwd = karate.get('userPassword') ? karate.get('userPassword') : userPwd
    * def login = {'uid': '#(uid)', 'pwd': '#(pwd)'}

    * def userAuth = callonce read('classpath:features/02_token_usuario/token_usuario.feature') login
    * def userToken = 'Bearer ' + userAuth.token

    * def systemAuth = callonce read('classpath:features/01_token_sistemico/token_sistemico.feature')
    * def systemToken = 'Bearer ' + systemAuth.token

    * def reviewId = karate.get('reviewId') ? karate.get('reviewId') : '1'
    * def helpful = karate.get('helpful') ? karate.get('helpful') : true

    * configure headers = { 'Content-Type': 'application/json' }

@smoke @customer @reviews
Scenario: Busca reviews de um produto -> Avalia se review foi útil -> Valida reviews

    # 1) Busca reviews de um produto
    * header Authorization = userToken
    Given path 'products', productCode, 'reviews'
    When method get
    Then status 200
    * assert response.reviews.length > 0

    # 2) Avalia se review foi útil
    * header Authorization = userToken
    Given path 'products', productCode, 'review', reviewId, 'helpful'
    And param helpful = helpful
    When method post
    Then status 201

    # 3) Valida reviews
    * header Authorization = systemToken
    Given path 'products', productCode, 'engagementSummary'
    When method get
    Then status 200
    * assert response.rating !== '0,00'

* def result = true