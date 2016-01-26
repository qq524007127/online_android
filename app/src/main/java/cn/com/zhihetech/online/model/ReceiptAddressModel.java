package cn.com.zhihetech.online.model;

import android.support.annotation.NonNull;

import org.xutils.common.Callback;

import java.text.MessageFormat;

import cn.com.zhihetech.online.bean.ReceivedGoodsAddress;
import cn.com.zhihetech.online.core.common.Constant;
import cn.com.zhihetech.online.core.common.Pager;
import cn.com.zhihetech.online.core.common.ResponseMessage;
import cn.com.zhihetech.online.core.http.ObjectCallback;
import cn.com.zhihetech.online.core.http.PageDataCallback;
import cn.com.zhihetech.online.core.http.ResponseMessageCallback;
import cn.com.zhihetech.online.core.util.StringUtils;

/**
 * Created by ShenYunjie on 2016/1/25.
 */
public class ReceiptAddressModel extends BaseModel<ReceivedGoodsAddress> {

    /**
     * 根据用户ID获取用户的收货地址
     *
     * @param callback
     * @param usreId
     * @return
     */
    public Callback.Cancelable getReceiptAdressesByUserId(PageDataCallback<ReceivedGoodsAddress> callback, @NonNull String usreId) {
        String url = MessageFormat.format(Constant.USER_RECEIPT_ADDRESSES_URL, usreId);
        ModelParams params = new ModelParams().addPager(new Pager(Integer.MAX_VALUE));
        return getPageData(url, params, callback);
    }

    /**
     * 更新或添加收货地址
     *
     * @return
     */
    public Callback.Cancelable updateOrSaveReceiptAddress(ResponseMessageCallback<ReceivedGoodsAddress> callback, @NonNull ReceivedGoodsAddress address) {
        return postResponseMessage(Constant.ADD_OR_UPDATE_RECEIPT_ADDRESS_URL, createAddressParams(address), callback);
    }

    /**
     * 删除指定ID的收货地址
     *
     * @param callback
     * @param addressId
     * @return
     */
    public Callback.Cancelable deleteReceiptAddressBiId(ObjectCallback<ResponseMessage> callback, @NonNull String addressId) {
        String url = MessageFormat.format(Constant.DELETE_RECEIPT_ADDRESS_URL, addressId);
        return new SimpleModel<ResponseMessage>(ResponseMessage.class).postObject(url, null, callback);
    }

    /**
     * 获取用户的默认收货地址
     *
     * @param callback
     * @param userId
     * @return
     */
    public Callback.Cancelable getDefaultReceiptAdress(ResponseMessageCallback<ReceivedGoodsAddress> callback, @NonNull String userId) {
        String url = MessageFormat.format(Constant.USER_DEFAULT_RECEIPT_ADDRESS_URL, userId);
        return getResponseMessage(url, null, callback);
    }

    /**
     * 根据收货地址创建参数
     *
     * @param address
     * @return
     */
    private ModelParams createAddressParams(ReceivedGoodsAddress address) {
        ModelParams params = new ModelParams();
        params.addParam("receiverName", address.getReceiverName()).addParam("receiverPhone", address.getReceiverPhone())
                .addParam("detailAddress", address.getDetailAddress()).addParam("user.userId", address.getUser().getUserId())
                .addParam("defaultAddress", String.valueOf(address.isDefaultAddress()));
        if (!StringUtils.isEmpty(address.getAddressId())) {
            params.addParam("addressId", address.getAddressId());
        }
        /*if (address.getUser() != null && !StringUtils.isEmpty(address.getUser().getUserId())) {
            params.addParam("user.userId", address.getUser().getUserId());
        }*/
        return params;
    }
}
