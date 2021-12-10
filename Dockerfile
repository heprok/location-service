FROM --platform=linux/arm64/v8 public.ecr.aws/docker/library/gradle:jdk11-hotspot as builder

WORKDIR /app
COPY . .

RUN gradle bootJar --no-daemon

FROM --platform=linux/arm64/v8 public.ecr.aws/docker/library/openjdk:11-jdk-slim

COPY --from=builder /app/build/libs/*.jar /application.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","application.jar"]
