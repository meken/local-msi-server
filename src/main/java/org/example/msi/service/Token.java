package org.example.msi.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aad.msal4j.IAuthenticationResult;

public class Token {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_on")
    private long expiresOn;

    @JsonProperty("resource")
    private String resource;

    public Token(IAuthenticationResult result) {
        this.accessToken = result.accessToken();
        this.expiresOn = result.expiresOnDate().getTime();
        this.resource = result.scopes();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresOn() {
        return expiresOn;
    }

    public String getResource() {
        return resource;
    }

}
