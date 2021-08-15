package com.scnu.whiboxkey.pksys.models;

public class CommunicationMsg {
    //算法名称，目前仅支持Dummyround-WBSM4
    private String algorithm;

    //是上行密钥还下行密钥，参数为up或down
    private String updown;

    //是加密还是解密，参数为enc或dec
    private String enc;

    //是加密模式，CBC或GCM模式，参数为cbc或gcm
    private String mode;

    //初始值
    private String iv;

    //可选：附加值
    private String aad;

    //进行操作的消息
    private String text;

    public CommunicationMsg() {
    }

    public CommunicationMsg(String algorithm, String updown, String enc, String mode, String iv, String aad, String text) {
        this.algorithm = algorithm;
        this.updown = updown;
        this.enc = enc;
        this.mode = mode;
        this.iv = iv;
        this.aad = aad;
        this.text = text;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getUpdown() {
        return updown;
    }

    public void setUpdown(String updown) {
        this.updown = updown;
    }

    public String getEnc() {
        return enc;
    }

    public void setEnc(String enc) {
        this.enc = enc;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getAad() {
        return aad;
    }

    public void setAad(String aad) {
        this.aad = aad;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
