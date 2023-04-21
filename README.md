## Дипломная работа «Облачное хранилище»
### Описание проекта
Задача — разработать REST-сервис. Сервис должен предоставить REST-интерфейс для загрузки файлов и вывода списка уже загруженных файлов пользователя.
Все запросы к сервису должны быть авторизованы. Заранее подготовленное веб-приложение (FRONT) должно подключаться к разработанному сервису без доработок, а также использовать функционал FRONT для авторизации, загрузки и вывода списка файлов пользователя.
### Стек технологий:
1. Java 11
2. Spring boot
3. Hibernate
4. Liquibase
5. Docker
6. JWT

### Запуск проекта:
1. Перейти в корень проекта
2. Выполнить команду
```
docker compose build
```
3. Выполнить команду
```
 docker compose up
```
4. Скачать [фронт](https://github.com/AlekseyGoldberg/DataCloudFrontend) и следовать инструкциям  
5. Перейти на фронт http://localhost:8080/
6. В базе данных есть тестовый пользователь, под которым можно авторизоваться в приложении
```
Логин: test
Пароль: test
```