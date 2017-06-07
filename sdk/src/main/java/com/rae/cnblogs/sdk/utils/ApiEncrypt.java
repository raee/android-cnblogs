package com.rae.cnblogs.sdk.utils;

import android.util.Base64;

import com.rae.cnblogs.sdk.config.CnblogSdkConfig;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * 证书加密
 * Created by ChenRui on 2017/1/13 0013 12:55.
 */
public final class ApiEncrypt {

    private ApiEncrypt(){}

    // 公钥加密
    public static String encrypt(String content) {
        try {
            // 加载公钥
            X509EncodedKeySpec data = new X509EncodedKeySpec(Base64.decode(CnblogSdkConfig.API_PUB_KEY_BYTE, Base64.DEFAULT));
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PublicKey key = factory.generatePublic(data);

            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptData = cipher.doFinal(content.getBytes());

            return Base64.encodeToString(encryptData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;

    }

}

