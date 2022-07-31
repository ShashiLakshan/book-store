FROM java:8

EXPOSE 8080

ADD target/book-store-0.0.1-SNAPSHOT.jar book-store-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","book-store-0.0.1-SNAPSHOT.jar"]