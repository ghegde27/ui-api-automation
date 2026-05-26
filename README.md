# API Automation Framework

This project contains a reusable API automation framework built with Rest Assured, TestNG, Gradle, and Jackson.

The API framework is designed to keep tests clean by separating configuration, request creation, response handling, assertions, models, and endpoint services.

## Current Automated API

```http
POST http://gateway.local:8081/users
```

Request body:

```json
{
  "name": "shaila1",
  "email": "manjushaila1@gmail.com",
  "phone": "9986693954"
}
```

## Framework Structure

```text
src/main/java/org/framework/api
├── assertions
│   └── ApiAssertions.java
├── config
│   └── ApiConfig.java
├── core
│   ├── ApiClient.java
│   ├── ApiRequest.java
│   └── ApiResponse.java
├── model
│   └── User.java
└── service
    ├── BaseApiService.java
    └── UserService.java

src/test/java
└── UsersApiTest.java

src/test/resources
├── api-testng.xml
└── config.properties
```

## Architecture

```text
UsersApiTest
   |
   v
UserService
   |
   v
BaseApiService.request()
   |
   v
ApiRequest.create()
   |
   v
ApiClient.givenRequest()
   |
   v
Rest Assured RequestSpecification
   |
   v
POST /users
   |
   v
ApiResponse.from(response)
   |
   v
Print response details
   |
   v
ApiAssertions
```

High-level flow:

```text
Test Class
  -> Service Class
    -> Request Wrapper
      -> Rest Assured Client
        -> API Server
      <- Response Wrapper
  <- Assertions
```

## Layer Responsibilities

### Config Layer

File:

```text
src/main/java/org/framework/api/config/ApiConfig.java
```

Purpose:

- Reads API configuration from the existing `ConfigManager`
- Keeps API-specific config in one place
- Supports runtime overrides through JVM system properties

Configuration:

```properties
api.base.url=http://gateway.local:8081
api.connection.timeout.ms=10000
api.socket.timeout.ms=30000
api.default.content.type=application/json
api.logging.enabled=true
api.auth.bearer.token=
api.auth.api.key.header=
api.auth.api.key.value=
```

### Core Client Layer

File:

```text
src/main/java/org/framework/api/core/ApiClient.java
```

Purpose:

- Creates the base Rest Assured `RequestSpecification`
- Applies base URI
- Applies default headers
- Applies timeout configuration
- Applies optional bearer token or API key authentication
- Enables request logging

### Request Layer

File:

```text
src/main/java/org/framework/api/core/ApiRequest.java
```

Purpose:

- Provides a fluent wrapper around Rest Assured
- Keeps tests and services clean
- Supports common HTTP operations

Supported HTTP methods:

```java
get(String path)
post(String path)
put(String path)
patch(String path)
delete(String path)
```

Supported request customizations:

```java
header(String name, Object value)
headers(Map<String, ?> headers)
queryParam(String name, Object value)
pathParam(String name, Object value)
body(Object body)
```

Example:

```java
ApiRequest.create()
        .body(user)
        .post("/users");
```

### Response Layer

File:

```text
src/main/java/org/framework/api/core/ApiResponse.java
```

Purpose:

- Wraps Rest Assured `Response`
- Centralizes response handling
- Prints response details for every request
- Supports JSON deserialization

Printed response details:

```text
Response status
Response time
Response headers
Response body
```

Available helpers:

```java
statusCode()
bodyAsString()
as(Class<T> type)
asList(Class<T> type)
jsonPath(String path)
raw()
```

### Assertion Layer

File:

```text
src/main/java/org/framework/api/assertions/ApiAssertions.java
```

Purpose:

- Centralizes reusable API assertions
- Keeps test classes readable
- Adds useful failure messages with response body

Current assertions:

```java
assertStatusCode(response, 200)
assertStatusCodeIn(response, 200, 201)
assertJsonField(response, "name", expectedName)
assertResponseTimeBelow(response, 2000)
```

### Model Layer

File:

```text
src/main/java/org/framework/api/model/User.java
```

Purpose:

- Represents API request and response payloads as Java objects
- Avoids hard-coded JSON strings inside tests
- Works with Jackson serialization and deserialization

Current model:

```text
User
├── name
├── email
└── phone
```

