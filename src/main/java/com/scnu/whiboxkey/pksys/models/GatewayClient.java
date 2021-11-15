package com.scnu.whiboxkey.pksys.models;

import com.scnu.whiboxkey.pksys.utils.JWTUtils;

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

    //客户端身份序列，用于验证客户端身份，每个客户端身份序列唯一
    @Column(nullable = false, unique = true, length = 50)
    private String serial;

    //客户端所在的IP地址，用于验证客户端当前所处ip是否准确
    @Column(nullable = false, length = 50)
    private String ip;

    //客户端所对应的IP地址，初始化客户端信息时生成
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text", nullable = false)
    private String token;

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

    //与服务端对应的身份序列
    @Column(nullable = false)
    private String gatewayServerSerial;

    //一个客户端密钥对应上行密钥和下行密钥
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="client_key",
            joinColumns={ @JoinColumn(name="client_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="key_id",referencedColumnName="id")})
//    private Collection<KeyMsg> keyMsgList = new ArrayList<KeyMsg>();
    private Collection<KeyMsg> keyMsgList;

    public GatewayClient() {
    }

    public GatewayClient(String serial, String ip, Boolean vaild, String gatewayServerSerial) {
        this.serial = serial;
        this.ip = ip;
        this.vaild = vaild;
        this.gatewayServerSerial = gatewayServerSerial;
        this.keyMsgList = new ArrayList<KeyMsg>();
        this.token = JWTUtils.createToken(serial, ip);
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getGatewayServerSerial() {
        return gatewayServerSerial;
    }

    public void setGatewayServerSerial(String gatewayServerSerial) {
        this.gatewayServerSerial = gatewayServerSerial;
    }

    public Collection<KeyMsg> getKeyMsgList() {
        return keyMsgList;
    }

    public void setKeyMsgList(Collection<KeyMsg> keyMsgList) {
        this.keyMsgList = keyMsgList;
    }
}
