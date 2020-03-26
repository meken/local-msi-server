package org.example.msi.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aad.msal4j.IAuthenticationResult;

public class Token {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_on")
    private String expiresOn;

    @JsonProperty("resource")
    private String resource;

    public Token(IAuthenticationResult result) {
        this.accessToken = result.accessToken();
        DateFormat df = new SimpleDateFormat("M/d/yyyy h:mm:ss a X");
        this.expiresOn = df.format(result.expiresOnDate());
        this.resource = result.scopes();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpiresOn() {
        return expiresOn;
    }

    public String getResource() {
        return resource;
    }

}
