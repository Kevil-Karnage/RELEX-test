package generator;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

public class SecretKeyGenerator {
    public static String generateSecretKey(String username, String email) {
        String usernamePart = DigestUtils.md5DigestAsHex(username.getBytes(StandardCharsets.UTF_8));
        String emailPart = DigestUtils.md5DigestAsHex(email.getBytes(StandardCharsets.UTF_8));
        return usernamePart + "-" + emailPart;
    }
}
