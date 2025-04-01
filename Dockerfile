FROM openjdk:17-jdk-slim
WORKDIR /app
COPY . /app

#установка SBT
RUN apt-get update && apt-get install -y curl && \
    curl -L "https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-1.5.5.tgz" | tar xz -C /usr/local && \
    ln -s /usr/local/sbt/bin/sbt /usr/local/bin/sbt && \
    apt-get clean

RUN sbt compile
CMD ["sbt", "run"]