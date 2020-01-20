package com.board.project.blockboard.util;

import lombok.Data;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;

@Data
public class SessionTokenizer {
    private String userID;
    private int companyID;
    private String serverToken;
    private String key = "slgi3ibu5phi8euf";
    private String token = "server";

    public SessionTokenizer(HttpServletRequest request) throws UnsupportedEncodingException, DecoderException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cookie[] getCookie = request.getCookies();
        AES256Util aes256 = new AES256Util(key);
        URLCodec codec = new URLCodec();

        String decode = "";

        if(getCookie != null) {
            for (Cookie c : getCookie) {
                if (c.getName().equals("sessionID")) {
                    decode = aes256.aesDecode(codec.decode(c.getValue()));
                }
            }
        }

        StringTokenizer tokenizer = new StringTokenizer(decode, "#");
        this.userID = tokenizer.nextToken();
        this.companyID = Integer.parseInt(tokenizer.nextToken());
        this.serverToken = tokenizer.nextToken();
    }
}
