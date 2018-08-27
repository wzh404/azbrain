package com.blueocean.azbrain.common.kcode;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Optional;

public class ProdKCodeService implements KCodeService {
    @Override
    public Optional<String> getKCode(String code) {
        try {
            String kcode = new String(Base64.getDecoder().decode(code), "utf-8");
            return Optional.ofNullable(kcode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
