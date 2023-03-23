package util;

import org.example.core.util.BCryptEncryptor;
import org.example.core.util.Encryptor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BCryptEncryptorTest {
    @Test
    void test() {
        final String origin = "password";
        final Encryptor encryptor = new BCryptEncryptor();
        final String hash = encryptor.encrypt(origin);

        assertTrue(encryptor.isMatch(origin, hash));

        final String origin2 = "passwordd";
        assertFalse(encryptor.isMatch(origin2, hash));
    }
}
