@user_oauth
Feature: 02_token_usuario - Gerar token usuario

  Background:
  * def usernameInput = karate.get('uid') ? karate.get('uid') : userUid
  * def passwordInput = karate.get('pwd') ? karate.get('pwd') : userPwd

@user_oauth @token
  Scenario: get user token
        Given url authorizeUrl
        And param response_type = 'code'
        And param client_id = clientId
        When method get
        Then status 200
          * def responseHtml = response
          * def csrfToken = karate.extract(responseHtml, 'name="_csrf".+?value="([^"]+)"', 1)
          * print '_csrToken: ', csrfToken

        * header Authorization = 'Basic ' + clientEncoded
        Given url loginUrl
        And header Content-Type = 'application/x-www-form-urlencoded'
        And form field username = usernameInput
        And form field password = passwordInput
        And form field _csrf = csrfToken
        When method post
        Then status 200
            * def secondResponseHtml = response
            * def secondCsrfToken = karate.extract(secondResponseHtml, 'name="_csrf".+?value="([^"]+)"', 1)
            * print '_SecondCsrToken: ', secondCsrfToken

        * configure followRedirects = false
        * header Authorization = 'Basic ' + clientEncoded
        Given url loginUrl
        And header Content-Type = 'application/x-www-form-urlencoded'
        And header Accept = 'application/json, text/plain, */*'
        And form field username = userUid
        And form field password = userPwd
        And form field _csrf = secondCsrfToken
        When method post
        Then status 302
          * def location = responseHeaders['Location'][0]
          * print 'Location2: ', location
          * def codeString = "code="
          * def code = location.substring(location.indexOf(codeString) + codeString.length, location.indexOf("&"));
          * print 'Code Param: ', code

      * header Authorization = 'Basic ' + clientEncoded
      Given url tokenUrl
      And header Content-Type = 'application/x-www-form-urlencoded'
      And header Accept = 'application/json, text/plain, */*'
      And form field grant_type = 'authorization_code'
      And form field code = code
      When method post
      Then status 200
        * def token = response.access_token
        * match token == '#string'
        * print 'User Token: ', token
