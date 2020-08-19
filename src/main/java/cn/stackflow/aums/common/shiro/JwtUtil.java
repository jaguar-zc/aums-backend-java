package cn.stackflow.aums.common.shiro;



import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * 总的来说，工具类中有三个方法
 * 获取JwtToken，获取JwtToken中封装的信息，判断JwtToken是否存在
 * 1. encode()，参数是=签发人，存在时间，一些其他的信息=。返回值是JwtToken对应的字符串
 * 2. decode()，参数是=JwtToken=。返回值是荷载部分的键值对
 * 3. isVerify()，参数是=JwtToken=。返回值是这个JwtToken是否存在
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-03 16:43
 * */
@Slf4j
public class JwtUtil {


    //创建默认的秘钥和算法，供无参的构造方法使用
    private static final String defaultbase64EncodedSecretKey = "badbabe";
    private static final SignatureAlgorithm defaultsignatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtUtil() {
        this(defaultbase64EncodedSecretKey, defaultsignatureAlgorithm);
    }


    // 生成签名是所使用的秘钥
    private final String base64EncodedSecretKey;

    // 生成签名的时候所使用的加密算法
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtUtil(String secretKey, SignatureAlgorithm signatureAlgorithm) {
        this.base64EncodedSecretKey = Base64.encodeBase64String(secretKey.getBytes());
        this.signatureAlgorithm = signatureAlgorithm;
    }

    /**
     * 生成 JWT Token 字符串
     *
     * @param issname       签发人名称
     * @param ttlMillis jwt 过期时间
     * @param claims    额外添加到荷部分的信息。
     *                  例如可以添加用户名、用户ID、用户（加密前的）密码等信息
     */
    public String encode(String issname, long ttlMillis, Map<String,Object> claims) {
        if(claims == null){
            claims = new HashMap<String,Object>();
        }

        // 签发时间（iat）：荷载部分的标准字段之一
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // 下面就是在为payload添加各种标准声明和私有声明了
        JwtBuilder builder = Jwts.builder()
                // 荷载部分的非标准字段/附加字段，一般写在标准的字段之前。
                .setClaims(claims)
                // JWT ID（jti）：荷载部分的标准字段之一，JWT 的唯一性标识，虽不强求，但尽量确保其唯一性。
                .setId(UUID.randomUUID().toString())
                // 签发时间（iat）：荷载部分的标准字段之一，代表这个 JWT 的生成时间。
                .setIssuedAt(now)
                // 签发人（iss）：荷载部分的标准字段之一，代表这个 JWT 的所有者。通常是 username、userid 这样具有用户代表性的内容。
                .setSubject(issname)
                // 设置生成签名的算法和秘钥
                .signWith(signatureAlgorithm, base64EncodedSecretKey);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 过期时间（exp）：荷载部分的标准字段之一，代表这个 JWT 的有效期。
            builder.setExpiration(exp);
        }
//        return "Bearer "+builder.compact();
        return builder.compact();
    }


    /**
     * JWT Token 由 头部 荷载部 和 签名部 三部分组成。签名部分是由加密算法生成，无法反向解密。
     * 而 头部 和 荷载部分是由 Base64 编码算法生成，是可以反向反编码回原样的。
     * 这也是为什么不要在 JWT Token 中放敏感数据的原因。
     *
     * @param jwtToken 加密后的token
     * @return claims 返回荷载部分的键值对
     */
    public Claims decode(String jwtToken) {

        return Jwts.parser()
                // 设置签名的秘钥
                .setSigningKey(base64EncodedSecretKey)
                // 设置需要解析的 jwt
                .parseClaimsJws(jwtToken)
                .getBody();
    }


    /**
     * 校验 token
     * 在这里可以使用官方的校验，或，
     * 自定义校验规则，例如在 token 中携带密码，进行加密处理后和数据库中的加密密码比较。
     *
     * @param jwtToken 被校验的 jwt Token
     */
    public boolean isVerify(String jwtToken) {
        try {
            Algorithm algorithm = null;

            switch (signatureAlgorithm) {
                case HS256:
                    algorithm = Algorithm.HMAC256(Base64.decodeBase64(base64EncodedSecretKey));
                    break;
                default:
                    throw new RuntimeException("不支持该算法");
            }

            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(jwtToken);  // 校验不通过会抛出异常
        } catch (TokenExpiredException e) {
            log.error("Token expired :{}",e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Token error:{}",e);
            return false;
        }
        /*
            // 得到DefaultJwtParser
            Claims claims = decode(jwtToken);

            if (claims.get("password").equals(user.get("password"))) {
                return true;
            }
        */

        return true;
    }
}
