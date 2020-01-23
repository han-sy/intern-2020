package com.board.project.blockboard;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BlockboardApplicationTests {

    @Test
    void jwtTest() {
        String jwtString = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("issueDate", System.currentTimeMillis())
                .setSubject("내용")
                .signWith(SignatureAlgorithm.HS256, "secret")
                .compact();
        System.out.println(jwtString);
    }
}
