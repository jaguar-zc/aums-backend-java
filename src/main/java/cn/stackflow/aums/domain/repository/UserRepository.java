package cn.stackflow.aums.domain.repository;


import cn.stackflow.aums.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    int countByUsername(String username);

    int countByPhone(String phone);

    List<User> findAll();

    List<User> findByDeptId(String deptId);
}
