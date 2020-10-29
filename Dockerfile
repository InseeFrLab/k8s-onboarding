FROM adoptopenjdk/openjdk11:alpine-jre
ADD target/k8s-onboarding-0.0.1-SNAPSHOT.jar /app/k8s-onboarding.jar
CMD ["java","-jar","/app/k8s-onboarding.jar"]