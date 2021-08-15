package com.scnu.whiboxkey.pksys.models;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "gatewayServer")
public class GatewayServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //服务端唯一标识，用于验证服务端身份
    @Column(nullable = false, unique = true, length = 50)
    private String serial;

    //有效性，服务端是否是否有效
    @Column(nullable = false)
    @org.hibernate.annotations.Type(type = "boolean")
    private Boolean vaild;

    //服务端关联客户端关系
    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name="gateway_server_client",
            joinColumns={ @JoinColumn(name="server_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="client_id",referencedColumnName="id")})
    private Collection<GatewayClient> clientKeyList = new ArrayList<GatewayClient>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    public GatewayServer() {
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

    public Collection<GatewayClient> getClientKeyList() {
        return clientKeyList;
    }

    public void setClientKeyList(Collection<GatewayClient> clientKeyList) {
        this.clientKeyList = clientKeyList;
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
