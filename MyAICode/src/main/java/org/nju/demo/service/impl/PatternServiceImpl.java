package org.nju.demo.service.impl;

import org.nju.demo.dao.PatternInfoMapper;
import org.nju.demo.dao.VersionPatternRelMapper;
import org.nju.demo.entity.PatternInfo;
import org.nju.demo.entity.PatternInfoExample;
import org.nju.demo.entity.PatternInfoWithBLOBs;
import org.nju.demo.entity.VersionPatternRel;
import org.nju.demo.service.PatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatternServiceImpl implements PatternService {

    @Autowired
    private PatternInfoMapper patternInfoMapper;

    @Autowired
    private VersionPatternRelMapper versionPatternRelMapper;

    @Override
    public PatternInfoWithBLOBs getPatternInfoWithBlobs(String patternInfoId) {
        return patternInfoMapper.selectByPrimaryKey(patternInfoId);
    }

    @Override
    public PatternInfo getPatternByPatternName(String patternName) {
        PatternInfoExample example = new PatternInfoExample();
        PatternInfoExample.Criteria criteria = example.createCriteria();

        criteria.andPatternNameEqualTo(patternName);

        List<PatternInfo> patternInfoList = patternInfoMapper.selectByExample(example);
        if (patternInfoList.size() > 0)
            return patternInfoList.get(0);
        else return null;
    }

    @Override
    public PatternInfo getPatternInfo(String patternInfoId) {
        return patternInfoMapper.selectByPrimaryKey(patternInfoId);
    }

    @Override
    public List<PatternInfoWithBLOBs> getPatternInfoList() {
        PatternInfoExample example = new PatternInfoExample();
        return patternInfoMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<PatternInfo> getPatternListByKeyword(String keyword) {
        return patternInfoMapper.selectByKeyword(keyword);
    }

    @Override
    public List<PatternInfo> getFalsePatternList() {
        PatternInfoExample example = new PatternInfoExample();
        PatternInfoExample.Criteria criteria = example.createCriteria();

        criteria.andFNumGreaterThan(0);

        return patternInfoMapper.selectByExample(example);
    }

    @Override
    public int addRelation(VersionPatternRel versionPatternRel) {
        return versionPatternRelMapper.insert(versionPatternRel);
    }

    @Override
    public int updatePatternLikelihood(PatternInfo pattern) {
        return patternInfoMapper.updateByPrimaryKeySelective(pattern);
    }

    @Override
    public int updatePatternInfo(PatternInfoWithBLOBs patternInfo) {
        return patternInfoMapper.updateByPrimaryKeyWithBLOBs(patternInfo);
    }

    @Override
    public int addPatternInfo(PatternInfoWithBLOBs patternInfo) {
        return patternInfoMapper.insert(patternInfo);
    }

}
