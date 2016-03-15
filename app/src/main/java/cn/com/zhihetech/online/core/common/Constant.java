package cn.com.zhihetech.online.core.common;

import java.io.Serializable;

/**
 * 系统常量
 * Created by ShenYunjie on 2015/11/17.
 */
public class Constant implements Serializable, Cloneable {

    public final static boolean DEBUG = true;

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";    //默认日期格式
    public final static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";    //默认日期时间格式

    /**
     * 商家自己推荐商品数量最大值
     */
    public final static int MERCHANT_RECOMMEND_MAX = 20;

    /**
     * 默认密码
     */
    public final static String DEFAULT_PASSWORD = "123456";

    /**
     * 数据分页相关，默认从第一页
     */
    public final static int DEFAULT_PAGE = 1;
    /**
     * 数据分页相关，默认每页数据显示条数（默认每页显示20条数据）
     */
    public final static int DEFAULT_ROWS = 20;

    /**
     * 商户、商品审核状态
     */
    public final static int EXAMINE_STATE_NOT_SUBMIT = 1;   //未提交审核
    public final static int EXAMINE_STATE_SUBMITED = 2;   //待审核
    public final static int EXAMINE_STATE_EXAMINED_OK = 3;   //已审核通过
    public final static int EXAMINE_STATE_EXAMINED1_NUOK = 4;   //已审核未通过

    /**
     * 活动审核状态
     */
    public final static int ACTIVITY_STATE_UNSBUMIT = 1; //未提交申请
    public final static int ACTIVITY_STATE_SBUMITED = 2; //已提交申请未审核
    public final static int ACTIVITY_STATE_EXAMINED_OK = 3; //已审核通过
    public final static int ACTIVITY_STATE_EXAMINED_NOT = 4; //已审核未通过
    public final static int ACTIVITY_STATE_STARTED = 5; //活动已开始
    public final static int ACTIVITY_STATE_FNISHED = 6; //活动已结束

    /**
     * 优惠券类型
     */
    public final static int COUPON_DISCOUNT_TYPE = 1;   //打折卷
    public final static int COUPON_VOUCHER_TYPE = 2;    //代金券

    /**
     * App端viewType相关
     */
    public final static int BANNER_VIEWTYPE_TARGET = 1; //跳转到viewTarget指定模块
    public final static int BANNER_VIEWTYPE_MERCHANT = 2;//跳转到商户页面
    public final static int BANNER_VIEWTYPE_GOODS = 3;//跳转到商品页面
    public final static int BANNER_VIEWTYPE_ACTIVITY = 4;//跳转到活动页面
    public final static int BANNER_VIEWTYPE_PAGE = 5;//跳转到指定页面
    /**
     * APP端viewTarget相关
     */
    public final static String NAVIGATION_ONE = "1";//投诉维权
    public final static String NAVIGATION_TWO = "2";//购物中心
    public final static String NAVIGATION_THREE = "3";//活动专区
    public final static String NAVIGATION_FOUR = "4";//特色街区
    public final static String NAVIGATION_FIVE = "5";//特色店
    public final static String NAVIGATION_SIX = "6";//分类

    /**
     * 验证码类型
     */
    public final static int SECURITY_REGISTER = 1;   //注册验证码
    public final static int SECURITY_ALTERPWD = 2; //修改密码验证码

    /**
     * 验证码有效期
     */
    public final static int VALIDITY_ONE_MINUTE = 60 * 1000;
    /**
     * 搜索类型相关
     */
    public final static int SEARCH_GOODS = 1;//商品
    public final static int SEARCH_MERCHANT = 2;//商家
    public final static String ENCODING = "UTF-8";

    /**
     * 用户类型，分为普通用户与买手用户
     */
    public final static int COMMON_USER = 1;    //普通用户
    public final static int MERCHANT_USER = 2; //商家用户
    public final static int BUYER_USER = 3; //买手用户

