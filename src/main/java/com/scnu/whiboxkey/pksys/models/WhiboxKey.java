package com.scnu.whiboxkey.pksys.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "whiboxKey")
public class WhiboxKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //黑盒密钥，加密后存储
    @Column(length = 50)
    private String blackKey;

    //版本号
    @Column(nullable = false)
    private Long version;

    //白盒密钥表，以文件形式存储，存储文件路径
    @Column(length = 254)
    private String keyFpath;

    //白盒密钥表的通行证，含该通行证才能下载密钥表
    @Column(length = 50, unique = true)
    private String pass;

    //白盒密钥表有效至时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date effectiveTime;

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

    public WhiboxKey() {
    }

    public WhiboxKey(String blackKey, Long version, String keyFpath, String pass, Date effectiveTime) {
        this.blackKey = blackKey;
        this.version = version;
        this.keyFpath = keyFpath;
        this.pass = pass;
        this.effectiveTime = effectiveTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBlackKey() {
        return blackKey;
    }

    public void setBlackKey(String blackKey) {
        this.blackKey = blackKey;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getKeyFpath() {
        return keyFpath;
    }

    public void setKeyFpath(String keyFpath) {
        this.keyFpath = keyFpath;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
