package org.nju.demo.controller;

import freemarker.template.TemplateException;
import org.nju.demo.config.Constants;
import org.nju.demo.entity.*;
import org.nju.demo.pojo.dto.IssueInfoDTO;
import org.nju.demo.pojo.dto.PatternInfoDTO;
import org.nju.demo.pojo.dto.PatternStatisticsDTO;
import org.nju.demo.pojo.vo.*;
import org.nju.demo.service.*;
import org.nju.demo.struct.ASTNode;
import org.nju.demo.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

import static org.nju.demo.utils.ClangASTConverter.loadASTNodes;
import static org.nju.demo.utils.CodeToVector.*;
import static org.nju.demo.utils.OssUtil.downloadFileFromOSS;
import static org.nju.demo.utils.OssUtil.readLinesInRange;

@Controller
public class MainController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private PatternService patternService;

    @Autowired
    private VersionService versionService;
    
    @Autowired
    private IssueService issueService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private FeatureService featureService;

    @Autowired
    private HttpSession session;

    private static final String UPLOADED_FOLDER = System.getProperty("user.dir");

    @RequestMapping("/view/projects")
    public String viewProjects(Model model){
        List<ACode> codeList = codeService.getAllCodes();
        model.addAttribute("codeList",new ArrayList<ACode>());
        model.addAttribute("codeList", codeList);
        return "project_list";
    }

    @ResponseBody
    @RequestMapping("/projects")
    public List<Project> getProjects(){
        AUser user = (AUser) session.getAttribute("user");
        return projectService.getShownProjects(user.getId());
    }


    @RequestMapping("/delAll")
    public String deleteAll(@RequestParam("projectId") String projectId){
        List<AVersion> aVersionList = versionService.getVersionsByProjectId(projectId);
        for (AVersion aVersion: aVersionList){
                issueService.deleteIssueByVersionId(aVersion.getVersionId());
                statisticsService.deleteAllStatistics(aVersion.getVersionId());
                versionService.deleteByVersionId(aVersion.getVersionId());
        }
        projectService.deleteProject(projectId);
        return "project_list";
    }


    @ResponseBody
    @RequestMapping(value = "/addProject",method = RequestMethod.POST)
    public int addProjects(@RequestParam("projectName") String projectName,
                           @RequestParam("description") String description){
        Project project = new Project();
        AUser user = (AUser) session.getAttribute("user");

        List<Project> projectList = projectService.getProjectsByProjectName(user.getId(),projectName);
        if(projectList.size()>0) return -1;

        project.setProjectId(StringUtil.generateStringId());
        project.setUserId(user.getId());
        project.setProjectName(projectName);
        project.setDescription(description);
        project.setCreateTime(new Date());
        return projectService.addProject(project);
    }

    @ResponseBody
    @RequestMapping("/version")
    public int getVersion(@RequestParam("versionName") String versionName){
        Project project = (Project) session.getAttribute("project");
        List<AVersion> versionList = versionService.getVersionsByVersionName(project.getProjectId(),versionName);
        if (versionList.size()!=0) return 1;
        else return 0;
    }

    @RequestMapping("/view/versions/{projectId}")
    public String viewVersions(@PathVariable("projectId") String projectId,
                               Model model){
        Project project = projectService.getProject(projectId);
        List<AVersion> versionList = versionService.getVersionsByProjectId(projectId);
        session.setAttribute("project",project);
        if (versionList.size() != 0)
            model.addAttribute("versionList",versionList);
        return "version_list";
    }

    //创建version的时候需要手动传入files，在oss中，files的名称就是versionId
    @RequestMapping("/addVersion")
    public String addVersion(@RequestParam("versionName") String versionName,
                             @RequestParam("reportFile") MultipartFile reportFile,
                             @RequestParam(value="fileInput",required = false) MultipartFile[] files,
                             @RequestParam(value="folderNameInput",required = false) String folderName)throws IOException {
        AVersion aVersion = new AVersion();
        AUser user = (AUser) session.getAttribute("user");
        Project project = (Project) session.getAttribute("project");

        List<AVersion> versionList = versionService.getVersionsByVersionName(project.getProjectId(),versionName);
        if (versionList.size()!=0) return "redirect:/back_versions";

        String fileName = "default.xml";
        if (reportFile != null){
            fileName = reportFile.getOriginalFilename();
            int index = fileName.lastIndexOf('.');
//            if (!fileName.substring(index+1).equals("xml")) return "redirect:/back_versions";
            String filePath = UPLOADED_FOLDER + "/data/"+user.getUsername()+"/"+project.getProjectName()+"/";
            File file = new File(filePath);
            if (!file.exists()) file.mkdirs();
            filePath+=fileName;
            file = new File(filePath);
            reportFile.transferTo(file);
        }
        String versionId = StringUtil.generateStringId();
        aVersion.setVersionId(versionId);
        aVersion.setVersionName(versionName);
        aVersion.setLastId(Constants.VersionState.FIRST);
        aVersion.setFilePath("data/"+user.getUsername()+"/"+project.getProjectName()+"/"+fileName);
        aVersion.setProjectId(project.getProjectId());
        aVersion.setCreateTime(new Date());
        aVersion.setFolderName(folderName);
        // Check if files were provided in the fileInput parameter
        if (files != null && files.length > 1) {
            System.out.println(files.length);
            aVersion.setCodeState(Constants.CodeState.usingCode);
            OssUtil.upload(versionId, files);
        }
        versionService.addVersion(aVersion);

        return "redirect:/back_versions";
    }

    @RequestMapping("/back_versions")
    public String backVersions(Model model){
        Project project = (Project) session.getAttribute("project");
        List<AVersion> versionList = versionService.getVersionsByProjectId(project.getProjectId());
        if (versionList.size() != 0)
            model.addAttribute("versionList",versionList);
        return "version_list";
    }

    @ResponseBody
    @RequestMapping("/versions")
    public List<VersionVO> getVersions(){
        Project project = (Project) session.getAttribute("project");
        List<VersionVO> versionVOList = new ArrayList<>();
        List<AVersion> aversionList =  versionService.getVersionsByProjectId(project.getProjectId());
        for (AVersion aVersion:aversionList){
            VersionVO versionVO = new VersionVO();
            versionVO.setVersionId(aVersion.getVersionId());
            versionVO.setVersionName(aVersion.getVersionName());
            versionVO.setCreateTime(aVersion.getCreateTime());
            versionVO.setFilePath(aVersion.getFilePath());
            versionVO.setCodeState(aVersion.getCodeState());
            if(!Objects.equals(aVersion.getLastId(), Constants.VersionState.FIRST)){
                versionVO.setLastVersionName(versionService.getVersion(aVersion.getLastId()).getVersionName());
            }else{
                versionVO.setLastVersionName("As original version");
            }
            versionVOList.add(versionVO);
        }
        return versionVOList;
    }

    @RequestMapping("/view/issues/{versionId}")
    public String viewIssues(@PathVariable("versionId") String versionId,
                             Model model){
        AVersion version = versionService.getVersion(versionId);
        List<PatternStatisticsDTO> patternStatisticsList = statisticsService.getPatternStatisticsByVersionId(versionId);
        List<PatternItem> patternItemList = new ArrayList<>();

        for(PatternStatisticsDTO patternStatistics : patternStatisticsList){
            PatternItem patternItem = new PatternItem();
            patternItem.setPatternId(patternStatistics.getPatternId());
            patternItem.setPatternName(patternStatistics.getPatternName());
            patternItem.setIssueNum(patternStatistics.getIssueNum());
            patternItemList.add(patternItem);
        }

        model.addAttribute("patternItemList",patternItemList);
        session.setAttribute("version",version);
        session.setAttribute("versionId",versionId);
        return "ordered_issue_list";
    }

    @RequestMapping("/view/checkIssues")
    public String viewCheckIssues(
                             Model model){
        String versionId = (String) session.getAttribute("versionId");
        AVersion version = versionService.getVersion(versionId);
        List<PatternStatisticsDTO> patternStatisticsList = statisticsService.getPatternStatisticsByVersionId(versionId);
        List<PatternItem> patternItemList = new ArrayList<>();

        for(PatternStatisticsDTO patternStatistics : patternStatisticsList){
            PatternItem patternItem = new PatternItem();
            patternItem.setPatternId(patternStatistics.getPatternId());
            patternItem.setPatternName(patternStatistics.getPatternName());
            patternItem.setIssueNum(patternStatistics.getIssueNum());
            patternItemList.add(patternItem);
        }

        model.addAttribute("patternItemList",patternItemList);
        session.setAttribute("version",version);

        return "issue_list";
    }



    @ResponseBody
    @RequestMapping("/editVersion")
    public int editVersion(@RequestParam("versionId") String versionId,
                           @RequestParam(value = "lastId") String lastId){
        AVersion version = versionService.getVersion(versionId);
        version.setLastId(lastId);
        return versionService.updateVersion(version);
    }

    @ResponseBody
    @RequestMapping("/scan")
    public int scan(@RequestParam("versionId") String versionId) throws IOException {
        if (issueService.getIssueList(versionId,"","","",Constants.IsFilter.IGNORE).size() != 0) return 2;

        Project project = (Project) session.getAttribute("project");
        AVersion aVersion = versionService.getVersion(versionId);
        if (aVersion.getLastId().equals(Constants.VersionState.INIT)) return 3;
        if (!aVersion.getLastId().equals(Constants.VersionState.FIRST) && issueService.getIssueList(aVersion.getLastId(),"","","", Constants.IsFilter.IGNORE).size() == 0) return 4;

        try{
            InputStream file = new FileInputStream(UPLOADED_FOLDER+"/"+aVersion.getFilePath());
//            Map<String,List> res = XMLUtil.getInfo(file);
            Map<String,List> res = XlsxUtil.getXlsxInfo(file);
            List<IssueInfoDTO> issueInfoList = res.get("issueInfoList");
            List<PatternInfoDTO> patternInfoList = res.get("patternInfoList");

            Map<String,Integer> hm = statisticsService.countIssueByPattern(issueInfoList);
            PriorityStatistics priorityStatistics = statisticsService.countIssueByPriority(issueInfoList);

            for(PatternInfoDTO patternInfoDTO : patternInfoList){
                PatternInfo pattern = patternService.getPatternByPatternName(patternInfoDTO.getPatternName());
                VersionPatternRel versionPatternRel = new VersionPatternRel();
                versionPatternRel.setVersionId(versionId);
                if (pattern == null){
                    PatternInfoWithBLOBs patternInfo = new PatternInfoWithBLOBs();
                    patternInfo.setPatternName(patternInfoDTO.getPatternName());

                    patternInfo.setPatternInfoId(StringUtil.generateStringId());
                    patternInfo.setExplanation(patternInfoDTO.getExplanation());
                    patternInfo.setRecommendation(patternInfoDTO.getRecommendation());
                    patternInfo.setTip(patternInfoDTO.getTip());

                    versionPatternRel.setPatternId(patternInfo.getPatternInfoId());

                    patternService.addPatternInfo(patternInfo);
                }
                else versionPatternRel.setPatternId(pattern.getPatternInfoId());
                int id = issueService.addRelation(versionPatternRel);
                PatternStatistics patternStatistics = new PatternStatistics();
                patternStatistics.setvPId(id);
                patternStatistics.setIssueNum(hm.get(patternInfoDTO.getPatternName()));
                statisticsService.addPatternStatistics(patternStatistics);//按漏洞模式统计原始数据
            }

            priorityStatistics.setVersionId(versionId);
            statisticsService.addPriorityStatistics(priorityStatistics);//按优先级统计原始数据

            List<IssueBasic> issueBasicList = new ArrayList<>();
            for(IssueInfoDTO issueInfoDTO : issueInfoList){
                PatternInfo pattern = patternService.getPatternByPatternName(issueInfoDTO.getPatternName());
                IssueBasic issueBasic = new IssueBasic();
                issueBasic.setIssueId(StringUtil.generateStringId());
                issueBasic.setPatternId(pattern.getPatternInfoId());
                issueBasic.setKingdom(issueInfoDTO.getKingdom());
                issueBasic.setDescription(issueInfoDTO.getDescription());
                issueBasic.setPriority(issueInfoDTO.getPriority());
                issueBasic.setFileName(issueInfoDTO.getFileName());
                issueBasic.setFilePath(issueInfoDTO.getFilePath());
                issueBasic.setStartLine(issueInfoDTO.getStartLine());
                issueBasic.setSnippet(issueInfoDTO.getSnippet());
                issueBasic.setTargetFunction(issueInfoDTO.getTargetFunction());
                issueBasic.setVersionId(versionId);


                List<IssueBasic> lastIssueBasicList = issueService.getIssueListWithSnippet(aVersion.getLastId(),issueBasic.getPatternId(),issueBasic.getFileName(),issueBasic.getSnippet(),issueBasic.getFlag());
                if (lastIssueBasicList.isEmpty()) {
                    issueBasic.setType(Constants.IssueType.NEW);
                }else{
                    issueBasic.setType(Constants.IssueType.ORIGINAL);
                }
                issueBasicList.add(issueBasic);
                issueService.addIssue(issueBasic);

            }

            String lastVersionId = aVersion.getLastId();
            if (!lastVersionId.equals(Constants.VersionState.FIRST)){
                List<IssueBasic> lastIssueList = issueService.getIssueList(lastVersionId,"","","", Constants.IsFilter.IGNORE);
                issueService.compare(lastIssueList,issueBasicList);
                Map<String,Integer> tNums = SortUtil.countNum(lastIssueList, Constants.IssueState.TRUE);
                Map<String,Integer> fNums = SortUtil.countNum(lastIssueList, Constants.IssueState.FALSE);
                for(Map.Entry<String,Integer> entry:tNums.entrySet()){
                    String patternId = entry.getKey();
                    int trueNum = entry.getValue();
                    int falseNum = 0;
                    if (fNums.containsKey(patternId)) falseNum = fNums.get(patternId);
                    PatternInfo patternInfo = patternService.getPatternInfo(patternId);
                    patternInfo.settNum(patternInfo.gettNum()+trueNum);
                    patternInfo.setfNum(patternInfo.getfNum()+falseNum);
                    patternService.updatePatternLikelihood(patternInfo);
                }
                for (Map.Entry<String,Integer> entry:fNums.entrySet()){
                    String patternId = entry.getKey();
                    if (tNums.containsKey(patternId)) continue;
                    else{
                        int falseNum = entry.getValue();
                        PatternInfo patternInfo = patternService.getPatternInfo(patternId);
                        patternInfo.setfNum(patternInfo.getfNum()+falseNum);
                        patternService.updatePatternLikelihood(patternInfo);
                    }
                }
            }

            List<IssueBasic> issueBasicList2 = issueService.getIssueList(versionId,"", Constants.IsFilter.IGNORE);
            //TODO
            for (IssueBasic issueBasic:issueBasicList2){
                System.out.println(issueBasic.getFilePath());
                OssUtil.downloadFileFromOSS(versionId+issueBasic.getFilePath(),"ASTs/cppPath.c");
                ASTNode astNode = ClangASTConverter.loadASTNodes("ASTs/cppPath.c","ASTs/jsonPath.txt");
                int[] startLineAndEndLine = ClangASTConverter.findNodeByLineNumber(astNode,issueBasic.getStartLine());
                if(startLineAndEndLine == null){
                    startLineAndEndLine = new int[]{-1, -1};
                }
                issueBasic.setFuncStartLine(startLineAndEndLine[0]);
                issueBasic.setFuncEndLine(startLineAndEndLine[1]);
                issueBasic.setSnippet(readLinesInRange("ASTs/cppPath.c",issueBasic.getStartLine(),issueBasic.getStartLine()));
                issueService.updateIssue(issueBasic);
            }
            return 1;


        }catch (Exception e){
            e.printStackTrace();
        }




        return 0;
    }

    @ResponseBody
    @RequestMapping("/issues")
    public List<IssueVO> getIssueList(@RequestParam(value = "priority",required = false) String priority,
                                      @RequestParam(value = "patternId",required = false) String patternId,
                                      @RequestParam(value = "state",required = false) String state){
        AVersion version = (AVersion) session.getAttribute("version");
        List<IssueBasic> issueBasicList = issueService.getIssueList(version.getVersionId(),priority,patternId,state,Constants.IsFilter.DISABLE);
        List<IssueVO> issueVOList = new ArrayList<>();
        for(IssueBasic issueBasic : issueBasicList){
            IssueVO issueVO = new IssueVO();
            PatternInfo patternInfo = patternService.getPatternInfo(issueBasic.getPatternId());
            issueVO.setIssueId(issueBasic.getIssueId());
            issueVO.setPatternName(patternInfo.getPatternName());
            issueVO.setFileName(issueBasic.getFileName());
            issueVO.setPriority(issueBasic.getPriority());
            issueVO.setType(issueBasic.getType());
            int trueNum = patternInfo.gettNum();
            int falseNum = patternInfo.getfNum();

            if (trueNum == 0 && falseNum == 0) issueVO.setLikelihood(0);
            else{
                double likelihood = trueNum*1.0/(trueNum+falseNum);

                issueVO.setLikelihood(likelihood);
                issueVO.setState(issueBasic.getState());
            }

            issueVOList.add(issueVO);
        }
        return issueVOList;
    }

    @ResponseBody
    @RequestMapping("/orderedIssues")
    public List<IssueInfoVO> getOrderedIssueList(@RequestParam(value = "priority",required = false) String priority,
                                      @RequestParam(value = "patternId",required = false) String patternId,
                                      @RequestParam(value = "state",required = false) String state){
        AVersion version = (AVersion) session.getAttribute("version");
        List<IssueBasic> issueBasicList = issueService.getIssueList(version.getVersionId(),priority,patternId,state,Constants.IsFilter.DISABLE);
        List<IssueInfoVO> issueVOList = new ArrayList<>();
        for(IssueBasic issueBasic : issueBasicList){
            IssueInfoVO issueInfoVO = new IssueInfoVO();
            PatternInfo patternInfo = patternService.getPatternInfo(issueBasic.getPatternId());
            issueInfoVO.setIssueId(issueBasic.getIssueId());
            issueInfoVO.setPatternName(patternInfo.getPatternName());
            issueInfoVO.setFileName(issueBasic.getFileName());
            issueInfoVO.setPriority(issueBasic.getPriority());
            issueInfoVO.setType(issueBasic.getType());
            issueInfoVO.setKingdom(issueBasic.getKingdom());
            issueInfoVO.setTargetFunction(issueBasic.getTargetFunction());
            issueInfoVO.setStartLine(issueBasic.getStartLine());
            //TODO
            issueInfoVO.setFuncStartLine(issueBasic.getFuncStartLine());
            issueInfoVO.setFuncEndLine(issueBasic.getFuncEndLine());
            int trueNum = patternInfo.gettNum();
            int falseNum = patternInfo.getfNum();

            if (trueNum == 0 && falseNum == 0) issueInfoVO.setLikelihood(0);
            else{
                double likelihood = trueNum*1.0/(trueNum+falseNum);

                issueInfoVO.setLikelihood(likelihood);
                issueInfoVO.setState(issueBasic.getState());
            }

            issueVOList.add(issueInfoVO);
        }
        return issueVOList;
    }

    @ResponseBody
    @RequestMapping("/start/issue/{issueId}")
    public int startIssue(@PathVariable("issueId") String issueId){
        System.out.println("true");
        IssueBasic issue = issueService.getIssue(issueId);
        PatternInfoWithBLOBs pattern = patternService.getPatternInfoWithBlobs(issue.getPatternId());

        if (issue.getState().equals(Constants.IssueState.FALSE)) pattern.setfNum(pattern.getfNum()-1);
        pattern.settNum(pattern.gettNum()+1);
        patternService.updatePatternLikelihood(pattern);

        issue.setState(Constants.IssueState.TRUE);
        return issueService.updateIssue(issue);
    }

    @ResponseBody
    @RequestMapping("/predict/issue/{issueId}")
    public int predictIssue(@PathVariable("issueId") String issueId) throws IOException {
        IssueBasic issue = issueService.getIssue(issueId);
        double[] thisVec = getVecWithModel(issueService.getFuncSnippet(issue));
        PatternInfoWithBLOBs pattern = patternService.getPatternInfoWithBlobs(issue.getPatternId());
        String patternName = pattern.getPatternName();
        String fileName = patternName.replace(":","_") + ".txt";
        double[] thatVec = readDoublesFromFile("deepLearning/" +fileName);
        double sim = cosineSimilarity(thisVec,thatVec);
        System.out.println(sim);
        if(sim > 0){
            if (issue.getState().equals(Constants.IssueState.FALSE)) pattern.setfNum(pattern.getfNum()-1);
            pattern.settNum(pattern.gettNum()+1);
            patternService.updatePatternLikelihood(pattern);

            issue.setState(Constants.IssueState.TRUE);
            return issueService.updateIssue(issue);
        }else{
            if (issue.getState().equals(Constants.IssueState.TRUE)) pattern.settNum(pattern.gettNum()-1);
            pattern.setfNum(pattern.getfNum()+1);
            patternService.updatePatternLikelihood(pattern);

            issue.setState(Constants.IssueState.FALSE);
            return issueService.updateIssue(issue);
        }
    }

    @ResponseBody
    @RequestMapping("/write")
    public int writeSnippets() throws IOException {
        List<IssueBasic> allIssueBasicList = issueService.getAllIssue();
//        issueService.writeAllFuncSnippet(allIssueBasicList,"deepLearning/all.txt");
        issueService.getFeatureVec(allIssueBasicList,"deepLearning/feature");
        List<PatternInfoWithBLOBs> patternInfoList = patternService.getPatternInfoList();
        for(PatternInfoWithBLOBs patternInfoWithBLOBs:patternInfoList){
            issueService.getFeatureVec(issueService.getIssueListByPatternId(patternInfoWithBLOBs.getPatternInfoId()),"deepLearning/" + patternInfoWithBLOBs.getPatternName());
        }
        return 1;
    }

    @ResponseBody
    @RequestMapping("/end/issue/{issueId}")
    public int endIssue(@PathVariable("issueId") String issueId){
        System.out.println("false");
        IssueBasic issue = issueService.getIssue(issueId);
        PatternInfoWithBLOBs pattern = patternService.getPatternInfoWithBlobs(issue.getPatternId());

        if (issue.getState().equals(Constants.IssueState.TRUE)) pattern.settNum(pattern.gettNum()-1);
        pattern.setfNum(pattern.getfNum()+1);
        patternService.updatePatternLikelihood(pattern);

        issue.setState(Constants.IssueState.FALSE);
        return issueService.updateIssue(issue);
    }

    @ResponseBody
    @RequestMapping("/issue/{issueId}")
    public IssueBasic getIssueBasic(@PathVariable("issueId") String issueId){
        return issueService.getIssue(issueId);
    }

    @ResponseBody
    @RequestMapping("/feature")
    public int extractFeature(){
        AUser user = (AUser) session.getAttribute("user");
        FileUtil.generateTrainArff(user.getUsername(),featureService.getIssueFeature(issueService.getClassifiedIssueList()));
        return 1;
    }

    @ResponseBody
    @RequestMapping("/updateState/{issueId}/{state}")
    public int updateState(@PathVariable("issueId") String issueId,
                           @PathVariable("state") int state){
        IssueBasic issue = issueService.getIssue(issueId);
        PatternInfoWithBLOBs pattern = patternService.getPatternInfoWithBlobs(issue.getPatternId());

        if (state==0){
            issue.setState(Constants.IssueState.FALSE);
            pattern.setfNum(pattern.getfNum()+1);
        }
        else{
            issue.setState(Constants.IssueState.TRUE);
            pattern.settNum(pattern.gettNum()+1);
        }
        patternService.updatePatternLikelihood(pattern);
        return issueService.updateIssue(issue);
    }

    @ResponseBody
    @RequestMapping("/predict")
    public List<IssueVO> predict(){
        AVersion version = (AVersion) session.getAttribute("version");
        AUser user = (AUser) session.getAttribute("user");
        List<IssueBasic> issueBasicList = issueService.getIssueList(version.getVersionId(),Constants.IssueState.UNCLASSIFIED,Constants.IsFilter.IGNORE);
        FileUtil.generateTestArff(user.getUsername(),featureService.getIssueFeature(issueBasicList));
        File trainFile = new File(UPLOADED_FOLDER+"/data/"+user.getUsername()+"/train.arff");
        File testFile = new File(UPLOADED_FOLDER+"/data/"+user.getUsername()+"/test.arff");
        double[] res = WekaUtil.predict(trainFile,testFile);
        List<IssueVO> issueVOList = new ArrayList<>();
        int index = 0;

        for(IssueBasic issueBasic : issueBasicList){
            String fileName = issueBasic.getFileName();
            if (!fileName.endsWith("java")) continue;
            IssueVO issueVO = new IssueVO();
            PatternInfo patternInfo = patternService.getPatternInfo(issueBasic.getPatternId());
            issueVO.setIssueId(issueBasic.getIssueId());
            issueVO.setPatternName(patternInfo.getPatternName());
            issueVO.setFileName(issueBasic.getFileName());
            issueVO.setPriority(issueBasic.getPriority());
//            System.out.println(res[index]);
            issueVO.setLikelihood(res[index]);
            if (res[index]<0.5) issueVO.setState(Constants.IssueState.FALSE);
            else issueVO.setState(Constants.IssueState.TRUE);
            index++;
            issueVOList.add(issueVO);
        }
        return issueVOList;
    }

    @ResponseBody
    @RequestMapping("/issue/info/{issueId}")
    public IssueInfoVO getIssueInfo(@PathVariable("issueId") String issueId) throws IOException {
        IssueInfoVO issueInfoVO = new IssueInfoVO();
        IssueBasic issueBasic = issueService.getIssue(issueId);
        PatternInfoWithBLOBs patternInfo = patternService.getPatternInfoWithBlobs(issueBasic.getPatternId());
        issueInfoVO.setPatternId(patternInfo.getPatternInfoId());
        issueInfoVO.setPatternName(patternInfo.getPatternName());
        issueInfoVO.setKingdom(issueBasic.getKingdom());
        issueInfoVO.setFileName(issueBasic.getFileName());
        issueInfoVO.setFilePath(issueBasic.getFilePath());
        issueInfoVO.setStartLine(issueBasic.getStartLine());
        issueInfoVO.setSnippet(issueBasic.getSnippet());
        issueInfoVO.setTargetFunction(issueBasic.getTargetFunction());
        issueInfoVO.setDescription(issueBasic.getDescription());
        issueInfoVO.setExplanation(patternInfo.getExplanation());
        issueInfoVO.setRecommendation(patternInfo.getRecommendation());
        issueInfoVO.setTip(patternInfo.getTip());
        issueInfoVO.setType(issueBasic.getType());
        //TODO
//        issueInfoVO.setFuncSnippet(issueService.getFuncSnippet(issueBasic));
        issueInfoVO.setFuncSnippet(issueService.getFuncSnippet(issueBasic));
        return issueInfoVO;
    }
