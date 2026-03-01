package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminMerchantListSelectConditionDTO;
import com.cyj.dvo.AdminMerchantManagementLDVO;

/**
 * 管理员商家管理服务接口
 * 该接口定义了管理员对商家进行管理的相关服务方法
 */
public interface AdminMerchantManagementService {

/**
 * 查询管理员商户列表的方法
 *
 * @param adminMerchantListSelectConditionDTO 管理员商户列表查询条件数据传输对象
 * @return 返回一个包含分页结果的响应对象，结果中包含管理员商户列表的数据视图对象
 */
    Result<PageResult<AdminMerchantManagementLDVO>> list(AdminMerchantListSelectConditionDTO adminMerchantListSelectConditionDTO);

/**
 * 设置状态的方法
 * @param id 要设置状态的ID
 * @return 返回一个Result对象，其中包含String类型的结果信息
 */
    Result<String> setStatus(Long id,Integer pageNum);

}
