# keycloak-image

A custom **Keycloak 26.7.4** Docker image with a pre-built identity provider JAR
downloaded at build time.

The image is pre-optimized (via `kc.sh build`) for **PostgreSQL** as the default
database, so production usage with `KC_DB=postgres` starts without any
configuration-mismatch warning.

## Image

| Tag   | Base                                          | Provider            |
|-------|-----------------------------------------------|---------------------|
| `main` / `latest` | `quay.io/keycloak/keycloak:26.7.4` | `keycloak-feishu` |

## Usage

```bash
# Start in development mode (auto-detects H2 / dev-file)
docker run --rm -p 8080:8080 ghcr.io/dinoallo/keycloak-image:latest start-dev

# Start with PostgreSQL (no config-mismatch warning — pre-optimized)
docker run --rm -p 8080:8080 \
  -e KC_DB=postgres \
  -e KC_DB_URL=jdbc:postgresql://host/db \
  -e KC_DB_USERNAME=... \
  -e KC_DB_PASSWORD=... \
  -e KC_HOSTNAME=mykeycloak.example.com \
  ghcr.io/dinoallo/keycloak-image:latest start
```

> **Note**: The image is pre-optimized with `KC_DB=postgres`. If you use a
> different database at runtime (e.g. `KC_DB=dev-file`), Keycloak will detect
> the mismatch and automatically rebuild — the first startup will be slightly
> slower, and you'll see a `Changes detected in configuration` informational
> message. This is harmless.

## Custom Identity Provider

The identity provider JAR is downloaded from a configurable URL during the
Docker build. By default it fetches:

```
https://github.com/dinoallo/keycloak-feishu/releases/download/v1.0.0/keycloak-feishu.jar
```

You can override it with a different URL at build time:

```bash
docker build \
  --build-arg IDP_BINARY_URL=https://your-server.com/your-provider.jar \
  -t keycloak-custom:latest .
```

## Build

### Locally

```bash
docker build -t keycloak-custom:latest .
```

### Via GitHub Actions

Push to `main` or create a tag (`v*.*.*`) to trigger the workflow.
Manual trigger with a custom URL is also supported via **Actions** →
**Build Keycloak Image** → **Run workflow**.

When triggering manually, you can optionally set the `IDP_BINARY_URL` input to
override the provider JAR URL.

## Project structure

```
keycloak-image/
├── Dockerfile
├── .dockerignore
├── .github/workflows/build-image.yml
└── README.md
```
