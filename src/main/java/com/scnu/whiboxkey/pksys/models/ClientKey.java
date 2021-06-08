package com.scnu.whiboxkey.pksys.models;

import org.springframework.boot.autoconfigure.condition.ConditionalOnCloudPlatform;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientKey")
public class ClientKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //客户端唯一标识，用于验证客户端身份
    @Column(nullable = false, unique = true, length = 50)
    private String serial;

    //黑盒密钥
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

    //白盒密钥表的通行证，含该通行证才能下载密钥表
    @Column(length = 50, unique = true)
    private String pass;

    //有效性，是否有效，有效才允许分发密钥表
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "boolean")
    private Boolean vaild;

    //白盒密钥表有效持续的时间（单位：天）
    @Column(nullable = false)
    private Integer duration;

    //白盒密钥表有效至时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTime;

    //对象创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //对象创更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public ClientKey() {
    }

    @PrePersist
    protected void onCreate() {
        createTime = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "ClientKey{" +
                "id=" + id +
                ", serial='" + serial + '\'' +
                ", blackKey='" + blackKey + '\'' +
                ", whiboxAlgName='" + whiboxAlgName + '\'' +
                ", encKfpath='" + encKfpath + '\'' +
                ", decKfpath='" + decKfpath + '\'' +
                ", pass='" + pass + '\'' +
                ", vaild=" + vaild +
                ", duration=" + duration +
                ", effectiveTime=" + effectiveTime +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Boolean getVaild() {
        return vaild;
    }

    public void setVaild(Boolean vaild) {
        this.vaild = vaild;
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
