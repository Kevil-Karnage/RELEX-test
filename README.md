# Приложение - биржа
## Обзор
Сервис является RESTfull API приложением для проведения торгов криптовалютами.
Согласно ТЗ реализованы минимальные требования и часть дополнительных заданий.

### Сервис реализует (обязательные требования):

- создан при помощи Spring Boot;
- возвращает все данные в формате Json;
- используется 2 роли: Пользователь и Администратор(далее - User и Admin)

### Сервис позволяет произвести действия (минимальные требования):
- 
- Всем
  - просмотр актуальных курсов валют
- Пользователю
  - регистрация;
  - просмотр баланса своего кошелька;
  - пополнение кошелька;
  - вывод денег с биржи:
    - по номеру кредитной карты
    - по адресу кошелька
  - обмен валют по установленному курсу
- Администратору
  - изменить курс валют
  - посмотреть общую сумму указанной валюты на всех пользовательских счетах
  - посмотреть количество операций за указанный период

### Сервис реализует дополнительные требования:

- подключена база данных PostgreSQL