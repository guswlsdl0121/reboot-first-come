package com.hyunjin.firstcome_system.auth.encryption.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class PersonalInfoConverter implements AttributeConverter<String, String> {

    private final TextEncryptor textEncryptor;


    @Override
    public String convertToDatabaseColumn(String attribute) {
        return textEncryptor.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return textEncryptor.decrypt(dbData);
    }
}