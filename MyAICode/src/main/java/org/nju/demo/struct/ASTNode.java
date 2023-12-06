package org.nju.demo.struct;

import java.util.List;
import java.util.Map;

public class ASTNode {
    private String id;
    private String kind;
    private Map<String, Object> loc;
    private Map<String, Object> range;
    private String name;
    private Map<String, Object> type;
    private List<ASTNode> inner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
        
    }

    public Map<String, Object> getLoc() {
        return loc;
    }

    public void setLoc(Map<String, Object> loc) {
        this.loc = loc;
    }

    public Map<String, Object> getRange() {
        return range;
    }

    public void setRange(Map<String, Object> range) {
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getType() {
        return type;
    }

    public void setType(Map<String, Object> type) {
        this.type = type;
    }

    public List<ASTNode> getInner() {
        return inner;
    }

    public void setInner(List<ASTNode> inner) {
        this.inner = inner;
    }
}
