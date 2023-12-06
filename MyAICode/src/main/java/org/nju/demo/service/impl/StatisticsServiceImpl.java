package org.nju.demo.service.impl;

import org.nju.demo.config.Constants;
import org.nju.demo.dao.PatternStatisticsMapper;
import org.nju.demo.dao.PriorityStatisticsMapper;
import org.nju.demo.dao.VersionPatternRelMapper;
import org.nju.demo.entity.*;
import org.nju.demo.pojo.dto.IssueInfoDTO;
import org.nju.demo.pojo.dto.PatternStatisticsDTO;
import org.nju.demo.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private PatternStatisticsMapper patternStatisticsMapper;

    @Autowired
    private PriorityStatisticsMapper priorityStatisticsMapper;

    @Autowired
    private VersionPatternRelMapper versionPatternRelMapper;

    @Override
    public List<PatternStatisticsDTO> getPatternStatisticsByVersionId(String versionId) {
        return patternStatisticsMapper.selectPatternStatisticsByVersionId(versionId);
    }

    @Override
    public int getIssueNumByRelation(String versionId, String patternId) {
        return patternStatisticsMapper.selectIssueNumByRelation(versionId,patternId);
    }

    @Override
    public PriorityStatistics getPriorityStatisticsByVersionId(String versionId) {
        PriorityStatisticsExample example = new PriorityStatisticsExample();
        PriorityStatisticsExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);

        return priorityStatisticsMapper.selectByExample(example).get(0);
    }

    @Override
    public int addPatternStatistics(PatternStatistics patternStatistics) {
        return patternStatisticsMapper.insert(patternStatistics);
    }

    @Override
    public int addPriorityStatistics(PriorityStatistics priorityStatistics) {
        return priorityStatisticsMapper.insert(priorityStatistics);
    }

    @Override
    public Map<String, Integer> countIssueByPattern(List<IssueInfoDTO> issueInfoList) {
        HashMap<String,Integer> hm = new HashMap<>();
        for(IssueInfoDTO issueInfo : issueInfoList) hm.put(issueInfo.getPatternName(),hm.getOrDefault(issueInfo.getPatternName(),0)+1);
        return hm;
    }

    @Override
    public PriorityStatistics countIssueByPriority(List<IssueInfoDTO> issueInfoList) {
        PriorityStatistics priorityStatistics = new PriorityStatistics();
        for(IssueInfoDTO issueInfo : issueInfoList){
            if (issueInfo.getPriority().equals(Constants.Priority.LOW)) priorityStatistics.setLowNum(priorityStatistics.getLowNum()+1);
            else if (issueInfo.getPriority().equals(Constants.Priority.MEDIUM)) priorityStatistics.setMediumNum(priorityStatistics.getMediumNum()+1);
            else if (issueInfo.getPriority().equals(Constants.Priority.HIGH)) priorityStatistics.setHighNum(priorityStatistics.getHighNum()+1);
            else priorityStatistics.setCriticalNum(priorityStatistics.getCriticalNum()+1);
        }
        return priorityStatistics;
    }

    @Override
    public int deleteAllStatistics(String versionId) {
        VersionPatternRelExample versionPatternRelExample = new VersionPatternRelExample();
        VersionPatternRelExample.Criteria criteria = versionPatternRelExample.createCriteria();
        criteria.andVersionIdEqualTo(versionId);
        List<VersionPatternRel> versionPatternRelList = versionPatternRelMapper.selectByExample(versionPatternRelExample);
        for(VersionPatternRel versionPatternRel:versionPatternRelList){
            //删patternStatistics
            patternStatisticsMapper.deletePatternStatisticsByVPId(versionPatternRel.getId());
        }
        //
        priorityStatisticsMapper.deletePriorityStatisticsByVersionId(versionId);
        //
        versionPatternRelMapper.deleteRelByVersionId(versionId);
        return 0;
    }
}
