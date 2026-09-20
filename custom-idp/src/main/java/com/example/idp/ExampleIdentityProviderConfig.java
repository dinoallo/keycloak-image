package com.example.idp;

import org.keycloak.models.IdentityProviderModel;

/**
 * Configuration model for the Example identity provider.
 *
 * <p>Extends the base {@link IdentityProviderModel} with a placeholder
 * download URL for the real provider binary.  The URL can be overridden
 * at runtime through the Keycloak admin console.</p>
 */
public class ExampleIdentityProviderConfig extends IdentityProviderModel {

    public static final String PROVIDER_BINARY_URL_PROP = "providerBinaryUrl";
    public static final String DEFAULT_BINARY_URL =
            "https://example.com/downloads/identity-provider-binary.jar";

    public ExampleIdentityProviderConfig() {
        super();
    }

    public ExampleIdentityProviderConfig(IdentityProviderModel model) {
        super(model);
    }

    /** URL where the actual identity provider binary can be downloaded. */
    public String getProviderBinaryUrl() {
        return getConfig()
                .getOrDefault(PROVIDER_BINARY_URL_PROP, DEFAULT_BINARY_URL);
    }

    public void setProviderBinaryUrl(String url) {
        getConfig().put(PROVIDER_BINARY_URL_PROP, url);
    }
}
