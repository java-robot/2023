package org.nju.demo.service.impl;

import org.nju.demo.dao.ACodeMapper;
import org.nju.demo.entity.ACode;
import org.nju.demo.entity.ACodeExample;
import org.nju.demo.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CodeServiceImpl implements CodeService {
    @Autowired
    private ACodeMapper aCodeMapper;
    @Override
    public List<ACode> getAllCodes() {
        ACodeExample aCodeExample = new ACodeExample();
        ACodeExample.Criteria criteria = aCodeExample.createCriteria();
        return aCodeMapper.selectByExample(aCodeExample);
    }

    @Override
    public int addCode(ACode aCode) {
        return aCodeMapper.insert(aCode);
    }

    @Override
    public List<ACode> getCodesByCodeName(String codeName) {
        ACodeExample aCodeExample = new ACodeExample();
        ACodeExample.Criteria criteria = aCodeExample.createCriteria();
        criteria.andCodeNameEqualTo(codeName);
        return aCodeMapper.selectByExample(aCodeExample);
    }

}
