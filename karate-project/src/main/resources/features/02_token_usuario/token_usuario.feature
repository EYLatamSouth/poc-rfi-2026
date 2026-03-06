@user_oauth
Feature: 02_token_usuario - Gerar token usuario - Fluxo Local

  Background:
  * def usernameInput = karate.get('uid') ? karate.get('uid') : userUid
  * def passwordInput = karate.get('pwd') ? karate.get('pwd') : userPwd

@user_oauth @token
  Scenario: get user token

        * header Authorization = 'Basic ' + clientEncoded
        Given url loginUrl
        And header Content-Type = 'application/x-www-form-urlencoded'
        When method get
        Then status 200
              * def responseHtml = response
              * def csrfToken = karate.extract(responseHtml, 'name="_csrf".+?value="([^"]+)"', 1)
              * print '_csrToken: ', csrfToken

        * configure followRedirects = false
        * header Authorization = 'Basic ' + clientEncoded
        Given url loginUrl
        And header Content-Type = 'application/x-www-form-urlencoded'
        And form field username = usernameInput
        And form field password = passwordInput
        And form field _csrf = csrfToken
        When method post
        Then status 302

        * configure followRedirects = false
        Given url authorizeUrl
        And param response_type = 'code'
        And param client_id = clientId
        When method get
        Then status 302
           * def location = responseHeaders['Location'][0]
           * print 'Location2: ', location
           * def codeString = "code="
           * def lastIndex = location.indexOf("&") != -1 ? location.indexOf("&") : location.length
           * def code = location.substring(location.indexOf(codeString) + codeString.length, lastIndex);
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