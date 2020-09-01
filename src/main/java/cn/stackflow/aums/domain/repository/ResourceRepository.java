package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

    List<Resource> findByEnableAndParentIdOrderBySortAsc(Integer enable, String parentId);

    List<Resource> findByEnableAndResourceLevelOrderBySortAsc(Integer enable, int level);

    @Query(value = "select resource_id from t_sys_role_resource where role_id =:roleId",nativeQuery = true)
    List<String> findResourceIdsByRoleId(@Param("roleId") String roleId);

    Optional<Resource> findByTypeAndAppId(Resource.ResourceType type,String appId);

    void deleteByAppId(String appId);

    int countByParentId(String parentId);
}
