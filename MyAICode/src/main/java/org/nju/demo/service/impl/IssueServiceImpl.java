package org.nju.demo.service.impl;

import org.nju.demo.config.Constants;
import org.nju.demo.dao.AVersionMapper;
import org.nju.demo.dao.IssueBasicMapper;
import org.nju.demo.dao.VersionPatternRelMapper;
import org.nju.demo.entity.AVersion;
import org.nju.demo.entity.IssueBasic;
import org.nju.demo.entity.IssueBasicExample;
import org.nju.demo.entity.VersionPatternRel;
import org.nju.demo.pojo.dto.IssueDTO;
import org.nju.demo.pojo.vo.IssueItem;
import org.nju.demo.pojo.vo.ProblemItem;
import org.nju.demo.service.IssueService;
import org.nju.demo.struct.ASTNode;
import org.nju.demo.utils.ClangASTConverter;
import org.nju.demo.utils.OssUtil;
import org.nju.demo.utils.algorithm.Match;
import org.nju.demo.utils.algorithm.impl.ExactMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.nju.demo.utils.CNNUtil.extractFeatures;
import static org.nju.demo.utils.CodeToVector.getVecWithModel;
import static org.nju.demo.utils.CodeToVector.reshapeVectorToMatrix;

@Service
public class IssueServiceImpl implements IssueService {

    @Autowired
    private IssueBasicMapper issueBasicMapper;

    @Autowired
    private VersionPatternRelMapper versionPatternRelMapper;

    @Autowired
    private AVersionMapper aVersionMapper;

