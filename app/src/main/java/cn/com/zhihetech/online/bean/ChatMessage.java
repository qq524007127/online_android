package cn.com.zhihetech.online.bean;

import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by ShenYunjie on 2016/4/25.
 */
public class ChatMessage extends BaseBean {

    public enum ChatType {
        chat, groupchat   //群聊（包括聊天室）
    }

    public enum MessageType {
        txt, img, audio, loc, video, cmd
    }

    private String messageId;   //自定义聊天记录ID
    private String type;
    private String from; //发送人username
    private String msg_id; //消息id
    private ChatType chat_type;//用来判断单聊还是群聊。chat:单聊，groupchat:群聊
    private long timestamp; //消息发送时间
    private String to;//接收人的username或者接收group的id
    private Date createDate;//业务服务器保存时间

    private MessagePayload payload;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public ChatType getChat_type() {
        return chat_type;
    }

    public void setChat_type(ChatType chat_type) {
        this.chat_type = chat_type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public MessagePayload getPayload() {
        return payload;
    }

    public void setPayload(MessagePayload payload) {
        this.payload = payload;
    }

    public EMMessage createEMMessage() {
        EMMessage message = null;
        if (this.payload.getBodies() != null) {
            for (MessageBody msgBody : this.payload.getBodies()) {
                ChatMessage.MessageType messageType = msgBody.getType();
                switch (messageType) {
                    case img:
                        message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
                        ImageMessageBody body = new ImageMessageBody();
                        body.setRemoteUrl(msgBody.getUrl());
                        body.setFileName(msgBody.getFilename());
                        body.setSecret(msgBody.getSecret());
                        body.setThumbnailSecret(msgBody.getThumb_secret());
                        body.setThumbnailUrl(msgBody.getThumb());
                        // 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
                        //body.setSendOriginalImage(true);
                        message.addBody(body);
                        break;
                    case loc:
                        message = EMMessage.createSendMessage(EMMessage.Type.LOCATION);
                        LocationMessageBody locBody = new LocationMessageBody(msgBody.getAddr(), msgBody.getLat(), msgBody.getLng());
                        message.addBody(locBody);
                        break;
                    case audio:
                        message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
                        VoiceMessageBody voiceBody = new VoiceMessageBody(null, (int) msgBody.getLength());
                        voiceBody.setSecret(msgBody.getSecret());
                        voiceBody.setFileName(msgBody.getFilename());
                        voiceBody.setRemoteUrl(msgBody.getUrl());
                        message.addBody(voiceBody);
                        break;
                    default:
                        message = EMMessage.createSendMessage(EMMessage.Type.TXT);
                        //设置消息body
                        TextMessageBody txtBody = new TextMessageBody(msgBody.getMsg());
                        message.addBody(txtBody);
                }
            }
        }

        Map<String, String> ext = this.payload.getExt();
        if (ext != null && !ext.isEmpty()) {
            for (String key : ext.keySet()) {
                message.setAttribute(key, ext.get(key));
            }
        }
        message.setChatType(this.chat_type == ChatType.chat ? EMMessage.ChatType.Chat : EMMessage.ChatType.ChatRoom);
        message.setReceipt(this.to);
        message.setFrom(this.from);
        message.setMsgId(this.msg_id);
        message.setMsgTime(this.timestamp);
        return message;
    }
}
