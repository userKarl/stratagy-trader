package com.zd.common.utils;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JWT {

    /**
    * 公共密钥
    * */
    private static String SECRET  = "5Li66L+Z576O5aW955qE5LiW55WM54yu5LiK56Wd56aP";

    /**
     * 生成token
     * */
    public static String createToken(String clientNo) throws UnsupportedEncodingException {
        //签发时间
        Date date = new Date();
        //过期时间
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE,1);
        Date expireDate = now.getTime();
        HashMap<String, Object> map = new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");
        String token = com.auth0.jwt.JWT.create().withHeader(map)
                                        .withClaim("org","上海直达")
                                        .withClaim("clientNo",clientNo)
                                        .withIssuedAt(date)
                                        .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public static Map<String,Claim> verifyToken(String token) throws UnsupportedEncodingException {
        JWTVerifier verifier = com.auth0.jwt.JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        }catch (Exception e){
            throw new RuntimeException("登录信息不合法 ,请重新登录");
        }
        return jwt.getClaims();
    }


    public static String ErrorResult(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("result",false);
        map.put("message","请重新登录");
        String jsonResult = JSON.toJSONString(map);
        return jsonResult;
    }

}
