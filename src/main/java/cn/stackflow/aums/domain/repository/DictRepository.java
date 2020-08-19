package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.Dict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface DictRepository extends JpaRepository<Dict, String>, JpaSpecificationExecutor<Dict> {

    Optional<Dict> findByDataCode(String dataCode);

}