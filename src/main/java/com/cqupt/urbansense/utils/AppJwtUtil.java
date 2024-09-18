package com.cqupt.urbansense.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;

public class AppJwtUtil {

    // TOKEN的有效期两小时（S）
    private static final int TOKEN_TIME_OUT = 7_200;
    // 加密KEY
    private static final String TOKEN_ENCRY_KEY = "yeidGo5Jd+V65aqANkIHNDAli0mU8g+AC8sPtfdwp+XbUZOFKshd+BIREIw9kdQJQbjrJjxOzXlDoY29vo9Q9A==";
    // 最小刷新间隔(S)
    private static final int REFRESH_TIME = 300;

    // 生产ID
    public static String getToken(Long id){
        Map<String, Object> claimMaps = new HashMap<>();
        claimMaps.put("id",id);
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date(currentTime))  //签发时间
                .setSubject("system")  //说明
                .setIssuer("heima") //签发者信息
                .setAudience("app")  //接收用户
                .compressWith(CompressionCodecs.GZIP)  //数据压缩方式
                .signWith(SignatureAlgorithm.HS512, generalKey()) //加密方式
                .setExpiration(new Date(currentTime + TOKEN_TIME_OUT * 1000))  //过期时间戳
                .addClaims(claimMaps) //cla信息
                .compact();
    }

    /**
     * 获取token中的claims信息
     *
     * @param token
     * @return
     */
    private static Jws<Claims> getJws(String token) {
            return Jwts.parser()
                    .setSigningKey(generalKey())
                    .parseClaimsJws(token);
    }

    /**
     * 获取payload body信息
     *
     * @param token
     * @return
     */
    public static Claims getClaimsBody(String token) {
        try {
            return getJws(token).getBody();
        }catch (ExpiredJwtException e){
            return null;
        }
    }

    /**
     * 获取hearder body信息
     *
     * @param token
     * @return
     */
    public static JwsHeader getHeaderBody(String token) {
        return getJws(token).getHeader();
    }

    /**
     * 是否过期
     *
     * @param claims
     * @return -1：有效，0：有效，1：过期，2：过期
     */
    public static int verifyToken(Claims claims) {
        if(claims==null){
            return 1;
        }
        try {
            claims.getExpiration()
                    .before(new Date());
            // 需要自动刷新TOKEN
            if((claims.getExpiration().getTime()-System.currentTimeMillis())>REFRESH_TIME*1000){
                return -1;
            }else {
                return 0;
            }
        } catch (ExpiredJwtException ex) {
            return 1;
        }catch (Exception e){
            return 2;
        }
    }

    public static SecretKey generalKey() {
        // 将 TOKEN_ENCRY_KEY 转换为字节数组
        byte[] encodedKey = Base64.getDecoder().decode(TOKEN_ENCRY_KEY);
        // 根据字节数组生成 SecretKey，使用 "HmacSHA512" 作为算法
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "HmacSHA512");
    }



        public static void main(String[] args) {
            // 使用 HS512 算法生成一个至少 512 位的密钥
            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
            // 将密钥转换为 Base64 编码字符串
            String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println("Generated Key: " + base64Key);
        }


}
