package org.nju.demo.service.impl;

import org.nju.demo.dao.ARuleMapper;
import org.nju.demo.entity.ARule;
import org.nju.demo.entity.ARuleExample;
import org.nju.demo.service.RuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleServiceImpl implements RuleService {

    @Autowired
    private ARuleMapper ruleMapper;

    @Override
    public List<ARule> getRules(String versionId) {
        ARuleExample example = new ARuleExample();
        ARuleExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);

        return ruleMapper.selectByExample(example);
    }

    @Override
    public ARule getRule(String ruleId) {
        return ruleMapper.selectByPrimaryKey(ruleId);
    }

    @Override
    public int addRule(ARule rule) {
        return ruleMapper.insert(rule);
    }

    @Override
    public int updateRule(ARule rule) {
        return ruleMapper.updateByPrimaryKeySelective(rule);
    }

    @Override
    public int deleteRule(String ruleId) {
        return ruleMapper.deleteByPrimaryKey(ruleId);
    }
}
