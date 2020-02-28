/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.msi;

import java.net.MalformedURLException;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IClientCredential;
import org.example.msi.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MSIApplication {
	@Value("${app.clientId}")
	private String clientId;
	@Value("${app.clientSecret}")
	private String clientSecret;
	@Value("${app.tenantId}")
	private String tenantId;

	public static void main(String[] args) {
		SpringApplication.run(MSIApplication.class, args);
	}

	@Bean
	public ConfidentialClientApplication confidentialClientApplication() {
		String authority = String.format("https://login.microsoftonline.com/%s", tenantId);
		IClientCredential clientCredential = ClientCredentialFactory.createFromSecret(clientSecret);
		try {
			return ConfidentialClientApplication.builder(clientId, clientCredential).
				authority(authority).build();
		} catch (MalformedURLException e) {
			// Shouldn't happen as we're constructing the URL, only if tenantId is problematic
			throw new RuntimeException(e);
		}
	}
}