### Service Layer

Files:

```text
src/main/java/org/framework/api/service/BaseApiService.java
src/main/java/org/framework/api/service/UserService.java
```

Purpose:

- Encapsulates endpoint details
- Keeps test classes focused on validation
- Uses one service class per API domain or resource

Current service:

```java
public ApiResponse createUser(User user) {
    return request()
            .body(user)
            .post("/users");
}
```

### Test Layer

File:

```text
src/test/java/UsersApiTest.java
```

Purpose:

- Defines TestNG API scenarios
- Uses service and assertion layers
- Avoids direct Rest Assured usage in tests

Current test flow:

```text
Create User test
  -> Build User request object
  -> Call UserService.createUser()
  -> Validate HTTP status is 200 or 201
  -> Validate name, email, and phone in response
```

## Running API Tests

API tests are isolated in:

```text
src/test/resources/api-testng.xml
```

Run:

```bash
env JAVA_HOME=/Users/gopalkrishnahegde/Library/Java/JavaVirtualMachines/corretto-11.0.31/Contents/Home ./gradlew test -DsuiteXml=src/test/resources/api-testng.xml
```

Compile only:

```bash
./gradlew compileTestJava
```

## Logging

Request logging is enabled in `ApiClient`.

Response logging is centralized in `ApiResponse`.

Logging is controlled by:

```properties
api.logging.enabled=true
```

When enabled, each API call prints:

```text
Request method
Request URI
Request headers
Request body
Response status
Response time
Response headers
Response body
```

## JDBC Database Utilities

The framework includes JDBC helpers for SQL database validation.

Files:

```text
src/main/java/org/framework/database/DatabaseConfig.java
src/main/java/org/framework/database/JdbcUtils.java
```

### Database Configuration

Add database values in `src/test/resources/config.properties`.

Example for MySQL:

```properties
db.url=jdbc:mysql://localhost:3306/userdb
db.username=root
db.password=root
db.driver.class=com.mysql.cj.jdbc.Driver
db.connection.timeout.ms=10000
```

Example for PostgreSQL:

```properties
db.url=jdbc:postgresql://localhost:5432/userdb
db.username=postgres
db.password=postgres
db.driver.class=org.postgresql.Driver
db.connection.timeout.ms=10000
```

The correct JDBC driver dependency must be present in `build.gradle` for the database being used.

MySQL example:

```gradle
implementation "com.mysql:mysql-connector-j:8.4.0"
```

PostgreSQL example:

```gradle
implementation "org.postgresql:postgresql:42.7.3"
```

### Check Database Connection

```java
import org.framework.database.JdbcUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DatabaseHealthTest {

    @Test
    public void verifyDatabaseConnection() {
        Assert.assertTrue(
                JdbcUtils.isConnectionAvailable(),
                "Database connection should be available"
        );
    }
}
```

### Query SQL Database

```java
import org.framework.database.JdbcUtils;

import java.util.List;
import java.util.Map;

public class UserRepositoryExample {

    public void readUsers() {
        List<Map<String, Object>> rows = JdbcUtils.executeQuery(
                "SELECT id, name, email, phone FROM users WHERE email = ?",
                "manjushaila1@gmail.com"
        );

        for (Map<String, Object> row : rows) {
            Long id = ((Number) row.get("id")).longValue();
            String name = String.valueOf(row.get("name"));
            String email = String.valueOf(row.get("email"));
            String phone = String.valueOf(row.get("phone"));

            System.out.println(id);
            System.out.println(name);
            System.out.println(email);
            System.out.println(phone);
        }
    }
}
```

### Query Single Row And Parse Response

```java
import org.framework.database.JdbcUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class UserDatabaseTest {

    @Test
    public void verifyUserCreatedInDatabase() {
        Map<String, Object> user = JdbcUtils.executeQueryForSingleRow(
                "SELECT id, name, email, phone FROM users WHERE email = ?",
                "manjushaila1@gmail.com"
        );

        Assert.assertFalse(user.isEmpty(), "User should exist in database");

        String name = String.valueOf(user.get("name"));
        String email = String.valueOf(user.get("email"));
        String phone = String.valueOf(user.get("phone"));

        Assert.assertEquals(name, "shaila1");
        Assert.assertEquals(email, "manjushaila1@gmail.com");
        Assert.assertEquals(phone, "9986693954");
    }
}
```

