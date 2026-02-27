package com.cyj.dvo;


import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MerchantInformationDVO {
    /**
     * 商家ID
     */
    private Long merchantId;

    /**
     * 商家名
     */
    private String merchantName;

    /**
     * 商家密码
     */
    private String merchantPassword;

    /**
     * 店铺名称
     */
    private String shopName;

    /**
     * 店铺Logo
     */
    private String shopLogoUrl;

    /**
     * 店铺描述
     */
    private String description;

    /**
     * 联系人姓名
     */
    private String contactName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系邮箱
     */
    private String contactEmail;

    /**
     * 商家状态：0-待审核，1-正常，2-关闭，3-禁用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人
     */
    private Long updateBy;

}
