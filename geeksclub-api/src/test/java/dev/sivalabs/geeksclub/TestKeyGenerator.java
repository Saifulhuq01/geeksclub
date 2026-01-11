package dev.sivalabs.geeksclub;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TestKeyGenerator {

    private static Path privateKeyPath;
    private static Path publicKeyPath;

    public static void generateKeys() {
        if (privateKeyPath != null && publicKeyPath != null) {
            return;
        }

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            Path tempDir = Files.createTempDirectory("test-certs");
            privateKeyPath = tempDir.resolve("private.pem");
            publicKeyPath = tempDir.resolve("public.pem");

            String privateKeyContent = "-----BEGIN PRIVATE KEY-----\n"
                    + Base64.getMimeEncoder().encodeToString(pair.getPrivate().getEncoded())
                    + "\n-----END PRIVATE KEY-----";

            String publicKeyContent = "-----BEGIN PUBLIC KEY-----\n"
                    + Base64.getMimeEncoder().encodeToString(pair.getPublic().getEncoded())
                    + "\n-----END PUBLIC KEY-----";

            Files.writeString(privateKeyPath, privateKeyContent);
            Files.writeString(publicKeyPath, publicKeyContent);

            System.setProperty("JWT_PRIVATE_KEY", "file:" + privateKeyPath.toAbsolutePath());
            System.setProperty("JWT_PUBLIC_KEY", "file:" + publicKeyPath.toAbsolutePath());

        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException("Failed to generate test keys", e);
        }
    }
}
