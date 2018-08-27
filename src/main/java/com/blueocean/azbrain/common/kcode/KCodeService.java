package com.blueocean.azbrain.common.kcode;

import java.util.Optional;

public interface KCodeService {
    Optional<String> getKCode(String code);
}
