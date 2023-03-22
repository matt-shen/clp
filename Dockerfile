FROM amazoncorretto:17

COPY e-commerce-backend-main/target/e-commerce*.jar e-commerce.jar

ENTRYPOINT ["java", "-jar", "/e-commerce.jar"]

EXPOSE 7777