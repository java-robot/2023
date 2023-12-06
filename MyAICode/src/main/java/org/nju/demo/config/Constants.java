package org.nju.demo.config;

public class Constants {

    public static class Type {
        public static final String[] fortify = {"Input Validation and Representation","API Abuse","Security Features","Time and State","Errors","Code Quality","Encapsulation","Environment"};
        //TODO
        public static final String[] spotbugs = {};

    }

    public static class CodeState{
        public static final String initial = "initial";
        public static final String usingCode = "using code";
    }

    public static class IssueType{
        public static final String NEW = "new";

        public static final String ORIGINAL = "original";
    }

    public static class IssueState{
        public static final String UNCLASSIFIED = "Unclassified";
        public static final String UNKNOWN = "Unknown";
        public static final String TRUE = "True";
        public static final String FALSE = "False";
    }

    public static class IsFilter{
        public static final int IGNORE = -1;
        public static final int ENABLE = 1;
        public static final int DISABLE = 0;
    }

    public static class IsEnable{
        public static final int YES = 1;
        public static final int NO = 0;
    }

    public static class Priority{
        public static final String LOW = "Low";
        public static final String MEDIUM = "Medium";
        public static final String HIGH = "High";
        public static final String CRITICAL = "Critical";
    }

    public static class ProjectState{
        public static final String SHOWN = "shown";
        public static final String HIDEN = "hiden";

    }

    public static class VersionState{
        public static final String INIT = "Null";
        public static final String FIRST = "First";
    }

    public static class TemplateState{
        public static final int ENABLE = 1;
        public static final int DISABLE = 0;
    }

    public static class UserInfo{
        public static final String EMAIL = "123@nju.edu.cn";
        public static final String TELEPHONE = "10000";
    }

}