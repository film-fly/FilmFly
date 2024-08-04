# OpenJDK 17 slim 기반
FROM openjdk:17-jdk-slim

# Gradle 을 사용해 빌드를 실행하는 명령어
#CMD ["./gradlew", "clean", "build"]
# CMD ["./mvnw", "clean", "package"]	필요시, Mavne을 사용해 빌드를 실행하는 명령어

#VOLUME /tmp	컨테이너 내에 /tmp 디렉터리를 볼륨으로 설정

# 이미지에 레이블 추가
LABEL type="application"

# 작업 디렉토리 설정
WORKDIR /apps

ARG JAR_FILE=build/libs/*.jar
#Gradle로 빌드한 jar 파일의 위치를 변수로 설정
# or Maven
# ARG JAR_FILE_PATH=target/*.jar

# 애플리케이션 jar 파일을 컨테이너로 복사
COPY ${JAR_FILE} /apps/app.jar

# 애플리케이션이 사용할 포트 노출
EXPOSE 443

# 애플리케이션을 실행하기 위한 엔트리포인트 정의
ENTRYPOINT ["java", "-jar", "/apps/app.jar"]