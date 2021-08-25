[![Build Status](https://travis-ci.com/elvolt/job4j_todo.svg?branch=master)](https://travis-ci.com/elvolt/job4j_todo)

# TODO List

## О проекте
Приложение для ведения списка задач. Позволяет пользователям совместо вести список заданий.

## Функционал
* CRUD операции с Hibernate и PostgreSQL
* Авторизация/регистрация. Доступ только у авторизованных пользователей (реализовано через фильтр). 
Авторизованный пользователь сохраняется в сессию.
* Взаимодействие frontend-a с backend-ом основано на передаче сообщений в формате JSON

## Используемые технологии
* Hibernate/PostgreSQL
* Servlets
* Maven
* Apache Tomcat
* Travis CI
* Gson
* HTML, Bootstrap, JS, Axios (front)
* Архитектурный паттерн MVC

## Демо
![ScreenShot](images/todoDemo.gif)

#### Общий вид
![ScreenShot](images/todolist.png)

#### Страница авторизации
![ScreenShot](images/signin.png)

#### Страница регистрации
![ScreenShot](images/signup.png)