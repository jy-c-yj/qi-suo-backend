package com.cyj.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.entity.Merchant;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.MerchantManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商家信息管理实现类
 */
@Service
@Slf4j
public class MerchantManagementServiceImpl implements MerchantManagementService {
    @Autowired
    private MerchantMapper merchantMapper;

    /**
     * 查询商家列表的方法
     * @param adminMerchantListSelectConditionDTO 商家列表查询条件数据传输对象
     * @return Result<PageResult<AdminMerchantManagementLDVO>> 返回结果对象
     */
    @Override
    public Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        PageResult<AdminMerchantManagementLDVO> pageResult = new PageResult<>();
        //查询数据库
        Page<Merchant> page = new Page<>(adminMerchantListSelectConditionDTO.getPageNum(), adminMerchantListSelectConditionDTO.getPageSize());
        List<AdminMerchantManagementLDVO> merchantList = merchantMapper.list(page, adminMerchantListSelectConditionDTO);
        if(merchantList.isEmpty()){
            return Result.error("商户列表为空");
        }
        pageResult.setTotal(page.getTotal());
        pageResult.setRows(merchantList);

        return Result.success(pageResult);
    }

    /**
     * 设置状态的方法
     * @param id 用于标识需要设置状态的唯一标识符
     * @return 返回一个Result<String>类型的结果对象
     */
    @Override
    public Result<String> setStatus(Long id, Integer pageNum) {
        //查询当前状态
        Merchant merchant = merchantMapper.selectById(id);
        if (merchant == null) {
            return Result.error("商户不存在");
        }
        //设置状态
        if(merchant.getStatus()==1){
            merchant.setStatus(0);
        }else {
            merchant.setStatus(1);
        }
        merchantMapper.updateById(merchant);
        return Result.success("设置成功");
    }

    /**
     * 按商家id取商家名
     * @param id 商家id
     * @return 返回商家名
     */
    @Override
    public Result<String> getMerchantNameById(Long id) {
        if(id == null){
            return Result.error("商家id为空");
        }
        Merchant merchant = merchantMapper.selectById(id);
        if(merchant == null){
            return Result.error("商家不存在");
        }
        //商家名要放进 data：Result.success(String) 重载会把值塞进 msg，所以这里手工塞 data
        Result<String> result = Result.success();
        result.setData(merchant.getMerchantName());
        return result;
    }
}