    @Override
    public List<IssueBasic> getIssueList(String versionId,String priority,String patternId,String state,int flag) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);
        if (priority.length() > 0) criteria.andPriorityEqualTo(priority);
        if (patternId.length() > 0) criteria.andPatternIdEqualTo(patternId);
        if (state.length() > 0) criteria.andStateEqualTo(state);
        if (flag != Constants.IsFilter.IGNORE) criteria.andFlagEqualTo(flag);

        return issueBasicMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<IssueBasic> getIssueListWithSnippet(String versionId,String patternId, String fileName, String snippet,int flag) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);
        if(patternId.length()>0) criteria.andPatternIdEqualTo(patternId);
        if(fileName.length()>0) criteria.andFileNameEqualTo(fileName);
        if(snippet.length()>0) criteria.andSnippetEqualTo(snippet);
        if (flag != Constants.IsFilter.IGNORE) criteria.andFlagEqualTo(flag);
        return issueBasicMapper.selectByExampleWithBLOBs(example);
    }


    @Override
    public List<IssueBasic> getIssueList(String versionId, String state, int flag) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId);
        if (state.length() > 0) criteria.andStateEqualTo(state);
        if (flag != Constants.IsFilter.IGNORE) criteria.andFlagEqualTo(flag);

        return issueBasicMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<IssueBasic> getIssueListByPatternId(String patternId) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();
        criteria.andPatternIdEqualTo(patternId);
        return issueBasicMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<IssueBasic> getAllIssue() {
        IssueBasicExample example = new IssueBasicExample();
        return issueBasicMapper.selectByExampleWithBLOBs(example);

    }

    @Override
    public List<IssueDTO> getIssueItemList(String versionId, String priority,int flag) {
        return issueBasicMapper.selectIssueByVersionIdAndPriority(versionId,priority,flag);
    }

    @Override
    public List<IssueBasic> getIssueListByPatternId(String versionId, String patternId) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId)
                .andPatternIdEqualTo(patternId);

        return issueBasicMapper.selectByExampleWithBLOBs(example);
    }

    @Override
    public List<IssueBasic> getClassifiedIssueList() {
        return issueBasicMapper.selectClassifiedIssueList();
    }

    @Override
    public IssueBasic getIssue(String issueId) {
        return issueBasicMapper.selectByPrimaryKey(issueId);
    }

    @Override
    public int countTrueIssueByPriority(String versionId, String priority) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId)
                .andPriorityEqualTo(priority)
                .andStateEqualTo(Constants.IssueState.TRUE);

        return (int)issueBasicMapper.countByExample(example);
    }

    @Override
    public int countTrueIssueByPattern(String versionId, String patternId) {
        IssueBasicExample example = new IssueBasicExample();
        IssueBasicExample.Criteria criteria = example.createCriteria();

        criteria.andVersionIdEqualTo(versionId)
                .andPatternIdEqualTo(patternId)
                .andStateEqualTo(Constants.IssueState.TRUE);

        return (int)issueBasicMapper.countByExample(example);
    }

    @Override
    public int addIssue(IssueBasic issueBasic) {
        return issueBasicMapper.insert(issueBasic);
    }

    @Override
    public int deleteIssueByVersionId(String versionId) {
        return issueBasicMapper.deleteByVersion(versionId);
    }

    @Override
    public int addRelation(VersionPatternRel versionPatternRel) {
        VersionPatternRel last = versionPatternRelMapper.selectLastRecord();
        if(last==null) versionPatternRel.setId(1);
        else versionPatternRel.setId(last.getId()+1);
        versionPatternRelMapper.insert(versionPatternRel);
        return versionPatternRel.getId();
    }

    @Override
    public int updateIssue(IssueBasic issueBasic) {
        return issueBasicMapper.updateByPrimaryKeySelective(issueBasic);
    }

    @Override
    public void compare(List<IssueBasic> lastIssueList, List<IssueBasic> issueBasicList) {
        Match match = new ExactMatch();
        for (IssueBasic lastIssue:lastIssueList){
            int flag = 1;
            for (IssueBasic issueBasic:issueBasicList){
                if (match.mark(issueBasic,lastIssue) == 0){
                    flag--;
                    lastIssue.setState(Constants.IssueState.TRUE);
                    break;
                }
            }
            if (flag == 0) continue;
            else{
                for(IssueBasic issueBasic:issueBasicList){
                    if (lastIssue.getFilePath().equals(issueBasic.getFilePath())){
                        flag--;
                        lastIssue.setState(Constants.IssueState.TRUE);
                        break;
                    }
                }
            }
            if (flag == 1) lastIssue.setState(Constants.IssueState.UNKNOWN);
        }
        for(IssueBasic lastIssue:lastIssueList) updateIssue(lastIssue);
    }

    @Override
    public List<ProblemItem> getProblemItemList(List<IssueDTO> issueList) {
        List<ProblemItem> problemItemList = new ArrayList<>();
        Map<String,List<IssueItem>> hm = new HashMap<>();

        for (IssueDTO issue : issueList){
            IssueItem issueItem = new IssueItem();
            issueItem.setIssueId(issue.getIssueId());
            issueItem.setFileName(issue.getFileName());
            issueItem.setKingdom(issue.getKingdom());
            issueItem.setStartLine(issue.getStartLine());
            issueItem.setState(issue.getState());
            issueItem.setTargetFunction(issue.getTargetFunction());
            List<IssueItem> issueItemList = null;
            if (hm.containsKey(issue.getPatternName())) issueItemList = hm.get(issue.getPatternName());
            else issueItemList = new ArrayList<>();
            issueItemList.add(issueItem);
            hm.put(issue.getPatternName(),issueItemList);
        }

        for(Map.Entry<String,List<IssueItem>> entry : hm.entrySet()){
            ProblemItem problemItem = new ProblemItem();
            problemItem.setPatternName(entry.getKey());
            problemItem.setIssueItemList(entry.getValue());
            problemItem.setIssueNum();
            problemItemList.add(problemItem);
        }

        return problemItemList;
    }

    @Override
    public String getFuncSnippet(IssueBasic issueBasic) throws IOException {
        String versionId = issueBasic.getVersionId();
        AVersion aVersion = aVersionMapper.selectByPrimaryKey(versionId);
        OssUtil.downloadFileFromOSS(versionId  + issueBasic.getFilePath(),"ASTs/cppTemp.txt");
        if(issueBasic.getFuncStartLine()!=null && issueBasic.getFuncEndLine()!=null){
            int start =issueBasic.getFuncStartLine();
            int end = issueBasic.getFuncEndLine();
            String res = OssUtil.readLinesInRange("ASTs/cppTemp.txt",start,end);
            System.out.println(res);
            return res;
        }else{
            return "";
        }
    }

    @Override
    public int writeAllFuncSnippet(List<IssueBasic> issueBasicList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (IssueBasic issueBasic : issueBasicList) {
                try {
                    String snippet = getFuncSnippet(issueBasic);
                    if(snippet!=null && !snippet.isEmpty()){
                    writer.write(snippet);
                    writer.newLine();}
                } catch (IOException e) {
                    // 处理异常情况，比如无法获取函数片段等
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            // 处理文件写入异常
            e.printStackTrace();
        }
        return 1;
    }

    public double[] getFeatureVec(List<IssueBasic> issueBasicList,String filePath) throws IOException {
        filePath = filePath.replace(":","_");
        int num = issueBasicList.size();
        double[][][] inputArray = new double[10][10][num];
        int flag = 0;
        for(IssueBasic issueBasic:issueBasicList){

            double[] vec = getVecWithModel(getFuncSnippet(issueBasic));
            double[][] matrix = reshapeVectorToMatrix(vec);
            for(int i=0;i<10;i++){
                for(int j=0;j<10;j++){
                    inputArray[i][j][flag] = matrix[i][j];
                }
            }
            flag ++;
        }
        double[] res = extractFeatures(inputArray);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + ".txt"))) {
            for (double d : res) {
                writer.write(String.valueOf(d));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
