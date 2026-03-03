@question @data
Feature: 12_PlaceOrder - Reserva de Produto e Finaliza Pedido com dados CSV

  @question @data
  Scenario: gerar tokens para usuários do CSV
    * def questions = read('classpath:data/user-questions.csv')
    * def responses = call read('classpath:features/03_customer_question/customer_question.feature') questions

    # Filtra somente testes finalizados com sucesso
    * def valid = responses.filter(x => x && x.result)
    * print 'Responses OK: ', valid

    # Se alguma pergunta tiver falha no teste, falha explicitamente para diagnóstico
    * if (valid.length != responses.length) karate.fail('Uma ou mais perguntas retornaram erro')