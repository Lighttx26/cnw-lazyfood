package lazyfood.demo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class IdGenerator {
    public static String generateId(String namespace, String name) {
        // Assuming namespace and name are both strings
        UUID namespaceUUID = UUID.nameUUIDFromBytes(namespace.getBytes());

        // Generate UUID v3 based on namespace and name
        UUID userId = UUID.nameUUIDFromBytes(generateBytes(namespaceUUID, name));

        // Convert UUID to string and remove hyphens
        return userId.toString().replaceAll("-", "");
    }

    private static byte[] generateBytes(UUID namespace, String name) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(namespace.toString().getBytes());
            md.update(name.getBytes());
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating UUID v3", e);
        }
    }
}
