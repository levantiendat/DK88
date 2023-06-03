package com.example.dk88.Model;

import java.io.Serializable;

public class GroupInfo implements Serializable {
    private String lophp;
    private int current;
    private int max;
    private String groupID;

    public GroupInfo(String lophp, int current, int max, String groupID) {
        this.lophp = lophp;
        this.current = current;
        this.max = max;
        this.groupID = groupID;
    }

    public GroupInfo() {
        this.lophp = "";
        this.current = 0;
        this.max = 0;
        this.groupID="";
    }

    public String getLophp() {
        return lophp;
    }

    public void setLophp(String lophp) {
        this.lophp = lophp;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
}
