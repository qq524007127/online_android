package cn.com.zhihetech.online.bean;


/**
 * Created by ShenYunjie on 2016/4/25.
 */
public class MessageBody extends BaseBean {

    private String bodyId;
    private ChatMessage message;
    private ChatMessage.MessageType type = ChatMessage.MessageType.txt;//消息类型。txt:文本消息, img:图片, loc：位置, audio：语音
    private String msg;//消息内容
    private float length;//语音时长，单位为秒，这个属性只有语音消息有
    private String url;//图片语音等文件的网络url，图片和语音消息有这个属性
    private String filename;//文件名字，图片和语音消息有这个属性
    private String secret;//获取文件的secret，图片和语音消息有这个属性
    private float lat;//发送的位置的纬度，只有位置消息有这个属性
    private float lng;//位置经度，只有位置消息有这个属性
    private String addr; //位置消息详细地址，只有位置消息有这个属性
    private String thumb;   //缩略图地址
    private String thumb_secret;    //上传缩略图后返回

    public String getBodyId() {
        return bodyId;
    }

    public void setBodyId(String bodyId) {
        this.bodyId = bodyId;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage.MessageType getType() {
        return type;
    }

    public void setType(ChatMessage.MessageType type) {
        this.type = this.type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getThumb_secret() {
        return thumb_secret;
    }

    public void setThumb_secret(String thumb_secret) {
        this.thumb_secret = thumb_secret;
    }
}
