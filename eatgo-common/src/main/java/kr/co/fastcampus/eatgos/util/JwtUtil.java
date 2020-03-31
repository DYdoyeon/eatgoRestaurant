package kr.co.fastcampus.eatgos.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;


public class JwtUtil {
    private final String secret;
    private Key key;
    public JwtUtil(String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());

        this.secret = secret;
    }

    public String createToken(Long userId, String name) {

        String token = Jwts.builder()
                .claim("userId",userId)
                .claim("name",name)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }
}
