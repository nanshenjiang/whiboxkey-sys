package com.scnu.whiboxkey.pksys.models;

public class CommunicationMsg {
    //算法名称，目前仅支持WBSM4
    private String algorithm;

    //是加密模式，CBC或GCM模式，参数为cbc或gcm
    private String mode;

    //初始值
    private String iv;

    //进行操作的消息
    private String text;

    public CommunicationMsg() {
    }

    public CommunicationMsg(String algorithm, String mode, String iv, String text) {
        this.algorithm = algorithm;
        this.mode = mode;
        this.iv = iv;
        this.text = text;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
