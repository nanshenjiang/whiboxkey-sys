package com.scnu.whiboxkey.pksys.models;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

public class GCandKM {
    private String serial;

    private Boolean vaild;

    private Long sid;

    private String upalgname;

    private Integer upduration;

    private String downalgname;

    private Integer downduration;

    public GCandKM() {
    }

    public GCandKM(String serial, Boolean vaild, Long sid, String upalgname, Integer upduration, String downalgname, Integer downduration) {
        this.serial = serial;
        this.vaild = vaild;
        this.sid = sid;
        this.upalgname = upalgname;
        this.upduration = upduration;
        this.downalgname = downalgname;
        this.downduration = downduration;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public Boolean getVaild() {
        return vaild;
    }

    public void setVaild(Boolean vaild) {
        this.vaild = vaild;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public String getUpalgname() {
        return upalgname;
    }

    public void setUpalgname(String upalgname) {
        this.upalgname = upalgname;
    }

    public Integer getUpduration() {
        return upduration;
    }

    public void setUpduration(Integer upduration) {
        this.upduration = upduration;
    }

    public String getDownalgname() {
        return downalgname;
    }

    public void setDownalgname(String downalgname) {
        this.downalgname = downalgname;
    }

    public Integer getDownduration() {
        return downduration;
    }

    public void setDownduration(Integer downduration) {
        this.downduration = downduration;
    }
}
