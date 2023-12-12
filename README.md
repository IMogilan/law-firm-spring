# law-firm-spring

# HOW TO RUN APPLICATION
# 1. Create database law_firm (first line in create-tables-script.sql)
# 2. Created tables in law_firm by execution of create-tables-script.sql
# 3. Populate tables by execution of populate-script.sql
# 4. Edit properties in application.properties file (if necessary)
# 5. Set up and launch Tomcat 10

# HOT TO RUN TESTS
# 1. START DOCKER
# 2. Execute "mvn clean test" in command line

# Эндпоинт	        	        Метод	Описание
# /api/clients/			        GET	    Получение списка всех клиентов
# /api/clients/{id}		        GET	    Получение информации о конкретном клиенте
# /api/clients/			        POST	Создание нового клиента
# /api/clients/{id}		        PUT	    Обновление информации о клиенте
# /api/clients/{id}		        DELETE	Удаление клиента

# /api/lawyers/			        GET	    Получение списка всех юристов
# /api/lawyers/{id}		        GET	    Получение информации о конкретном юристе
# /api/lawyers/			        POST	Создание нового юриста
# /api/lawyers/{id}		        PUT	    Обновление информации о юристе
# /api/lawyers/{id}		        DELETE	Удаление юриста

# /api/tasks/			        GET	    Получение списка всех задач
# /api/tasks/{id}			    GET	    Получение информации о конкретной задаче
# /api/tasks/			        POST	Создание новой задачи
# /api/tasks/{id}			    PUT	    Обновление информации о задаче
# /api/tasks/{id}			    DELETE	Удаление задачи
