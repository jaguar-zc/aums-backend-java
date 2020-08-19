package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface RoleRepository extends JpaRepository<Role,String>, JpaSpecificationExecutor<Role> {
    List<Role> findByIdIn(List<String> ids);
}
