# Stage 1: Build the application
FROM gradle:jdk17 as builder

# Copy the source code into the container
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Run gradle build, which creates the WAR file
RUN gradle build --no-daemon


# Stage 2: Deploy the application
FROM quay.io/wildfly/wildfly:latest

# Set the working directory inside the container
WORKDIR /opt/jboss/wildfly

# Before copying the new WAR file, clear the deployment directory
RUN rm -rf /opt/jboss/wildfly/standalone/deployments/*

# Copy the built war file from the builder stage
COPY --from=builder /home/gradle/src/build/libs/*.war /opt/jboss/wildfly/standalone/deployments/

# Add PostgreSQL driver and modify configuration files
COPY ./wildfly_files/postgresql/main/ /opt/jboss/wildfly/modules/system/layers/base/org/postgresql/main/
COPY ./wildfly_files/standalone.xml /opt/jboss/wildfly/standalone/configuration/

# Expose the port the app runs on
EXPOSE 8080

# Set the default command to run on boot
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]
