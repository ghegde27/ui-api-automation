FROM gradle:jdk21

WORKDIR /app

COPY . .

RUN chmod +x gradlew

ENTRYPOINT ["./gradlew"]

CMD ["clean","test"]