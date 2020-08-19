package cn.stackflow.aums.common.cache;

import com.google.common.cache.RemovalNotification;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户缓存
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-11 10:43
 */
@Component
@Slf4j
public class UserCache extends AbstractCache<String, User>{

    @Autowired
    UserRepository userRepository;


    public UserCache() {
        super();
    }

    @Override
    public User load(String username) throws Exception {
        log.info("load:{}",username);
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void onRemoval(RemovalNotification<String, User> removalNotification) {
        log.info("onRemoval:{}",removalNotification.getKey());
    }
}