//未经测试
    @ResponseBody
    @RequestMapping("/generateReport")
    public String generateReport(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException, ParserConfigurationException, SAXException {
        AUser user = (AUser) session.getAttribute("user");
        Project project = (Project) session.getAttribute("project");
        AVersion version = (AVersion) session.getAttribute("version");

        ATemplate template = templateService.getUsedTemplate(user.getId());

        if (template == null) return "null";

        String filePath = UPLOADED_FOLDER+"/doc/"+user.getUsername()+"/"+project.getProjectName()+"/"+version.getVersionName()+".doc";
        File file = new File(filePath);
        if (!file.exists()){
            List<PatternInfoWithBLOBs> patternInfoList = patternService.getPatternInfoList();
            List<PatternDocVO> patternDocVOList = new ArrayList<>();
            int i=1;
            for(PatternInfoWithBLOBs patternInfo:patternInfoList){
                List<IssueBasic> issueBasicList = issueService.getIssueListByPatternId(version.getVersionId(),patternInfo.getPatternInfoId());
                if (issueBasicList.size() == 0) continue;
                List<IssueDocVO> issueDocVOList = new ArrayList<>();
                for(IssueBasic issueBasic:issueBasicList){
                    IssueDocVO issueDocVO = new IssueDocVO();
                    issueDocVO.setId(i);
                    issueDocVO.setPriority(issueBasic.getPriority());
                    issueDocVO.setKingdom(issueBasic.getKingdom());
                    issueDocVO.setFilePath(issueBasic.getFilePath());
                    issueDocVO.setTargetFunction(issueBasic.getTargetFunction());
                    issueDocVO.setStartLine(issueBasic.getStartLine());
                    issueDocVO.setDescription(issueBasic.getDescription());
                    if (issueBasic.getSnippet() != null) issueDocVO.setSnippet(issueBasic.getSnippet());
                    else issueDocVO.setSnippet("");
                    issueDocVO.setState(issueBasic.getState());
                    issueDocVOList.add(issueDocVO);
                    i++;
                }
                PatternDocVO patternDocVO = new PatternDocVO();
                int tNum = patternInfo.gettNum();
                int fNum = patternInfo.getfNum();
                double likelihood;
                if (tNum == 0 && fNum == 0) likelihood = 0;
                else likelihood = tNum*1.0/(tNum+fNum);
                patternDocVO.setPatternName(patternInfo.getPatternName());
                patternDocVO.setExplanation(patternInfo.getExplanation());
                patternDocVO.setRecommendation(patternInfo.getRecommendation());
                if (patternInfo.getTip().length() != 0) patternDocVO.setTip(patternInfo.getTip());
                else patternDocVO.setTip("暂无");
                patternDocVO.setIssueDocVOList(issueDocVOList);
                patternDocVO.setLikelihood(likelihood);
                patternDocVOList.add(patternDocVO);
            }
            DocUtil.generateDoc(patternDocVOList,project.getProjectName(),version.getVersionName(),user.getUsername(),template.getFilePath());
        }

        StringBuilder sb = new StringBuilder();
        try{
            response.setContentType("application/x-download;charset=UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename="+version.getVersionName()+".doc");
            FileReader fr = new FileReader(file);
            char[] buffer = new char[23];
            int length;
            while ((length = fr.read(buffer)) != -1) {
                sb.append(buffer,0,length);
            }
            fr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
