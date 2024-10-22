package com.reboot_course.firstcome_system.auth.encryption.converter;

import com.reboot_course.firstcome_system.auth.encryption.config.EncryptionProperties;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Converter
@Component
public class EmailConverter implements AttributeConverter<String, String> {

    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    private final SecretKeySpec secretKey;

    @Autowired
    public EmailConverter(EncryptionProperties encryptionProperties) {
        String emailKey = encryptionProperties.getKey();
        this.secretKey = new SecretKeySpec(emailKey.getBytes(StandardCharsets.UTF_8), "AES");
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new RuntimeException("이메일 암호화 과정에서 오류가 발생했습니다.", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("이메일 복호화 과정에서 오류가 발생했습니다.", e);
        }
    }
}