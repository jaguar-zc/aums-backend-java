package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.DictType;
import cn.stackflow.aums.domain.entity.DictValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface DictValueRepository extends JpaRepository<DictValue, String>, JpaSpecificationExecutor<DictValue> {

    Optional<DictType> findByDataCode(String dataCode);

}