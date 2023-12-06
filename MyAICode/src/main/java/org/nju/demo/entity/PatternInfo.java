package org.nju.demo.entity;

public class PatternInfo {
    private String patternInfoId;

    private String patternName;

    private Integer tNum;

    private Integer fNum;

    public PatternInfo(){
        this.tNum = 0;
        this.fNum = 0;
    }

    public String getPatternInfoId() {
        return patternInfoId;
    }

    public void setPatternInfoId(String patternInfoId) {
        this.patternInfoId = patternInfoId;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public Integer gettNum() {
        return tNum;
    }

    public void settNum(Integer tNum) {
        this.tNum = tNum;
    }

    public Integer getfNum() {
        return fNum;
    }

    public void setfNum(Integer fNum) {
        this.fNum = fNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", patternInfoId=").append(patternInfoId);
        sb.append(", patternName=").append(patternName);
        sb.append(", tNum=").append(tNum);
        sb.append(", fNum=").append(fNum);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        PatternInfo other = (PatternInfo) that;
        return (this.getPatternInfoId() == null ? other.getPatternInfoId() == null : this.getPatternInfoId().equals(other.getPatternInfoId()))
            && (this.getPatternName() == null ? other.getPatternName() == null : this.getPatternName().equals(other.getPatternName()))
            && (this.gettNum() == null ? other.gettNum() == null : this.gettNum().equals(other.gettNum()))
            && (this.getfNum() == null ? other.getfNum() == null : this.getfNum().equals(other.getfNum()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getPatternInfoId() == null) ? 0 : getPatternInfoId().hashCode());
        result = prime * result + ((getPatternName() == null) ? 0 : getPatternName().hashCode());
        result = prime * result + ((gettNum() == null) ? 0 : gettNum().hashCode());
        result = prime * result + ((getfNum() == null) ? 0 : getfNum().hashCode());
        return result;
    }
}