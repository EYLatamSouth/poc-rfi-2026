@customer @reviews
Feature: 04_Customer_Reviews - Avaliações de Clientes

  Background:
    * url baseOccUrl

    * def productCode = '23355'
    * def login = {'uid': '#(userUid)', 'pwd': '#(userPwd)'}

    * def userAuth = callonce read('classpath:features/02_token_usuario/token_usuario.feature') login
    * def userToken = 'Bearer ' + userAuth.token

    * def title = 'Não compre'
    * def comment = 'Produto de baixa qualidade'
    * def rating = -1
    * configure headers = { 'Content-Type': 'application/json', 'Authorization': '#(userToken)' }

@smoke @customer @reviews
Scenario: Não deve criar review com rating menor que 1 ou maior que 5

    # 1) Não deve criar review com rating menor que 1 ou maior que 5
    Given path 'products', productCode, 'reviews'
    And request {'headline': '#(title)', 'comment': '#(comment)', 'rating': '#(rating)'}
    When method post
    Then status 400