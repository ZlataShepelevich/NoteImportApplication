# Импорт данных из Старой системы в Новую систему
Для запуска необходимо установить Docker, запустить контейнер и саму программу.  

--- 

Для запуска необходимо установить Maven (у меня 3.9.8), JDK и Docker.

После настройки вышеупомянутого и запуска Docker необходимо выполнить ряд команд:

- запустить проект старой системы
- cd путь_к_папке_проекта (переходим в папку проекта)
- docker-compose up
- mvn install
- java -jar target/search-app-0.0.1-SNAPSHOT.jar или mvn spring-boot:run (запустить программу можно и в IDE)

_Для того чтобы программа работала правильно, необходимо при первом запуске добавить в базу данных строку (она есть в файле src/main/resources/schema.sql)_
