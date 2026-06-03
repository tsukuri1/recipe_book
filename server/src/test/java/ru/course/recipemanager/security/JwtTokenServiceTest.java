package ru.course.recipemanager.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import ru.course.recipemanager.security.JwtTokenService.JwtPrincipal;

class JwtTokenServiceTest {
    @Test
    void createsSignedJwtAndParsesPrincipal() {
        JwtTokenService service = new JwtTokenService("test-secret");

        String token = service.createToken("admin@ladushki.app", "ADMIN");
        Optional<JwtPrincipal> principal = service.parse(token);

        assertEquals(3, token.split("\\.").length);
        assertTrue(principal.isPresent());
        assertEquals("admin@ladushki.app", principal.get().email());
        assertEquals("ADMIN", principal.get().role());
    }

    @Test
    void rejectsTokenWithChangedSignature() {
        JwtTokenService service = new JwtTokenService("test-secret");
        String token = service.createToken("user@ladushki.app", "USER");
        String tampered = token.substring(0, token.length() - 2) + "xx";

        assertFalse(service.parse(tampered).isPresent());
    }
}
