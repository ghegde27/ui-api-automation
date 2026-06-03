FROM gradle:8.0-jdk17

WORKDIR /app

COPY . .

RUN chmod +x gradlew

CMD [
 "./gradlew",
 "clean",
 "test",
 "allureReport"
]