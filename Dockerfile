# Base Image
FROM ubuntu

# Set environment variables to suppress prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Update and install required packages
RUN apt-get update && \
    apt-get install -y openjdk-11-jdk wget curl mysql-server && \
    apt-get clean

# Configure MySQL
RUN service mysql start

# Install Apache Tomcat 9
RUN mkdir -p /opt/tomcat && \
    wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.97/bin/apache-tomcat-9.0.97.tar.gz -O /tmp/tomcat.tar.gz && \
    tar -xvzf /tmp/tomcat.tar.gz -C /opt/tomcat --strip-components=1 && \
    rm -f /tmp/tomcat.tar.gz

# RUN mysql -u root -proot < /opt/tomcat/webapps/ROOT/WEB-INF/oasis.sql

# Expose ports for MySQL and Tomcat
EXPOSE 3306 8080

# Copy application files to Tomcat's webapps directory
COPY . /opt/tomcat/webapps/ROOT/

# Start MySQL and Tomcat
CMD ["sh", "-c", "service mysql start && /opt/tomcat/bin/catalina.sh run"]
