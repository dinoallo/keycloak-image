# keycloak-image

A custom **Keycloak 26.7.4** Docker image with a built-in custom identity provider.

## Image

| Tag | Base | Provider |
|-----|------|----------|
| `latest` | `quay.io/keycloak/keycloak:26.7.4` | `example-identity-provider` |

## Usage

```bash
# Start in development mode
docker run --rm -p 8080:8080 keycloak-custom:latest start-dev

# Start with a production database
docker run --rm -p 8080:8080 \
  -e KC_DB=postgres \
  -e KC_DB_URL=jdbc:postgresql://host/db \
  -e KC_DB_USERNAME=... \
  -e KC_DB_PASSWORD=... \
  -e KC_HOSTNAME=mykeycloak.example.com \
  keycloak-custom:latest start
```

## Custom Identity Provider

The image includes a placeholder identity provider registered as:

- **Provider ID**: `example-identity-provider`
- **Display name**: `Example Identity Provider`

It can be configured in the Keycloak admin console under **Identity Providers** →
**Add Provider** → **Example Identity Provider**.

### Configurable property

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `Provider Binary URL` | String | `https://example.com/downloads/identity-provider-binary.jar` | **TODO**: Replace with the actual URL of your identity provider binary. |

## Development

### Project structure

```
keycloak-image/
├── Dockerfile                          # Multi-stage build
├── .dockerignore
├── custom-idp/                         # Custom identity provider source
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/example/idp/
│       │   ├── ExampleIdentityProvider.java
│       │   ├── ExampleIdentityProviderConfig.java
│       │   └── ExampleIdentityProviderFactory.java
│       └── resources/META-INF/services/
│           └── org.keycloak.broker.provider.IdentityProviderFactory
└── README.md
```

### Build locally

```bash
docker build -t keycloak-custom:latest .
```

### Customizing the provider binary URL at build time

```bash
docker build \
  --build-arg IDP_BINARY_URL=https://your-server.com/provider-binary.jar \
  -t keycloak-custom:latest .
```

## Next steps

1. **Replace the placeholder provider** with your real identity provider
   implementation.
2. Update the `Provider Binary URL` config property to point to your actual
   binary download URL.
3. Rebuild and test the identity flow.
