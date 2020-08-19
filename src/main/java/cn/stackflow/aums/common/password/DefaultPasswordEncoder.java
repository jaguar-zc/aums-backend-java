package cn.stackflow.aums.common.password;

import org.springframework.util.DigestUtils;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 14:23
 */
public class DefaultPasswordEncoder implements PasswordEncoder {


    @Override
    public String encode(String rawPassword) {
        return DigestUtils.md5DigestAsHex(rawPassword.getBytes());
    }

    @Override
    public String encode(String salt, String rawPassword) {
        return DigestUtils.md5DigestAsHex((rawPassword + salt).getBytes());
    }

    public static void main(String[] args) {
        System.out.println(new DefaultPasswordEncoder().encode("33DAFA20", "123456"));
    }
}
