FROM registry.xavi.lasalle.edu/docker-environments/sbt-sonar

RUN apk update && apk upgrade && apk add --no-cache unzip

RUN mkdir /workspace

WORKDIR /workspace

COPY . .

RUN sbt compile
RUN sbt test
RUN sbt dist

RUN unzip target/universal/feed-processor-1.0.0-SNAPSHOT.zip -d /root/app
RUN mv /root/app/feed-processor-1.0.0-SNAPSHOT/* /root/app
RUN rm -R /root/app/feed-processor-1.0.0-SNAPSHOT
RUN rm -R /workspace

WORKDIR /root/app

EXPOSE 9000