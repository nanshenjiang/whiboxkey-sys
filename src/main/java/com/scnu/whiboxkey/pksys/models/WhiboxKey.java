package com.scnu.whiboxkey.pksys.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "whiboxKey")
public class WhiboxKey implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //上行密钥为true，下行密钥为false
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "boolean")
    private Boolean upOrDown;

    //黑盒密钥，加密后存储
    @Column(length = 50)
    private String blackKey;

    //白盒加密算法名字
    @Column(nullable = false, length = 50)
    private String whiboxAlgName;

    //白盒加密密钥表，以文件形式存储
    @Column(length = 254)
    private String encKfpath;

    //白盒解密密钥表，以文件形式存储
    @Column(length = 254)
    private String decKfpath;

    //白盒密钥表有效持续的时间（单位：天）
    @Column(nullable = false)
    private Integer duration;

    //白盒密钥表有效至时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTime;

    //白盒密钥表的通行证，含该通行证才能下载密钥表
    @Column(length = 50, unique = true)
    private String pass;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getUpOrDown() {
        return upOrDown;
    }

    public void setUpOrDown(Boolean upOrDown) {
        this.upOrDown = upOrDown;
    }

    public String getBlackKey() {
        return blackKey;
    }

    public void setBlackKey(String blackKey) {
        this.blackKey = blackKey;
    }

    public String getWhiboxAlgName() {
        return whiboxAlgName;
    }

    public void setWhiboxAlgName(String whiboxAlgName) {
        this.whiboxAlgName = whiboxAlgName;
    }

    public String getEncKfpath() {
        return encKfpath;
    }

    public void setEncKfpath(String encKfpath) {
        this.encKfpath = encKfpath;
    }

    public String getDecKfpath() {
        return decKfpath;
    }

    public void setDecKfpath(String decKfpath) {
        this.decKfpath = decKfpath;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
