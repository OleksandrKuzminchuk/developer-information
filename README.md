Необходимо реализовать консольное CRUD приложение, которое взаимодействует с БД 
и позволяет выполнять все CRUD операции над сущностями:
Developer (id, firstName, lastName, List<Skill> skills, Specialty specialty)
Skill
Specialty
Status (enum ACTIVE, DELETED)
Требования:
Все CRUD операции для каждой из сущностей
Придерживаться подхода MVC
Для сборки проекта использовать Maven
Для взаимодействия с БД - Hibernate
Для конфигурирования Hibernate - аннотации
Инициализация БД должна быть реализована с помощью flyway
Сервисный слой приложения должен быть покрыт юнит тестами (junit + mockito)
Результатом выполнения задания должен быть репозиторий на github. Технологии: Java, PostgreSQL, Hibernate, Flyway, Maven.