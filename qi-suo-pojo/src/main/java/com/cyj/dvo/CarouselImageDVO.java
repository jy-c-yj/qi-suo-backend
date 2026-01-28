package com.cyj.dvo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImageDVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 唯一标识符，可用于前端展示
     */
    @TableField("uid")
    private String uid;

    /**
     * 图片描述
     */
    @TableField("description")
    private String description;

    /**
     * 图片URL地址
     */
    @TableField("url")
    private String url;

    /**
     * 是否启用：1-启用，0-禁用
     */
    @TableField("is_enabled")
    private Boolean isEnabled;

//    /**
//     * 是否删除：1-已删除，0-未删除
//     */
//    @TableField("is_deleted")
//    private Boolean isDeleted;

    /**
     * 创建人
     */
    @TableField("created_by")
    private String createdBy;

    /**
     * 修改人
     */
    @TableField("modified_by")
    private String modifiedBy;

    /**
     * 创建时间
     */
    @TableField("created_time")
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    @TableField("modified_time")
    private LocalDateTime modifiedTime;
}
