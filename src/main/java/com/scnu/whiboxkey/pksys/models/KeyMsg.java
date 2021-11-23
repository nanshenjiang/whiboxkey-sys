package com.scnu.whiboxkey.pksys.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "keyMsg")
public class KeyMsg implements Serializable  {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //上行密钥为true，下行密钥为false
    //上行密钥生成加密表，下行密钥生成解密表
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "boolean")
    private Boolean upOrDown;

    //白盒加密算法名字
    @Column(nullable = false, length = 50)
    private String whiboxAlgName;

    //一份密钥对应多个版本白盒密钥
    //以防密钥过期，但该密钥仍在使用
    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinTable(name="key_wbkey",
            joinColumns={ @JoinColumn(name="key_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="wb_key_id",referencedColumnName="id")})
//    private Collection<WhiboxKey> whiboxKeyList = new ArrayList<WhiboxKey>();
    private Collection<WhiboxKey> whiboxKeyList;

    //白盒密钥表有效持续的时间（单位：天）
    @Column(nullable = false)
    private Integer duration;

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

    public KeyMsg() {
    }

    public KeyMsg(Boolean upOrDown, String whiboxAlgName, Integer duration) {
        this.upOrDown = upOrDown;
        this.whiboxAlgName = whiboxAlgName;
        this.duration = duration;
        this.whiboxKeyList = new ArrayList<>();
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

    public String getWhiboxAlgName() {
        return whiboxAlgName;
    }

    public void setWhiboxAlgName(String whiboxAlgName) {
        this.whiboxAlgName = whiboxAlgName;
    }

    public Collection<WhiboxKey> getWhiboxKeyList() {
        return whiboxKeyList;
    }

    public void setWhiboxKeyList(Collection<WhiboxKey> whiboxKeyList) {
        this.whiboxKeyList = whiboxKeyList;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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
