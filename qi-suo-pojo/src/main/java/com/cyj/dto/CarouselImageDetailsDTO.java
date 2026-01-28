package com.cyj.dto;


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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarouselImageDetailsDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String uid;

    private String description;

    private String url;

    private Boolean isEnabled;

    private String createdBy;

    private String modifiedBy;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;
}
