package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface AppRepository extends JpaRepository<App, String>, JpaSpecificationExecutor<App> {
}
