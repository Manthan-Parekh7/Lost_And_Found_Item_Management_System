package com.ce.LostAndFoundManagement.LostAndFoundManagement.entity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToClaimStatusConverter implements Converter<String, ClaimStatus> {
    @Override
    public ClaimStatus convert(String source) {
        return ClaimStatus.fromString(source);
    }
}
