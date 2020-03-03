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

package org.example.msi.web;

import org.example.msi.service.Token;
import org.example.msi.service.TokenRetrievalException;
import org.example.msi.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TokenController {
	private static final Logger logger = LoggerFactory.getLogger(TokenController.class);

	@Autowired
	private TokenService tokenService;

	@GetMapping("/msi/token")
	public ResponseEntity<Token> token(@RequestParam String resource) {
		try {
			logger.debug("Requested a token for resource {}", resource);
			Token token = tokenService.getToken(resource);
			logger.debug("Successfully retrieved the token");
			return ResponseEntity.ok(token);
		} catch (TokenRetrievalException e) {
			logger.error("Error while retrieving token", e);
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}
