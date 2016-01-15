package cn.com.zhihetech.online.bean;


import java.util.Date;
import java.util.List;
import java.util.Set;

import cn.com.zhihetech.online.core.common.Constant;

/**
 * Created by YangDaiChun on 2015/11/12.
 */

public class Merchant extends BaseBean {

    private String merchantId;
    private ImgInfo coverImg;   //商家封面图片
    private ImgInfo headerImg;  //商家主页顶部图片
    private District district;  //商家所在商圈
    private String merchName;
    private String merchTell;   //企业联系电话
    private String address; //详细地址
    private int longitude;  //商家定位经度
    private int latitude;   //商家定位纬度
    private String alipayCode;  //支付宝账号
    private String wxpayCode;   //微信账号
    private int merchOrder; //商家排列顺序
    private boolean permit; //是否有效
    private Date createDate;    //入驻时间
    private String orgCode; //组织机构代码，必填
    private String licenseCode; //工商执照注册码
    private String taxRegCode;  //税务登记证号
    private String businScope;  //经营范围
    private int emplyCount;  //企业规模（员工人数）
    private boolean kunmingFlag = false; //是否是昆明的商家private boolean kunmingFlag = false; //是否是昆明的商家
    private String merchantDetails;
    private Set<GoodsAttributeSet> categories;  //经营商品类别

    /*运营者（联系人）相关*/
    private String contactName; //企业联系人姓名
    private String contactPartAndPositon;   //联系人部门与职位
    private String contactMobileNO;   //联系人手机号码(必须为手机号码)
    private String contactEmail;   //联系人电子邮箱
    private String contactID;   //联系人身份证号

    /*提交材料相关*/
    private ImgInfo opraterIDPhoto;   //运营者手持身份证照片
    private ImgInfo orgPhoto;   //组织机构代码证原件照片
    private ImgInfo busLicePhoto;   //工商营业执照原件照片
    private ImgInfo applyLetterPhoto;   //加盖公章的申请认证公函（与商家纠纷事件裁定等）照片

    /*审核情况*/
    private int examinState = Constant.EXAMINE_STATE_NOT_SUBMIT;    //审核状态（默认为未提交审核）
    private String examinMsg;   //审核信息
    private String invitcode;   //受邀请码

    /*商品有关*/
    private List<Goods> recommendGoodses;  //推荐商品
    private long goodsNum; //该商家有多少商品
    private boolean isActivating;  //该商家现在是否有活动正在进行


    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }


    public ImgInfo getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ImgInfo coverImg) {
        this.coverImg = coverImg;
    }


    public ImgInfo getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(ImgInfo headerImg) {
        this.headerImg = headerImg;
    }


    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public String getMerchName() {
        return merchName;
    }

    public void setMerchName(String merchName) {
        this.merchName = merchName;
    }

    public String getMerchTell() {
        return merchTell;
    }

    public void setMerchTell(String merchTell) {
        this.merchTell = merchTell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public String getAlipayCode() {
        return alipayCode;
    }

    public void setAlipayCode(String alipayCode) {
        this.alipayCode = alipayCode;
    }

    public String getWxpayCode() {
        return wxpayCode;
    }

    public void setWxpayCode(String wxpayCode) {
        this.wxpayCode = wxpayCode;
    }

    public int getMerchOrder() {
        return merchOrder;
    }

    public void setMerchOrder(int merchOrder) {
        this.merchOrder = merchOrder;
    }

    public boolean isPermit() {
        return permit;
    }

    public void setPermit(boolean permit) {
        this.permit = permit;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getExaminState() {
        return examinState;
    }

    public void setExaminState(int examinState) {
        this.examinState = examinState;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getLicenseCode() {
        return licenseCode;
    }

    public void setLicenseCode(String licenseCode) {
        this.licenseCode = licenseCode;
    }

    public String getTaxRegCode() {
        return taxRegCode;
    }

    public void setTaxRegCode(String taxRegCode) {
        this.taxRegCode = taxRegCode;
    }

    public String getBusinScope() {
        return businScope;
    }

    public void setBusinScope(String businScope) {
        this.businScope = businScope;
    }

    public int getEmplyCount() {
        return emplyCount;
    }

    public void setEmplyCount(int emplyCount) {
        this.emplyCount = emplyCount;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPartAndPositon() {
        return contactPartAndPositon;
    }

    public void setContactPartAndPositon(String contactPartAndPositon) {
        this.contactPartAndPositon = contactPartAndPositon;
    }

    public String getContactMobileNO() {
        return contactMobileNO;
    }

    public void setContactMobileNO(String contactMobileNO) {
        this.contactMobileNO = contactMobileNO;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public ImgInfo getOpraterIDPhoto() {
        return opraterIDPhoto;
    }

    public void setOpraterIDPhoto(ImgInfo opraterIDPhoto) {
        this.opraterIDPhoto = opraterIDPhoto;
    }

    public ImgInfo getOrgPhoto() {
        return orgPhoto;
    }

    public void setOrgPhoto(ImgInfo orgPhoto) {
        this.orgPhoto = orgPhoto;
    }

    public ImgInfo getBusLicePhoto() {
        return busLicePhoto;
    }

    public void setBusLicePhoto(ImgInfo busLicePhoto) {
        this.busLicePhoto = busLicePhoto;
    }

    public ImgInfo getApplyLetterPhoto() {
        return applyLetterPhoto;
    }

    public void setApplyLetterPhoto(ImgInfo applyLetterPhoto) {
        this.applyLetterPhoto = applyLetterPhoto;
    }

    public String getInvitcode() {
        return invitcode;
    }

    public void setInvitcode(String invitcode) {
        this.invitcode = invitcode;
    }

    public String getExaminMsg() {
        return examinMsg;
    }

    public void setExaminMsg(String examinMsg) {
        this.examinMsg = examinMsg;
    }

    public List<Goods> getRecommendGoodses() {
        return recommendGoodses;
    }

    public void setRecommendGoodses(List<Goods> recommendGoodses) {
        this.recommendGoodses = recommendGoodses;
    }

    public long getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(long goodsNum) {
        this.goodsNum = goodsNum;
    }

    public boolean getIsActivating() {
        return isActivating;
    }

    public void setIsActivating(boolean isActivating) {
        this.isActivating = isActivating;
    }

    public boolean isKunmingFlag() {
        return kunmingFlag;
    }

    public void setKunmingFlag(boolean kunmingFlag) {
        this.kunmingFlag = kunmingFlag;
    }

    public Set<GoodsAttributeSet> getCategories() {
        return categories;
    }

    public void setCategories(Set<GoodsAttributeSet> categories) {
        this.categories = categories;
    }

    public String getMerchantDetails() {
        return merchantDetails;
    }

    public void setMerchantDetails(String merchantDetails) {
        this.merchantDetails = merchantDetails;
    }
}
