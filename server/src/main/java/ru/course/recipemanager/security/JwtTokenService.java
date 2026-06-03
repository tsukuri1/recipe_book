package ru.course.recipemanager.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenService {
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final long TOKEN_TTL_SECONDS = 24 * 60 * 60;
    private static final Pattern SUBJECT_PATTERN = Pattern.compile("\"sub\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern ROLE_PATTERN = Pattern.compile("\"role\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EXPIRATION_PATTERN = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)");

    private final String secret;

    public JwtTokenService(
        @Value("${security.jwt.secret:ladushki-course-project-secret}") String secret
    ) {
        this.secret = secret;
    }

    public String createToken(String email, String role) {
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        long expiresAt = Instant.now().getEpochSecond() + TOKEN_TTL_SECONDS;
        String payload = "{\"sub\":\"" + escape(email) + "\",\"role\":\"" + escape(role) + "\",\"exp\":" + expiresAt + "}";
        String body = base64Url(header.getBytes(StandardCharsets.UTF_8))
            + "."
            + base64Url(payload.getBytes(StandardCharsets.UTF_8));
        return body + "." + sign(body);
    }

    public Optional<JwtPrincipal> parse(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return Optional.empty();
        }

        String body = parts[0] + "." + parts[1];
        if (!sign(body).equals(parts[2])) {
            return Optional.empty();
        }

        String payload = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String email = extract(SUBJECT_PATTERN, payload);
        String role = extract(ROLE_PATTERN, payload);
        String expiresAt = extract(EXPIRATION_PATTERN, payload);
        if (email == null || role == null || expiresAt == null) {
            return Optional.empty();
        }

        if (Long.parseLong(expiresAt) < Instant.now().getEpochSecond()) {
            return Optional.empty();
        }
        return Optional.of(new JwtPrincipal(email, role));
    }

    private String sign(String body) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            return base64Url(mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot sign JWT", exception);
        }
    }

    private String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private String extract(Pattern pattern, String payload) {
        Matcher matcher = pattern.matcher(payload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    public record JwtPrincipal(String email, String role) {}
}
