package com.blueocean.azbrain.util;

import com.blueocean.azbrain.common.SessionObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Optional;

public class TokenUtil {
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    private TokenUtil(){
        throw new IllegalStateException("Utility class");
    }
    /**
     * create jwt token by json string of user.
     *
     * @param json
     * @return
     */
    public static Optional<String> createJwtToken(String json){
        if (json == null){
            return Optional.empty();
        }

        String payload = new String(Base64.getEncoder().encode(json.getBytes()));
        StringBuilder token = new StringBuilder(payload);
        String signed = CryptoUtil.signature(payload, AZBrainConstants.DEFAULT_KEY);
        token.append(".");
        token.append(signed);

        return Optional.of(token.toString());
    }

    /**
     * decode jwt payload
     *
     * @param token
     * @return jwt payload(json string of user)
     */
    public static Optional<String> decodeJwt(String token){
        if (token == null) {
            logger.error("Jwt token is null");
            return Optional.empty();
        }

        String[] tokens = token.split("\\.");
        if (tokens == null || tokens.length != 2) {
            logger.error("Invalid jwt token");
            return Optional.empty();
        }

        String signature = tokens[1];
        String signed = CryptoUtil.signature(tokens[0], AZBrainConstants.DEFAULT_KEY);
        if (!signature.equalsIgnoreCase(signed)) {
            logger.error("Invalid signature data");
            return Optional.empty();
        }

        String payload = new String(Base64.getUrlDecoder().decode(tokens[0].getBytes()));
        return Optional.of(payload);
    }

    public static int getUserId(String accessToken){
        Optional<String> opt =  TokenUtil.decodeJwt(accessToken);
        if (opt.isPresent()){
            return SessionObject.asSessionObject(opt.get()).getUserId();
        }

        return 0;
    }
}
