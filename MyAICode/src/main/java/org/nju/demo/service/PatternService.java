package org.nju.demo.service;

import org.nju.demo.entity.PatternInfo;
import org.nju.demo.entity.PatternInfoWithBLOBs;
import org.nju.demo.entity.VersionPatternRel;

import java.util.List;

public interface PatternService {

    PatternInfoWithBLOBs getPatternInfoWithBlobs(String patternInfoId);

    PatternInfo getPatternByPatternName(String patternName);

    PatternInfo getPatternInfo(String patternInfoId);

    List<PatternInfoWithBLOBs> getPatternInfoList();

    List<PatternInfo> getFalsePatternList();

    List<PatternInfo> getPatternListByKeyword(String keyword);

    int addRelation(VersionPatternRel versionPatternRel);

    int updatePatternLikelihood(PatternInfo pattern);

    int updatePatternInfo(PatternInfoWithBLOBs patternInfo);

    int addPatternInfo(PatternInfoWithBLOBs patternInfo);

}
