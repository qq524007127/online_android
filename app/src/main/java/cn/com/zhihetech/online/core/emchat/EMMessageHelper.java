package cn.com.zhihetech.online.core.emchat;

import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by ShenYunjie on 2016/4/7.
 */
public class EMMessageHelper {

    public static String getMessageBody(EMMessage message) {
        String msgTxt;
        switch (message.getType()) {
            case TXT:
                msgTxt = getTextMessageBody(message);
                break;
            case IMAGE:
                msgTxt = "[图片]";
                break;
            case VIDEO:
                msgTxt = "[视频]";
                break;
            case LOCATION:
                msgTxt = "[地理位置]";
                break;
            case VOICE:
                msgTxt = "[音频]";
                break;
            case FILE:
                msgTxt = "[文件]";
                break;
            case CMD:
                msgTxt = "[透传消息]";
                break;
            default:
                msgTxt = "[未知信息]";
        }
        return msgTxt;
    }

    public static String getTextMessageBody(EMMessage message) {
        TextMessageBody messageBody = (TextMessageBody) message.getBody();
        int msgType = 0;
        try {
            msgType = Integer.parseInt(message.getStringAttribute(Constant.EXTEND_MESSAGE_TYPE,"0"));
        } catch (Exception e) {
        }
        switch (msgType) {
            case Constant.EXTEND_MESSAGE_RED_ENVELOP:
                return "[红包]";
            case Constant.EXTEND_MESSAGE_SHOP_LINK:
                return "[店铺链接]";
            case Constant.EXTEND_MESSAGE_GOODS_LINK:
                return "[商品链接]";
            case Constant.EXTEND_MESSAGE_SECKILL_GOODS:
                return "[秒杀商品]";
            default:
                return messageBody.getMessage();
        }
    }
}