    /**
     * 轮播图所处的位置
     */
    public final static int BANNER_MAIN = 1;  //主页面上的轮播图
    public final static int BANNER_DAILY_NEW = 2;  //每日上新上的轮播图
    public final static int BANNER_BIG_BRAND = 3;  //大品牌上的轮播图
    public final static int BANNER_BUY_KUNMING = 4; //买昆明上的轮播图
    public final static int BANNER_BUY_PREFECTURES = 5; //购地州上的轮播图
    public final static int BANNER_TYPE_CATEGOR = 6;   //分类上的轮播图
    public final static int BANNER_TYPE_ACTIVITY_ZONE = 7; //活动专区上的轮播图

    /**
     * 订单状态
     */
    public static final int ORDER_STATE_NO_SUBMIT = 1;  //订单未提交
    public static final int ORDER_STATE_NO_PAYMENT = 2;   //订单已提交，待支付
    public static final int ORDER_STATE_NO_DISPATCHER = 3;  //订单已经支付，等待发货
    public static final int ORDER_STATE_ALREADY_DISPATCHER = 4;  //订单已发货，等待确认收货
    public static final int ORDER_STATE_ALREADY_DELIVER = 5;   //订单已经确认收货
    public static final int ORDER_STATE_ALREADY_CANCEL = 6;  //订单已取消
    public static final int ORDER_STATE_WAIT_REFUND = 7;  //订单等待退款
    public static final int ORDER_STATE_ALREADY_REFUND = 8; //订单退款成功
    public static final int ORDER_STATE_ALREADY_EVALUATE = 9;  //已评价

    /**
     * 消息扩展属性名称
     */
    public final static String EXTEND_USER_NICK_NAME = "nickName"; //昵称
    public final static String EXTEND_USER_HEAD_IMG = "headerImg";//头像
    public final static String EXTEND_USER_ID = "userID";//用户ID
    public final static String EXTEND_USER_TYPE = "userType";//userType用户类型
    public final static String EXTEND_MESSAGE_TYPE = "messageType"; //自定义扩展消息类型

    /**
     * 自定义消息扩展用户类型
     */
    public final static int EXTEND_NORMAL_USER = 1;    //普通用户
    public final static int EXTEND_MERCHANT_USER = 2;  //商家
    public final static int EXTEND_SERVICE_USER = 3;   //客服

    /**
     * 自定义消息扩展消息类型
     */
    public final static int EXTEND_MESSAGE_RED_ENVELOP = 1; //红包信息
    public final static int EXTEND_MESSAGE_SHOP_LINK = 2;   //店铺链接
    public final static int EXTEND_MESSAGE_GOODS_LINK = 3;  //商品链接
    public final static int EXTEND_MESSAGE_SECKILL_GOODS = 4;   //秒杀商品链接


    /**
     * 请求接口地址
     */
    public static final String DOMAIN = "http://192.168.1.11:8080/";
    public static final String HOST = DOMAIN + "api/";

    public static final String USER_PROTOCOL_URL = DOMAIN + "common/user_protocol.html";    //用户协议
    public static final String QINIU_UPLOAD_TOKEN_URL = DOMAIN + "qiniu/api/image/uptoken"; //获取七牛上传token
    public static final String APP_NEW_VERSION_URL = HOST + "app/lastVersion";  //后去App最新版本

    public static final String VER_CODE_URL = HOST + "securityCode/get";   //获取短信验证码
    public static final String VER_CODE_VERIY_URL = HOST + "securityCode/verify";   //验证码验证
    public static final String USER_REGISTER_URL = HOST + "user/register"; //用户注册
    public static final String USER_LOGIN_URL = HOST + "user/login";    //用户登录
    public static final String USER_CHANGE_PWD_URL = HOST + "user/changePwd";    //普通用户修改登录密码
    public static final String USER_RESET_PWD_URL = HOST + "user/resetPwd";    //普通用户重置登录密码
    public static final String USER_CHANGE_HEADER_URL = HOST + "user/updatePortrait";    //修改用户头像
    public static final String USER_MODIFY_INFO_URL = HOST + "user/updateUserInfo";    //修改用户基本信息
    public static final String USER_APPLY_TAKE_WALLET_MONEY_URL = HOST + "userWithdraw/apply";    //用户申请提现请求

