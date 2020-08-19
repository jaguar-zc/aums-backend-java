package cn.stackflow.aums.common.password;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 14:23
 */
public interface PasswordEncoder {

    String encode(String rawPassword);

    String encode(String salt, String rawPassword);
}
