package com.cyj.common;

public class WhitelistURL {

    /**
     * 令牌刷新url
     */
    public static final String Refresh_Token_Url = "/api/auth/jwt/refresh";
    /**
     * 头像上传url
     */
    public static final String File_Upload_Url = "/api/file/upload";
    /**
     * 注册接口
     */
    public static final String User_Register_Url = "/api/auth/user/register";

    /**
     * 商家注册接口
     * 与用户注册对称：注册本就该匿名可访问，漏掉它会导致无 token 时 403
     */
    public static final String Merchant_Register_Url = "/api/auth/merchant/register";

    /**
     * 健康检查（网关那条是 /health，这两个带了服务前缀，需要单独放行）
     */
    public static final String Auth_Health_Url = "/api/auth/health";
    public static final String File_Health_Url = "/api/file/health";

    /**
     * 登陆界面背景图片显示
     */
    public static final String home_getBackGroundImage_Url = "/api/user/home/getBackGroundImage";

    /**
     * 首页分类树
     */
    public static final String User_Home_Category_Url = "/api/user/home/category";

    /**
     * 首页轮播图
     */
    public static final String User_Home_Carousel_Url = "/api/user/home/getCarouselImageList";

    /**
     * 商品浏览（游客可逛）
     */
    public static final String User_Product_Browse_Url = "/api/user/userProduct/**";

    /**
     * 商品浏览落到商家服务的三个查询接口。
     * 刻意用精确路径而不是 /api/merchant/product/query* —— queryProductList 是管理台的商品审核列表，
     * 必须留在鉴权后面，不能被通配一起放行。
     */
    public static final String Merchant_Product_Hot_Url = "/api/merchant/product/queryHotProduct";
    public static final String Merchant_Product_Detail_Url = "/api/merchant/product/queryProductDetail";
    public static final String Merchant_Product_Cond_Url = "/api/merchant/product/queryProductByCondition";
}
