package com.scnu.whiboxkey.pksys.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "gatewayClient")
public class GatewayClient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //客户端唯一标识，用于验证客户端身份
    @Column(nullable = false, unique = true, length = 50)
    private String serial;

    //有效性，是否有效，有效才允许分发密钥表
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "boolean")
    private Boolean vaild;

    //对象创建时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    //对象创更新时间
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    //一个客户端密钥对应上行密钥和下行密钥
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="client_whibox_key",
            joinColumns={ @JoinColumn(name="client_key_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="whibox_key_id",referencedColumnName="id")})
    private Collection<KeyMsg> keyMsgList = new ArrayList<KeyMsg>();

    public GatewayClient() {
    }

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

    public Collection<KeyMsg> getWhiboxKeyList() {
        return keyMsgList;
    }

    public void setWhiboxKeyList(Collection<KeyMsg> keyMsgList) {
        this.keyMsgList = keyMsgList;
    }
}
