FROM openjdk:8-jdk
COPY . /bin
COPY ./test-project1.mp4 /bin
WORKDIR /bin

RUN mkdir /bin/TestVideoFrames
RUN apt-get update && apt-get install maven -y
RUN mvn package
CMD ["java", "-cp", "/bin/target/muic.backend.project1.milestone1-1.0-SNAPSHOT.jar", "MainApplication"]
RUN echo $PATH