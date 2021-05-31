package org.example.msi.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.aad.msal4j.IAuthenticationResult;

public class Token {
    // Old api version (2017-09-01) requires a specific date format
    private static final DateTimeFormatter MSI_DATE_FORMAT =
            DateTimeFormatter.ofPattern("M/d/yyyy h:mm:ss a xxx");

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_on")
    private String expiresOn;

    @JsonProperty("resource")
    private String resource;

    public Token(IAuthenticationResult result, String resource, String apiVersion) {
        this.accessToken = result.accessToken();
        if ("2017-09-01".equals(apiVersion)) {
            // blob storage (python) sdk expects an offset of +00:00 which is UTC
            ZonedDateTime utc = ZonedDateTime.ofInstant(
                    result.expiresOnDate().toInstant(), ZoneId.of("UTC"));
            this.expiresOn = MSI_DATE_FORMAT.format(utc);
        } else { // 2019-08-01
            this.expiresOn = String.valueOf(result.expiresOnDate().getTime());
        }
        this.resource = resource;
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
