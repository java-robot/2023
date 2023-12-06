package org.nju.demo.pojo.vo;

import org.nju.demo.entity.Knowledge;

import java.util.List;

public class IssueInfoVO {
    private String patternId;
    private String patternName;
    private String kingdom;
    private String fileName;
    private String filePath;
    private int startLine;
    private String snippet;

    private String funcSnippet;
    private String targetFunction;
    private String description;
    private String explanation;
    private String recommendation;
    private String tip;
    private double likelihood;
    private String issueId;
    private String state;

    private int funcStartLine;

    private int funcEndLine;
    private String priority;
    private String type;
    private List<Knowledge> knowledgeList;

    public String getFuncSnippet() {
        return funcSnippet;
    }

    public void setFuncSnippet(String funcSnippet) {
        this.funcSnippet = funcSnippet;
    }

    public String getPatternId() {
        return patternId;
    }

    public void setPatternId(String patternId) {
        this.patternId = patternId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public String getKingdom() {
        return kingdom;
    }

    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTargetFunction() {
        return targetFunction;
    }

    public void setTargetFunction(String targetFunction) {
        this.targetFunction = targetFunction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public double getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(double likelihood) {
        this.likelihood = likelihood;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getFuncStartLine() {
        return funcStartLine;
    }

    public void setFuncStartLine(int funcStartLine) {
        this.funcStartLine = funcStartLine;
    }

    public int getFuncEndLine() {
        return funcEndLine;
    }

    public void setFuncEndLine(int funcEndLine) {
        this.funcEndLine = funcEndLine;
    }

    public List<Knowledge> getKnowledgeList() {
        return knowledgeList;
    }

    public void setKnowledgeList(List<Knowledge> knowledgeList) {
        this.knowledgeList = knowledgeList;
    }
}
