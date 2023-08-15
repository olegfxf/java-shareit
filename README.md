# Shareit
Shareit это сервис, который должен обеспечить пользователям, во-первых, возможность рассказывать, какими вещами они готовы поделиться, а во-вторых, находить нужную вещь и брать её в аренду на какое-то время. Сервис может не только позволять бронировать вещь на определённые даты, но и закрывать к ней доступ на время бронирования от других желающих. На случай, если нужной вещи на сервисе нет, у пользователей должна быть возможность оставлять запросы.
# Стек технологий
Java 11, Spring Boot, Maven, Git, REST Api, Lombok, SQL, H2, Jdbc, JdbcTemplate, Spring Data JPA, Docker.
# Архитектура проекта
Сервис включает в себя два модуля. Модуль gateway выполняет функции валидации данных и маршрутизации проверенных данных на server. В модуле server реализованы все сервисные функции приложения. Обмен данными между модулями gateway и server реализован по технологии REST. Управление модулями реализовано через Docker-compose.

Основные функции модуля server:
* Добавление/удаление/редактирование вещей.
* Бронировать вещь на определённые даты и закрывает к ней доступ на время бронирования от других желающих.
* Поиск вещей по названию или описанию, с возможностью фильтрации и постраничной выдачей.
* Подтверждение или отклонение заявок на аренду владельцем.
* Создание запроса на вещь, которая отсутствует в приложении.
* Получение списка всех бронирований пользователя, с возможностью сортировки и фильтрации.
* Получение списка всех бронирований вещей, принадлежащих пользователю, с возможностью сортировки и фильтрации.
* Получение списка всех вещей пользователя.
* Добавление отзыва пользователем на вещь после того, как взял её в аренду.
* Получение пользователем всех его запросов, с возможностью фильтрации и сортировки.

# Запуск приложения

