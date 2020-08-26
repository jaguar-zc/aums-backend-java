package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.OperLogs;
import cn.stackflow.aums.domain.repository.OperLogsRepository;
import cn.stackflow.aums.domain.service.OperLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-08-25 10:43
 */
@Service
public class OperLogsServiceImpl implements OperLogsService {

    @Autowired
    OperLogsRepository operLogsRepository;


    @Override
    public PageResult<OperLogs> findList(PageResult page, Integer logsType, String url) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize(), Sort.by(Sort.Direction.DESC, "createTime"));
        Specification<OperLogs> specification = new Specification<OperLogs>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<OperLogs> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (logsType != null) {
                    predicates.add(criteriaBuilder.equal(root.get("logsType"), logsType));
                }
                if (StringUtils.isNotEmpty(url)) {
                    predicates.add(criteriaBuilder.equal(root.get("operUri"), url));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<OperLogs> deptPage = operLogsRepository.findAll(specification, of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent());
        return page;
    }

    @Override
    public void insert(OperLogs operLogs) {
        operLogsRepository.save(operLogs);
    }
}
