FROM eclipse-temurin:17.0.17_10-jdk-alpine
RUN apk add --no-cache coreutils
ADD scripts/x509.sh /app/x509.sh
ADD target/k8s-onboarding-0.0.1-SNAPSHOT.jar /app/k8s-onboarding.jar
CMD ["/app/x509.sh","java","-jar","/app/k8s-onboarding.jar"]
