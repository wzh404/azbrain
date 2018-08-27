package com.blueocean.azbrain.common.kcode;

import java.util.Optional;

public class TestKCodeService implements KCodeService {
    @Override
    public Optional<String> getKCode(String code) {
        return Optional.ofNullable(code);
    }
}
