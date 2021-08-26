FROM centos:7 AS build

# Install JAVA
RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel
ENV JAVA_HOME /etc/alternatives/jre

RUN yum update -y

# Install Maven
RUN yum install -y git maven

WORKDIR /app/server

# add project
ADD ./ /app/server

# server mvn install
WORKDIR /app/server
RUN mvn clean package -Dmaven.test.skip=true -U

###

FROM openjdk:8 AS app

WORKDIR /app
COPY --from=build /app/server/server/target/spring-boot-angular-sample-1.0.0-RELEASE.jar .

EXPOSE 8080

# server run
ENTRYPOINT ["java"]
CMD ["-Xms4g", "-Xmx6g", "-jar", "spring-boot-angular-sample-1.0.0-RELEASE.jar"]
