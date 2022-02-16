# Test task for TrackEnsure by Konstantin Zinchenko
## Description
Basic Tomcat 10 web chat that saves users and their sent messages to MySQL database and also shows new messages without reloading the page. To implement this it uses WebSocket server that sents new messages in JSON format (using Jackson) to clients.
### [Database link](webchat.sql)
### You can edit database connection credentials in [database.properties](src/main/resources/database.properties)
## Dependencies
- Tomcat 10
- Servlet API
- WebSocket API
- JDBC
- Jackson
