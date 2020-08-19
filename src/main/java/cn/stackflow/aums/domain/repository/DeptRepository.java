package cn.stackflow.aums.domain.repository;

import cn.stackflow.aums.domain.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-06-28 16:55
 */
@Repository
public interface DeptRepository extends JpaRepository<Dept,String>, JpaSpecificationExecutor<Dept> {
}
