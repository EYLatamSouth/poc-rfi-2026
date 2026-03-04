@question @data
Feature: 03_Question - Realiza reviews de produtos via CSV

  @question @data
  Scenario: gerar tokens para usuários do CSV
    * def reviews = read('classpath:data/user-reviews.csv')
    * def responses = call read('classpath:features/04_customer_reviews/customer_reviews.feature') reviews

    # Filtra somente testes finalizados com sucesso
    * def valid = responses.filter(x => x && x.result)
    * print 'Responses OK: ', valid

    # Se alguma pergunta tiver falha no teste, falha explicitamente para diagnóstico
    * if (valid.length != responses.length) karate.fail('Uma ou mais perguntas retornaram erro')