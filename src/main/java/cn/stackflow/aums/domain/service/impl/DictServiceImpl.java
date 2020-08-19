package cn.stackflow.aums.domain.service.impl;

import cn.stackflow.aums.common.bean.DictDTO;
import cn.stackflow.aums.common.bean.PageResult;
import cn.stackflow.aums.domain.entity.Dict;
import cn.stackflow.aums.domain.entity.User;
import cn.stackflow.aums.domain.repository.DictRepository;
import cn.stackflow.aums.domain.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: zhangc/jaguar_zc@sina.com
 * @create: 2020-07-08 09:57
 */
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    DictRepository dictRepository;


    @Override
    public PageResult<DictDTO> list(PageResult page) {
        PageRequest of = PageRequest.of(page.getPage() - 1, page.getSize());
        Page<Dict> deptPage = dictRepository.findAll(of);
        page.setTotal(deptPage.getTotalElements());
        page.setRows(deptPage.getContent().stream().map(item -> {
            DictDTO dept = new DictDTO();
            dept.setDataCode(item.getDataCode());
            dept.setDataValue(item.getDataValue());
            dept.setDataDesc(item.getDataDesc());
            return dept;
        }).collect(Collectors.toList()));
        return page;
    }

    @Override
    public DictDTO get(String code) {
        Optional<Dict> optionalDict = dictRepository.findByDataCode(code);
        if(!optionalDict.isPresent()){
            return null;
        }
        Dict dict = optionalDict.get();
        DictDTO dictDTO = new DictDTO();
        dictDTO.setDataCode(dict.getDataCode());
        dictDTO.setDataValue(dict.getDataValue());
        dictDTO.setDataDesc(dict.getDataDesc());
        return dictDTO;
    }


    @Override
    public DictDTO get(String code, String defaultValue) {
        DictDTO dictDTO = this.get(code);
        if (dictDTO != null) {
            return dictDTO;
        }
        Dict dict = new Dict();
        dict.setDataCode(code);
        dict.setDataValue(defaultValue);
        dict.setDataDesc("");
        dict = dictRepository.save(dict);
        dictDTO = new DictDTO();
        dictDTO.setDataCode(dict.getDataCode());
        dictDTO.setDataValue(dict.getDataValue());
        dictDTO.setDataDesc(dict.getDataDesc());
        return dictDTO;
    }

    @Override
    public void create(User user, DictDTO deptDTO) {
        Dict dict  = new Dict();
        dict.setDataCode(deptDTO.getDataCode());
        dict.setDataValue(deptDTO.getDataValue());
        dict.setDataDesc(deptDTO.getDataDesc());
        dictRepository.save(dict);
    }

    @Override
    public void update(String code,String value){
        Optional<Dict> byDataCode = dictRepository.findByDataCode(code);
        if (byDataCode.isPresent()) {
            Dict dict = byDataCode.get();
            dict.setDataValue(value);
            dictRepository.save(dict);
        }
    }

    @Override
    public void delete(String code) {
        Optional<Dict> optionalDict = dictRepository.findByDataCode(code);
        if(optionalDict.isPresent()){
            dictRepository.delete(optionalDict.get());
        }
    }
}
