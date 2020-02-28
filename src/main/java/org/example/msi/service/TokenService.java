package org.example.msi.service;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    @Autowired
    private ConfidentialClientApplication app;

    public Token getToken(String resource) throws TokenRetrievalException {
        ClientCredentialParameters params = ClientCredentialParameters.builder(
                Collections.singleton(String.format("%s/.default", resource))).build();
        CompletableFuture<IAuthenticationResult> future = app.acquireToken(params);
        try {
            return new Token(future.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new TokenRetrievalException(e);
        }
    }
}
