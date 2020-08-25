package cn.stackflow.aums.common.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 11:20
 */
public class RequestUtils {
    /**
     * 获取客户端IP地址，此方法用在proxy环境中
     *
     * @param req
     * @return
     */
    public static String getRemoteAddr(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip)) {
            String[] ips = StringUtils.split(ip, ',');
            if (ips != null) {
                for (String tmpip : ips) {
                    if (StringUtils.isBlank(tmpip)) {
                        continue;
                    }
                    tmpip = tmpip.trim();
                    if (isIPAddr(tmpip) && !tmpip.startsWith("10.") && !tmpip.startsWith("192.168.") && !"127.0.0.1".equals(tmpip)) {
                        return tmpip.trim();
                    }
                }
            }
        }
        ip = req.getHeader("x-real-ip");
        if (isIPAddr(ip)) {
            return ip;
        }
        ip = req.getRemoteAddr();
        if (ip.indexOf('.') == -1) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr
     * @return
     */
    public static boolean isIPAddr(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, '.');
        if (ips.length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
        }
        return false;
    }

}
