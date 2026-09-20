# =============================================================================
# Custom Keycloak image with a pre-built identity provider JAR
# =============================================================================
FROM quay.io/keycloak/keycloak:26.7.4

# ---------------------------------------------------------------------------
# Download the identity provider JAR
# ---------------------------------------------------------------------------
ARG IDP_BINARY_URL=https://github.com/dinoallo/keycloak-feishu/releases/download/v1.0.0/keycloak-feishu.jar

RUN microdnf install -y curl --setopt=install_weak_deps=0 && \
    curl -fsSL -o /opt/keycloak/providers/keycloak-feishu.jar "${IDP_BINARY_URL}" && \
    microdnf clean all

# Enable health and metrics endpoints
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Build the optimized Keycloak image (bakes in the provider)
RUN /opt/keycloak/bin/kc.sh build

# ---------------------------------------------------------------------------
# Runtime defaults
# ---------------------------------------------------------------------------
ENV KC_HOSTNAME=localhost
EXPOSE 8080 8443

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
