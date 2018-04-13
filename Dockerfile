FROM openjdk:9.0-jdk-slim
EXPOSE 9988
COPY index.html load-image.all.min.js /
COPY target/scala-2.12/imgqueue.jar /root/imgqueue.jar
CMD java -jar /root/imgqueue.jar
# docker build -t thstock/imgqueue:latest .
# docker rm -f imgq; docker run -d --name imgq -p 7777:9988 thstock/imgqueue:latest