    public static final String USER_WITH_DRAW_RESULTS_URL = HOST + "user/{0}/withdrawRecord";    //用户提现记录

    public static final String FEATURED_BLOCKS_URL = HOST + "featuredBlock/list";  //特色街区列表
    public static final String SHOPPING_CENTERS_URL = HOST + "shoppingCenter/list";  //购物中心列表

    public static final String BANNERS_URL = HOST + "banners";  //获取主页顶部轮播图
    public static final String NAVIGATIONS_URL = HOST + "navigations";   //获取主页导航菜单
    public static final String ACTIVITY_LIST_URL = HOST + "activity/list";    //获取正在开始或即将开始的活动
    public static final String ACTIVITY_INFO_URL = HOST + "activity/{0}";    //根据ID获取活动
    public static final String CATEGORY_ACTIVITIES_URL = HOST + "goodsAttributeSet/{0}/activities";    //获取正在开始或即将开始的活动
    public static final String MERCHANT_LIST_URL = HOST + "merchantOrGoods/list";    //获取商家数据列表
    public static final String FEATURED_BLOCK_OR_SHOPPING_CENTER_MERCHANTS_URL = HOST + "scOrFbOrFs/list";    //获取每日上新数据
    public static final String MERCHANT_URL = HOST + "merchant/{0}";    //根据ID获取商家基本信息
    public static final String MERCHANT_LOGIN_URL = HOST + "merchant/login";    //商家登录
    public static final String CHECK_MERCHANT_FUCOS_STATE_URL = HOST + "checkFocus";    //查询商家关注状态
    public static final String FOCUS_MERCHANT_URL = HOST + "focusMerchant/add";    //关注商家
    public static final String USER_FOCUS_MERCHANTS_URL = HOST + "user/{0}/focusMerchants";    //获取关注商家列表
    public static final String CANCEL_FOCUS_MERCHANT_URL = HOST + "cancelFocusMerchant ";    //取消商家收藏
    public static final String MERCHANT_ACTIVITIES_URL = HOST + "merchant/{0}/activities";    //获取指定商家的活动
    public static final String MERCHANT_STARTED_GOODSES_URL = HOST + "merchant/{0}/startedActivity/list";   //获取指定商家已经开始的活动
    public static final String MERCHANT_GOODSES_URL = HOST + "goodses/{0}";   //获取指定商家的商品

    public static final String GOODS_BANNERS_URL = HOST + "goodsBanners/{0}";   //获取指定商品的轮播图
    public static final String GOODS_DETAILS_URL = HOST + "goodsDetails/{0}";   //获取指定商品的详情图
    public static final String GOODS_URL = HOST + "goods/{0}";   //根据ID获取商品
    public static final String GOODSES_URL = HOST + "goodses";   //根据ID获取商品
    public static final String CATEGORY_GOODSES_URL = HOST + "goodsAttributeSet/{0}/goodses"; //获取指定类别的商品

