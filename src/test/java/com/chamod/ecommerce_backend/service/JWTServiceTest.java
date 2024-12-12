package com.chamod.ecommerce_backend.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.chamod.ecommerce_backend.model.LocalUser;
import com.chamod.ecommerce_backend.model.dao.LocalUserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;


@SpringBootTest
public class JWTServiceTest {


    @Autowired
    private JWTService jwtService;

    @Autowired
    private LocalUserDAO localUserDAO;


    @Test
    public void testVerificationTokenNotUsableForLogin() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateVerificationJWT(user);
        Assertions.assertNull(jwtService.getUsername(token), "Verification token should not contain username.");
    }


    @Test
    public void testAuthTokenReturnsUsername() {
        LocalUser user = localUserDAO.findByUsernameIgnoreCase("UserA").get();
        String token = jwtService.generateJWT(user);
        Assertions.assertEquals(user.getUsername(), jwtService.getUsername(token), "Token for auth should contain users username.");
    }

    @Test
    public void testJWTNotGeneratedByUs() throws UnsupportedEncodingException {
        String token =
                JWT.create().withClaim("USERNAME", "UserA").sign(Algorithm.HMAC256(
                        "NotTheRealSecret"));
        Assertions.assertThrows(SignatureVerificationException.class,
                () -> jwtService.getUsername(token));
    }

}
