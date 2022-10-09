FROM openjdk:11-jre-slim

RUN apt-get update
RUN apt-get install ffmpeg -y
RUN apt-get install sox -y
RUN apt-get install libsox-fmt-mp3 -y

WORKDIR /app/base
COPY target/lib ./lib
COPY target/classes/ .
COPY binary/ ./binary
COPY container/* .
RUN chmod a+x ./binary/*

CMD ["bash", "run.sh"]