    public static final String CHECK_FOCUS_GOODS_URL = HOST + "checkFocusGoods";  //检查用户对商品的收藏状态
    public static final String FOCUS_GOODS_URL = HOST + "focusGoods/add";  //收藏商品
    public static final String UN_FOCUS_GOODS_URL = HOST + "cancelFocusGoods";  //取消收藏商品
    public static final String ADD_SHOPPING_CART_URL = HOST + "shoppingCart/add";  //添加商品到购物车
    public static final String USER_SHOPPING_CARTS_URL = HOST + "user/{0}/shoppingCarts";  //查询用户的购物车数据
    public static final String DELETE_SHOPPING_CARTS_URL = HOST + "shoppingCarts/delete";  //批量删除指定购物车数据
    public static final String UPDATE_SHOPPING_CART_AMOUNT_URL = HOST + "shoppingCart/update";  //更新购物车商品数量
    public static final String CATEGORIES_URL = HOST + "goodsAttributeSet/list";  //获取分类列表
    public static final String CATEGORY_MERCHANTS_URL = HOST + "goodsAttributeSet/{0}/Merchants"; //根据商品类别获取商家
    public static final String USER_RECEIPT_ADDRESSES_URL = HOST + "user/{0}/receivedGoodsAddresses"; //获取指定用户的收货地址
    public static final String ADD_OR_UPDATE_RECEIPT_ADDRESS_URL = HOST + "receivedGoodsAddress/addOrUpdate"; //添加或更新收货地址信息
    public static final String DELETE_RECEIPT_ADDRESS_URL = HOST + "receivedGoodsAddress/{0}/delete"; //添加或更新收货地址信息
    public static final String USER_DEFAULT_RECEIPT_ADDRESS_URL = HOST + "user/{0}/defaultReceivedAddress"; //用户默认收货地址
    public static final String USER_FAVORITES_GOODSES_URL = HOST + "user/{0}/focusGoodses"; //获取用户收藏的商品
    public static final String USER_WALLET_TOTAL_MONEY_URL = HOST + "user/{0}/walletTotalMoney"; //获取用户收藏的商品

    public static final String ORDER_ADD_URL = HOST + "order/add";    //提交订单
    public static final String ACTIVITY_GOODS_ORDER_ADD_URL = HOST + "activityGoodsOrder/add";    //提交活动商品订单
    public static final String USER_ORDERS_URL = HOST + "order/list"; //用户订单
    public static final String ORDER_PAY_URL = HOST + "order/{0}/pay"; //支付指定订单
    public static final String ORDER_CANCEL_URL = HOST + "order/{0}/cancel"; //取消指定订单
    public static final String ORDER_DELETE_URL = HOST + "order/{0}/delete"; //删除指定订单
    public static final String ORDER_REFUND_URL = HOST + "order/{0}/refund"; //申请退款
    public static final String ORDER_RECEIPT_URL = HOST + "order/{0}/confirmReceipt"; //订单签收
    public static final String ORDER_DETAIL_URL = HOST + "order/{0}/orderDetails"; //根据订单ID获取订单详情
    public static final String ORDER_EVALUATE_URL = HOST + "order/evaluate"; //订单评价
    public static final String CHARGE_URL = HOST + "charge/get";    //支付API-获取charge信息
    public static final String CITY_AREAS_URL = HOST + "area/rootAreas"; //获取根级（市级）区域

    public static final String ACTIVITY_FANS_ADD_URL = HOST + "activityFans/add";    //添加活动会员
    public static final String ACTIVITY_FANS_LIST_URL = HOST + "activity/{0}/activityFans";    //添加活动会员

    public static final String MERCHANT_ACTIVITY_RED_ENVELOPS_URL = HOST + "redEnvelop/list";  //商家的活动红包
    public static final String UPDATE_RED_ENVELOP_STATU_URL = HOST + "redEnvelop/{0}/sended";  //更新红包状态为已发送
    public static final String GRAD_RED_ENVELOP_URL = HOST + "redEnvelopItem/get";  //抢红包
    public static final String RED_ENVELOP_ITEM_URL = HOST + "redEnvelopItem/{0}/details";  //抢红包
    public static final String EXTRACT_RED_ENVELOP_ITEM_URL = HOST + "wallet/putRedEnvelop";  //将红包存入我的钱包
    public static final String USER_RED_ENVELOP_ITEMS_URL = HOST + "user/{0}/redEnvelopItem/list";  //指定用户的红包
    public static final String MERCHANT_SECKILL_GOODSES_URL = HOST + "activity/activityGoods/list";  //指定活动和商家的活动商品
    public static final String ACTIVITY_GOODS_URL = HOST + "activityGoods/{0}"; //获取指定活动商品详情
    public static final String ADMIN_CHANGE_PASSWORD_URL = HOST + "admin/changePwd"; //更改商家登录密码
}
