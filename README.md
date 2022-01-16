# Приложение Async Queue

## О проекте
#### Описание
Приложение, реализующее функционал асинхронной очереди.
При запуске стартует Socket и ожидает запросов клиентов. 
Клиенты могут быть двух типов: отправители (publisher) и получатели (subscriber)
В качестве клиента используется утилита cURL `https://curl.se/download.html`
Два режима работы: queue и topic

#### Технологии
> JDK14, Maven, Junit, Sockets, Java Concurrency, Java IO

## Сборка
0. Скачать файлы репозитория
3. Произвести сборку проекта: `mvn clean install`
4. Скопировать полученный файл *target/Wget.jar* в папку вашего сервера
5. Приложение будет доступно на порте, который указан в файле *PoohServer*

## Запуск Docker
1. Создать директорию на сервере и скопировать файлы репозитория
2. Перейти в созданную директорию (корень проекта) и собрать приложение командой: `mvn install`
3. Собирать docker-образ приложения командой: `docker build -t wget .`
5. Запустить образ: `docker run -d -p 9000:9000 wget`

## Как пользоваться
Режим **queue**

Отправитель посылает запрос на добавление данных с указанием очереди и значением произвольного параметра.
Сообщение помещается в конец очереди. Если очереди нет в сервисе, то нужно создать новую и поместить в нее сообщение:

`curl -X POST -d "message=1" http://localhost:9000/queue/nameOfQueue`

`curl -X POST -d "message=2" http://localhost:9000/queue/nameOfQueue`

`curl -X POST -d "message=3" http://localhost:9000/queue/nameOfQueue`

![queueSendMessage](images/Selection_221.png)

Получатель посылает запрос на получение данных с указанием очереди. 
Сообщение забирается из начала очереди и удаляется.

`curl -X GET http://localhost:9000/queue/nameOfQueue`

![queueReceiveMessage](images/Selection_222.png)

Режим **topic**

В файле *ru/job4j/pooh/TopicService* задаем тему и ID подписчиков (например: topic - moscow, id - 1, 2)
Отправитель посылает запрос на добавление данных с указанием топика и значением параметра. 
Сообщение помещается в конец каждой индивидуальной очереди получателей. 
Если топика нет в сервисе, то данные игнорируются.

`curl -X POST -d "message=1" http://localhost:9000/topic/moscow`

`curl -X POST -d "message=2" http://localhost:9000/topic/moscow`

`curl -X POST -d "message=3" http://localhost:9000/topic/moscow`

![topicSendMessage](images/Selection_223.png)

Получатель посылает запрос на получение данных с указанием топика. Если топик отсутствует - создается новый. 
А если топик присутствует, то сообщение забирается из начала индивидуальной очереди получателя и удаляется.

![topicReceiveMessage](images/Selection_224.png)

## Контакты
Кутявин Владимир

skype: tribuna87

email: tribuna87@mail.ru

telegram: @kutiavinvladimir
