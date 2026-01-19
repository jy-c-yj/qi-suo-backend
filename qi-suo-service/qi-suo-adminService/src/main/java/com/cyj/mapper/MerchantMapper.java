package com.cyj.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyj.entity.Merchant;
import com.cyj.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantMapper extends BaseMapper<Merchant> {
}
