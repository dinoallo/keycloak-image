package com.example.idp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.keycloak.broker.provider.AbstractIdentityProviderFactory;
import org.keycloak.models.IdentityProviderModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.util.JsonSerialization;

public class ExampleIdentityProviderFactory
        extends AbstractIdentityProviderFactory<ExampleIdentityProvider> {

    public static final String PROVIDER_ID = "example-identity-provider";
    public static final String PROVIDER_NAME = "Example Identity Provider";

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getName() {
        return PROVIDER_NAME;
    }

    @Override
    public ExampleIdentityProvider create(KeycloakSession session,
                                          IdentityProviderModel model) {
        ExampleIdentityProviderConfig config = new ExampleIdentityProviderConfig(model);
        return new ExampleIdentityProvider(session, config);
    }

    @Override
    public IdentityProviderModel createConfig() {
        return new ExampleIdentityProviderConfig();
    }

    public ExampleIdentityProviderConfig parseConfig(KeycloakSession session,
                                                     InputStream inputStream)
            throws IOException {
        return JsonSerialization.readValue(inputStream,
                                           ExampleIdentityProviderConfig.class);
    }

    public List<ProviderConfigProperty> getConfigProperties() {
        ProviderConfigProperty binaryUrl = new ProviderConfigProperty(
                ExampleIdentityProviderConfig.PROVIDER_BINARY_URL_PROP,
                "Provider Binary URL",
                "Download URL for the real identity provider binary. "
              + "Replace the placeholder with the actual binary URL.",
                ProviderConfigProperty.STRING_TYPE,
                ExampleIdentityProviderConfig.DEFAULT_BINARY_URL,
                false
        );
        return Arrays.asList(binaryUrl);
    }
}
