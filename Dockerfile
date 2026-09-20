# =============================================================================
# Stage 1 — Build the custom identity provider JAR
# =============================================================================
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /build

# Install Maven
RUN apt-get update -qq && apt-get install -y -qq maven 2>/dev/null

# Copy the provider source
COPY custom-idp/ ./custom-idp/

# Build the provider JAR
RUN cd custom-idp && mvn -B -q package -DskipTests

# =============================================================================
# Stage 2 — Assemble the Keycloak image
# =============================================================================
FROM quay.io/keycloak/keycloak:26.7.4

# Copy the built provider JAR into Keycloak's providers directory
COPY --from=builder /build/custom-idp/target/keycloak-custom-idp.jar \
     /opt/keycloak/providers/keycloak-custom-idp.jar

# ---------------------------------------------------------------------------
# Build-time configuration
# ---------------------------------------------------------------------------
# TODO: Replace with the actual download URL for the real identity provider
#       binary once it is available.
ARG IDP_BINARY_URL=https://example.com/downloads/identity-provider-binary.jar
ENV KC_IDP_BINARY_URL=${IDP_BINARY_URL}

# Enable health and metrics endpoints (optional)
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Build the optimized Keycloak image (bakes in the provider)
RUN /opt/keycloak/bin/kc.sh build

# ---------------------------------------------------------------------------
# Runtime defaults
# ---------------------------------------------------------------------------
# By default, start in development mode.  Override KC_DB / KC_HOSTNAME etc.
# in production.
ENV KC_HOSTNAME=localhost
EXPOSE 8080 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
