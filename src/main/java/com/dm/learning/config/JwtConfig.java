package com.dm.learning.config;

import org.springframework.context.annotation.Configuration;

//    TODO: If keycloak, modify this file
@Configuration
public class JwtConfig {

    //    @Value("${LC_SECRET_KEY}")
//    private String SECRET_KEY;
// Ensure this is a valid Base64-encoded public key without PEM headers
    private static final String RSA_PUBLIC_KEY_PEM =
            "-----BEGIN PUBLIC KEY-----"
                    + "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA4f5wg5l2hKsTeNem/V41" +
                    "fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2h" +
                    "KsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeN" +
                    "em/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V4" +
                    "1fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJ" +
                    "m6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8" +
                    "rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5g5l2hKsTeNem/V41fGnJm6gO8rH5f5" +
                    "g5l2hKsTeNem/V41fGnJm6gO8"
                    + "-----END PUBLIC KEY-----";

//    @Bean
//    public JwtDecoder jwtDecoder() throws NoSuchAlgorithmException, InvalidKeySpecException {
//        RSAPublicKey publicKey = getPublicKeyFromPem(RSA_PUBLIC_KEY_PEM);
//        return NimbusJwtDecoder.withPublicKey(publicKey).build();
//    }
//
//    private RSAPublicKey getPublicKeyFromPem(String pem) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        try {
//            // Remove PEM headers and ensure correct format
//            String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "")
//                    .replace("-----END PUBLIC KEY-----", "")
//                    .replaceAll("\\s+", "");  // Remove whitespace
//
//            System.out.println("BASEE LENGTH"+Base64.getDecoder().decode(publicKeyPEM).length);
//            // Decode Base64 properly
//            byte[] encodedKey = Base64.getDecoder().decode(publicKeyPEM);
//
//            // Generate RSA Public Key
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
//            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Invalid Base64 encoding in the public key", e);
//        } catch (Exception e) {
//            throw new RuntimeException("Error while parsing RSA public key", e);
//        }
//    }
}
