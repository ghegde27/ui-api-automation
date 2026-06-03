FROM gradle:jdk21

WORKDIR /app

COPY . .

RUN chmod +x gradlew

ENTRYPOINT ["sh","-c"]

CMD ["./gradlew $@ && mkdir -p /results/allure-results /results/test-results && cp -R build/allure-results/* /results/allure-results/ 2>/dev/null || true && cp -R build/test-results/* /results/test-results/ 2>/dev/null || true","--"]