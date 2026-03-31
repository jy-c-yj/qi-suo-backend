package com.cyj.service.Impl;



import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;
import com.cyj.entity.Merchant;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.AdminMerchantManagementService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 管理员商户管理服务实现类
 * 该类实现了AdminMerchantManagementService接口，用于提供管理员对商户管理的具体业务逻辑实现
 */
@Service
@Slf4j
public class AdminMerchantManagementServiceImpl implements AdminMerchantManagementService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private RedissonUtil redissonUtil;
/**
 * 查询商户列表的方法
 * @param adminMerchantListSelectConditionDTO 商户列表查询条件数据传输对象
 * @return Result<PageResult<AdminMerchantManagementLDVO>> 返回结果对象

 */
    @Override
    public Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO) {
        //查询缓存
        String key = RedissonKey.ADMIN_MANAGEMENT_MERCHANT_KEY+ adminMerchantListSelectConditionDTO.getPageNum();
        //存在返回
//        List<AdminMerchantManagementLDVO> list = redissonUtil.getList(key);
        PageResult<AdminMerchantManagementLDVO> pageResult = new PageResult<>();
//        if (!list.isEmpty()) {
//            pageResult.setTotal((long) list.size());
//            pageResult.setRows(list);
//            return Result.success(pageResult);
//        }
        //不存在查询数据库
        Page<Merchant> page = new Page<>(adminMerchantListSelectConditionDTO.getPageNum(), adminMerchantListSelectConditionDTO.getPageSize());
        List<AdminMerchantManagementLDVO> merchantList = merchantMapper.list(page, adminMerchantListSelectConditionDTO);
        if(merchantList.isEmpty()){
            return Result.error("商户列表为空");
        }
        pageResult.setTotal(page.getTotal());
        pageResult.setRows(merchantList);

        //存入缓存
//        list.addAll(merchantList);
//        redissonUtil.expire(key, 60, TimeUnit.MINUTES);

        return Result.success(pageResult);
    }

/**
 * 设置状态的方法
 * 该方法用于根据传入的ID来设置状态，并返回一个包含字符串类型结果的对象
 *
 * @param id 用于标识需要设置状态的唯一标识符
 * @return 返回一个Result<String>类型的结果对象，目前实现为返回null，实际应用中应返回具体的操作结果
 */
    @Override
    public Result<String> setStatus(Long id,Integer pageNum) {
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
        //删除缓存
//        List<AdminMerchantManagementLDVO> list = redissonUtil.getList(RedissonKey.ADMIN_MANAGEMENT_MERCHANT_KEY+pageNum);
//        for (int i = 0; i < list.size(); i++) {
//            AdminMerchantManagementLDVO adminMerchantManagementLDVO = list.get(i);
//            if (adminMerchantManagementLDVO.getMerchantId().equals(id)) {
//                adminMerchantManagementLDVO.setStatus(merchant.getStatus());
//                list.set(i, adminMerchantManagementLDVO);
//                break;
//            }
//        }
        return Result.success("设置成功");
    }
}
