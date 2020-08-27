package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.DictTypeDTO;
import cn.stackflow.aums.common.bean.DictValueDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.common.utils.StringUtils;
import cn.stackflow.aums.domain.entity.DictType;
import cn.stackflow.aums.domain.entity.DictValue;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.DictTypeRepository;
import cn.stackflow.aums.domain.repository.DictValueRepository;
import cn.stackflow.aums.domain.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:57
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictTypeRepository dictTypeRepository;

    @Autowired
    DictValueRepository dictValueRepository;

    @Override
    public PageResult<DictTypeDTO> getTypeList(PageResult page) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize());
        Specification<DictType> specification = new Specification<DictType>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<DictType> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
//                if (StringUtils.isNotEmpty(name)) {
//                    predicates.add(criteriaBuilder.equal(root.get("dataCode"), name));
//                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<DictType> deptPage = dictTypeRepository.findAll(specification, of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> {
            DictTypeDTO dept = new DictTypeDTO();
            dept.setId(item.getId());
            dept.setAppId(item.getAppId());
            dept.setName(item.getDictName());
            dept.setDesc(item.getDictDesc());
            return dept;
        }).collect(Collectors.toList()));
        return page;
    }

    @Override
    public PageResult<DictValueDTO> getValueList(PageResult page, String dictTypeId) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize());
        Specification<DictValue> specification = new Specification<DictValue>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<DictValue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (StringUtils.isNotEmpty(dictTypeId)) {
                    predicates.add(criteriaBuilder.equal(root.get("dictTypeId"), dictTypeId));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<DictValue> deptPage = dictValueRepository.findAll(specification, of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> {
            DictValueDTO dictValue = new DictValueDTO();
            dictValue.setDataCode(item.getDataCode());
            dictValue.setDataValue(item.getDataValue());
            return dictValue;
        }).collect(Collectors.toList()));
        return page;
    }

    @Override
    public void createDictType(User user, DictTypeDTO dictTypeDTO) {
        DictType dictType = new DictType();
        dictType.setAppId(dictTypeDTO.getAppId());
        dictType.setDictName(dictTypeDTO.getName());
        dictType.setDictDesc(dictTypeDTO.getDesc());
        dictTypeRepository.save(dictType);
    }

    @Override
    public void createDictValue(User user, DictValueDTO dictTypeDTO) {
        DictValue dictValue = new DictValue();
        dictValue.setDataCode(dictTypeDTO.getDataCode());
        dictValue.setDataValue(dictTypeDTO.getDataValue());
        dictValue.setDictTypeId(dictTypeDTO.getDictTypeId());
        dictValueRepository.save(dictValue);
    }

    @Override
    public void updateDictType(DictTypeDTO dictTypeDTO) {
        DictType dictType = dictTypeRepository.getOne(dictTypeDTO.getId());
        dictType.setAppId(dictTypeDTO.getAppId());
        dictType.setDictName(dictTypeDTO.getName());
        dictType.setDictDesc(dictTypeDTO.getDesc());
        dictTypeRepository.save(dictType);
    }

    @Override
    public void deleteDictType(String id) {
        dictTypeRepository.deleteById(id);
    }

    @Override
    public void deleteDictValue(String id) {
        dictValueRepository.deleteById(id);
    }
}
