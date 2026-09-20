# =============================================================================
# Stage 1 — Download the identity provider JAR
# =============================================================================
FROM alpine:3.20 AS downloader

ARG IDP_BINARY_URL=https://github.com/dinoallo/keycloak-feishu/releases/download/v1.0.0/keycloak-feishu.jar

RUN apk add --no-cache curl && \
    curl -fsSL -o /provider.jar "${IDP_BINARY_URL}"

# =============================================================================
# Stage 2 — Assemble the Keycloak image
# =============================================================================
FROM quay.io/keycloak/keycloak:26.7.4

COPY --from=downloader /provider.jar /opt/keycloak/providers/keycloak-feishu.jar

# Enable health and metrics endpoints
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# ---------------------------------------------------------------------------
# Pre-optimize the Quarkus build with PostgreSQL as the default database.
# This avoids the "Changes detected in configuration" warning at startup
# when users run with KC_DB=postgres (the most common production setup).
# ---------------------------------------------------------------------------
ENV KC_DB=postgres
RUN /opt/keycloak/bin/kc.sh build

# ---------------------------------------------------------------------------
# Runtime defaults
# ---------------------------------------------------------------------------
EXPOSE 8080 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
