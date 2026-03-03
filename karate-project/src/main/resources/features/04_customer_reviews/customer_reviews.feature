@customer @reviews
Feature: 04_Customer_Reviews - Avaliações de Clientes

  Background:
    * url baseOccUrl
    #* def userAuth = callonce read('classpath:features/02_token_usuario/token_usuario.feature')
    * def systemAuth = callonce read('classpath:features/01_token_sistemico/token_sistemico.feature')
    #* def userToken = 'Bearer ' + userAuth.token
    * def systemToken = 'Bearer ' + systemAuth.token
    * def productCode = '23355'
    * configure headers = { 'Content-Type': 'application/json' }

@smoke @customer @reviews
Scenario: Busca reviews de um produto -> Avalia se review foi útil -> Valida reviews

    # 1) Busca reviews de um produto
    * header Authorization = systemToken
    Given path 'products', productCode, 'reviews'
    When method get
    Then status 200
    #And match response.reviews.size > 0

    # 2) Avalia se review foi útil
    * header Authorization = systemToken
    Given path 'products', productCode, 'review', 1, 'helpful'
    And param helpful = 'true'
    When method post
    Then status 201

    # 3) Valida reviews
    * header Authorization = systemToken
    Given path 'products', productCode, 'engagementSummary'
    When method get
    Then status 200
    #And match response.rating !== '0,00'