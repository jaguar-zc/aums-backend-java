package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, String>, JpaSpecificationExecutor<Resource> {

    List<Resource> findByEnableAndParentId(Integer enable, String parentId);

    List<Resource> findByEnableAndResourceLevelOrderBySortAsc(Integer enable, int level);
}
