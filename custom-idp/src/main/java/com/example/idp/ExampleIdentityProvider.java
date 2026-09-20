package com.example.idp;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import org.keycloak.broker.provider.AbstractIdentityProvider;
import org.keycloak.broker.provider.AuthenticationRequest;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityBrokerException;
import org.keycloak.events.EventBuilder;
import org.keycloak.models.FederatedIdentityModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserSessionModel;

public class ExampleIdentityProvider
        extends AbstractIdentityProvider<ExampleIdentityProviderConfig> {

    private static final String PLACEHOLDER_LOGIN_URL =
            "https://example.com/auth/placeholder-login";

    public ExampleIdentityProvider(KeycloakSession session,
                                   ExampleIdentityProviderConfig config) {
        super(session, config);
    }

    public Response performLogin(AuthenticationRequest request) {
        try {
            return Response.temporaryRedirect(
                    new java.net.URI(PLACEHOLDER_LOGIN_URL)).build();
        } catch (Exception e) {
            throw new IdentityBrokerException(
                    "Failed to build placeholder login URI", e);
        }
    }

    public Response callback(UriInfo uriInfo,
                             AuthenticationRequest request,
                             EventBuilder event) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Callback handler: placeholder — "
                      + "replace with the real identity provider logic.")
                .build();
    }

    @Override
    public Response retrieveToken(KeycloakSession session,
                                  FederatedIdentityModel federatedIdentityModel) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Token retrieval (2-param): placeholder — "
                      + "replace with the real identity provider logic.")
                .build();
    }

    @Override
    public Response retrieveToken(KeycloakSession session,
                                  FederatedIdentityModel federatedIdentityModel,
                                  UserSessionModel userSessionModel,
                                  UserModel userModel) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Token retrieval (4-param): placeholder — "
                      + "replace with the real identity provider logic.")
                .build();
    }

    @Override
    public void updateBrokeredUser(KeycloakSession session,
                                   RealmModel realm,
                                   UserModel user,
                                   BrokeredIdentityContext context) {
        super.updateBrokeredUser(session, realm, user, context);
    }

    @Override
    public void importNewUser(KeycloakSession session,
                              RealmModel realm,
                              UserModel user,
                              BrokeredIdentityContext context) {
        super.importNewUser(session, realm, user, context);
    }

    @Override
    public void preprocessFederatedIdentity(KeycloakSession session,
                                            RealmModel realm,
                                            BrokeredIdentityContext context) {
        super.preprocessFederatedIdentity(session, realm, context);
    }

    public String getDownloadUrl() {
        String url = getConfig().getProviderBinaryUrl();
        return url != null && !url.isBlank()
                ? url
                : PLACEHOLDER_LOGIN_URL;
    }
}