### Insert Or Update SQL Data

```java
int updatedRows = JdbcUtils.executeUpdate(
        "UPDATE users SET phone = ? WHERE email = ?",
        "9986693954",
        "manjushaila1@gmail.com"
);
```

```java
int insertedRows = JdbcUtils.executeUpdate(
        "INSERT INTO users (name, email, phone) VALUES (?, ?, ?)",
        "shaila1",
        "manjushaila1@gmail.com",
        "9986693954"
);
```

## Redis Utilities

The framework includes Redis helpers using Jedis.

Files:

```text
src/main/java/org/framework/redis/RedisConfig.java
src/main/java/org/framework/redis/RedisUtils.java
```

Dependency:

```gradle
implementation "redis.clients:jedis:${jedisVersion}"
```

Version:

```properties
jedisVersion=5.1.5
```

### Redis Configuration

Add Redis values in `src/test/resources/config.properties`.

```properties
redis.host=localhost
redis.port=6379
redis.password=
redis.database=0
redis.connection.timeout.ms=10000
redis.socket.timeout.ms=10000
redis.max.total=8
redis.max.idle=8
redis.min.idle=0
```

For password-protected Redis:

```properties
redis.host=localhost
redis.port=6379
redis.password=secret
redis.database=0
```

### Check Redis Connection

```java
import org.framework.redis.RedisUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RedisHealthTest {

    @Test
    public void verifyRedisConnection() {
        Assert.assertTrue(
                RedisUtils.isConnectionAvailable(),
                "Redis connection should be available"
        );
    }
}
```

### Set And Get String Value

```java
import org.framework.redis.RedisUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RedisStringTest {

    @Test
    public void verifyRedisStringValue() {
        RedisUtils.set("user:email", "manjushaila1@gmail.com");

        String email = RedisUtils.get("user:email");

        Assert.assertEquals(email, "manjushaila1@gmail.com");
    }
}
```

### Set Value With Expiry

```java
RedisUtils.set("otp:9986693954", "123456", 60);

String otp = RedisUtils.get("otp:9986693954");
long ttl = RedisUtils.ttl("otp:9986693954");
```

### Parse Redis Hash Response

```java
import org.framework.redis.RedisUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class RedisHashTest {

    @Test
    public void verifyUserHash() {
        RedisUtils.hset("user:manjushaila1@gmail.com", "name", "shaila1");
        RedisUtils.hset("user:manjushaila1@gmail.com", "phone", "9986693954");

        Map<String, String> user = RedisUtils.hgetAll("user:manjushaila1@gmail.com");

        String name = user.get("name");
        String phone = user.get("phone");

        Assert.assertEquals(name, "shaila1");
        Assert.assertEquals(phone, "9986693954");
    }
}
```

### Parse Redis List Response

```java
import org.framework.redis.RedisUtils;

import java.util.List;

public class RedisListExample {

    public void readEvents() {
        RedisUtils.lpush("user-events", "created", "verified", "logged-in");

        List<String> events = RedisUtils.lrange("user-events", 0, -1);

        for (String event : events) {
            System.out.println(event);
        }
    }
}
```

### Delete Redis Keys

```java
long deletedCount = RedisUtils.delete(
        "user:email",
        "otp:9986693954",
        "user:manjushaila1@gmail.com"
);
```

### Close Redis Pool

Use this only when the test suite is done with Redis.

```java
RedisUtils.closePool();
```

## Design Principles

- Tests should not directly depend on Rest Assured internals.
- Endpoint paths should live in service classes.
- Payloads should be represented as Java models.
- Configuration should come from `config.properties`.
- Request and response logging should be centralized.
- SQL database access should go through `JdbcUtils`.
- Redis access should go through `RedisUtils`.
- API tests should run separately from Selenium tests.
- Assertion failures should include response body for faster debugging.

## Current Backend Result

The framework currently sends the expected request to:

```text
http://gateway.local:8081/users
```

Observed response:

```json
{
  "error": "user creation failed"
}
```

Observed status:

```text
500
```

This means the automation is making the request successfully, but the backend is returning an application/server error for the current payload.
