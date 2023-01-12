# kton-mex-chat-server
Server created with Kton and Kotlin

# How to run it
- Need Docker and IntelliJ (recommended) installed
- Clone repo in your machine
- Run `docker-compose up`, this will deploy Docker with PostreSQL and PGAdmin, configurations are in `docker-compose.yml`
- Run `Application.kt` to start server

# Server configuration
All configurations are in `resources/application.conf` 

# Database migrations
Migrations are located in `resources/db.migrations/`

# Test
You can use Postman to test the api or the project include `http/loginJWT.http` file to use with VS Code and Rest Client extension 

REST Client for Visual Studio Code
https://marketplace.visualstudio.com/items?itemName=humao.rest-client 

# About Flywaydb
### How Flyway works
https://flywaydb.org/documentation/getstarted/how

### Flyway migrations
https://flywaydb.org/documentation/concepts/migrations