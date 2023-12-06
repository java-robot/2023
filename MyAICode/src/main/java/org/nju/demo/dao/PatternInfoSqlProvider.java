package org.nju.demo.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.jdbc.SQL;
import org.nju.demo.entity.PatternInfoExample.Criteria;
import org.nju.demo.entity.PatternInfoExample.Criterion;
import org.nju.demo.entity.PatternInfoExample;
import org.nju.demo.entity.PatternInfoWithBLOBs;

public class PatternInfoSqlProvider {

    public String countByExample(PatternInfoExample example) {
        SQL sql = new SQL();
        sql.SELECT("count(*)").FROM("pattern_info");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String deleteByExample(PatternInfoExample example) {
        SQL sql = new SQL();
        sql.DELETE_FROM("pattern_info");
        applyWhere(sql, example, false);
        return sql.toString();
    }

    public String insertSelective(PatternInfoWithBLOBs record) {
        SQL sql = new SQL();
        sql.INSERT_INTO("pattern_info");
        
        if (record.getPatternInfoId() != null) {
            sql.VALUES("pattern_info_id", "#{patternInfoId,jdbcType=VARCHAR}");
        }
        
        if (record.getExplanation() != null) {
            sql.VALUES("explanation", "#{explanation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getRecommendation() != null) {
            sql.VALUES("recommendation", "#{recommendation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getTip() != null) {
            sql.VALUES("tip", "#{tip,jdbcType=LONGVARCHAR}");
        }
        
        return sql.toString();
    }

    public String selectByExampleWithBLOBs(PatternInfoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("pattern_info_id");
        } else {
            sql.SELECT("pattern_info_id");
        }
        sql.SELECT("pattern_name");
        sql.SELECT("t_num");
        sql.SELECT("f_num");
        sql.SELECT("explanation");
        sql.SELECT("recommendation");
        sql.SELECT("tip");
        sql.FROM("pattern_info");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String selectByExample(PatternInfoExample example) {
        SQL sql = new SQL();
        if (example != null && example.isDistinct()) {
            sql.SELECT_DISTINCT("pattern_info_id");
        } else {
            sql.SELECT("pattern_info_id");
        }
        sql.SELECT("pattern_name");
        sql.SELECT("t_num");
        sql.SELECT("f_num");
        sql.FROM("pattern_info");
        applyWhere(sql, example, false);
        
        if (example != null && example.getOrderByClause() != null) {
            sql.ORDER_BY(example.getOrderByClause());
        }
        
        return sql.toString();
    }

    public String updateByExampleSelective(Map<String, Object> parameter) {
        PatternInfoWithBLOBs record = (PatternInfoWithBLOBs) parameter.get("record");
        PatternInfoExample example = (PatternInfoExample) parameter.get("example");
        
        SQL sql = new SQL();
        sql.UPDATE("pattern_info");
        
        if (record.getPatternInfoId() != null) {
            sql.SET("pattern_info_id = #{record.patternInfoId,jdbcType=VARCHAR}");
        }
        
        if (record.getExplanation() != null) {
            sql.SET("explanation = #{record.explanation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getRecommendation() != null) {
            sql.SET("recommendation = #{record.recommendation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getTip() != null) {
            sql.SET("tip = #{record.tip,jdbcType=LONGVARCHAR}");
        }
        
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExampleWithBLOBs(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("pattern_info");
        
        sql.SET("pattern_info_id = #{record.patternInfoId,jdbcType=VARCHAR}");
        sql.SET("pattern_name = #{record.patternName,jdbcType=VARCHAR}");
        sql.SET("t_num = #{record.tNum,jdbcType=INTEGER}");
        sql.SET("f_num = #{record.fNum,jdbcType=INTEGER}");
        sql.SET("explanation = #{record.explanation,jdbcType=LONGVARCHAR}");
        sql.SET("recommendation = #{record.recommendation,jdbcType=LONGVARCHAR}");
        sql.SET("tip = #{record.tip,jdbcType=LONGVARCHAR}");
        
        PatternInfoExample example = (PatternInfoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByExample(Map<String, Object> parameter) {
        SQL sql = new SQL();
        sql.UPDATE("pattern_info");
        
        sql.SET("pattern_info_id = #{record.patternInfoId,jdbcType=VARCHAR}");
        sql.SET("pattern_name = #{record.patternName,jdbcType=VARCHAR}");
        sql.SET("t_num = #{record.tNum,jdbcType=INTEGER}");
        sql.SET("f_num = #{record.fNum,jdbcType=INTEGER}");
        
        PatternInfoExample example = (PatternInfoExample) parameter.get("example");
        applyWhere(sql, example, true);
        return sql.toString();
    }

    public String updateByPrimaryKeySelective(PatternInfoWithBLOBs record) {
        SQL sql = new SQL();
        sql.UPDATE("pattern_info");

        if (record.getPatternName() != null) {
            sql.SET("pattern_name = #{patternName,jdbcType=VARCHAR}");
        }

        if (record.gettNum() != null) {
            sql.SET("t_num = #{tNum,jdbcType=INTEGER}");
        }

        if (record.getfNum() != null) {
            sql.SET("f_num = #{fNum,jdbcType=INTEGER}");
        }
        
        if (record.getExplanation() != null) {
            sql.SET("explanation = #{explanation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getRecommendation() != null) {
            sql.SET("recommendation = #{recommendation,jdbcType=LONGVARCHAR}");
        }
        
        if (record.getTip() != null) {
            sql.SET("tip = #{tip,jdbcType=LONGVARCHAR}");
        }
        
        sql.WHERE("pattern_info_id = #{patternInfoId,jdbcType=VARCHAR}");
        
        return sql.toString();
    }

    protected void applyWhere(SQL sql, PatternInfoExample example, boolean includeExamplePhrase) {
        if (example == null) {
            return;
        }
        
        String parmPhrase1;
        String parmPhrase1_th;
        String parmPhrase2;
        String parmPhrase2_th;
        String parmPhrase3;
        String parmPhrase3_th;
        if (includeExamplePhrase) {
            parmPhrase1 = "%s #{example.oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{example.oredCriteria[%d].allCriteria[%d].value} and #{example.oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{example.oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{example.oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{example.oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{example.oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        } else {
            parmPhrase1 = "%s #{oredCriteria[%d].allCriteria[%d].value}";
            parmPhrase1_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s}";
            parmPhrase2 = "%s #{oredCriteria[%d].allCriteria[%d].value} and #{oredCriteria[%d].criteria[%d].secondValue}";
            parmPhrase2_th = "%s #{oredCriteria[%d].allCriteria[%d].value,typeHandler=%s} and #{oredCriteria[%d].criteria[%d].secondValue,typeHandler=%s}";
            parmPhrase3 = "#{oredCriteria[%d].allCriteria[%d].value[%d]}";
            parmPhrase3_th = "#{oredCriteria[%d].allCriteria[%d].value[%d],typeHandler=%s}";
        }
        
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = example.getOredCriteria();
        boolean firstCriteria = true;
        for (int i = 0; i < oredCriteria.size(); i++) {
            Criteria criteria = oredCriteria.get(i);
            if (criteria.isValid()) {
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" or ");
                }
                
                sb.append('(');
                List<Criterion> criterions = criteria.getAllCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" and ");
                    }
                    
                    if (criterion.isNoValue()) {
                        sb.append(criterion.getCondition());
                    } else if (criterion.isSingleValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase1, criterion.getCondition(), i, j));
                        } else {
                            sb.append(String.format(parmPhrase1_th, criterion.getCondition(), i, j,criterion.getTypeHandler()));
                        }
                    } else if (criterion.isBetweenValue()) {
                        if (criterion.getTypeHandler() == null) {
                            sb.append(String.format(parmPhrase2, criterion.getCondition(), i, j, i, j));
                        } else {
                            sb.append(String.format(parmPhrase2_th, criterion.getCondition(), i, j, criterion.getTypeHandler(), i, j, criterion.getTypeHandler()));
                        }
                    } else if (criterion.isListValue()) {
                        sb.append(criterion.getCondition());
                        sb.append(" (");
                        List<?> listItems = (List<?>) criterion.getValue();
                        boolean comma = false;
                        for (int k = 0; k < listItems.size(); k++) {
                            if (comma) {
                                sb.append(", ");
                            } else {
                                comma = true;
                            }
                            if (criterion.getTypeHandler() == null) {
                                sb.append(String.format(parmPhrase3, i, j, k));
                            } else {
                                sb.append(String.format(parmPhrase3_th, i, j, k, criterion.getTypeHandler()));
                            }
                        }
                        sb.append(')');
                    }
                }
                sb.append(')');
            }
        }
        
        if (sb.length() > 0) {
            sql.WHERE(sb.toString());
        }
    }
}