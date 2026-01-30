package com.cyj.service;

import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dto.AdminDTO;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;

public interface AdminManagementService {
    /**
     * 查询admin列表
     * @return
     */
    Result<PageResult<AdminDVO>> getAdminList(int pageNum, Long pageSize);

    /**
     *新增admin
     * @param newAdminDTO
     * @return
     */
    Result<AdminDVO> newAdmin(NewAdminDTO newAdminDTO);

    /**
     * 设为启用
     * @param adminId
     * @return
     */
    Result<Boolean> setAdminIsEnabled(Long adminId,Integer pageNum);

    /**
     * 设为禁用
     * @param adminId
     * @return
     */
    Result<Boolean> setAdminNotEnabled(Long adminId,Integer pageNum);

    /**
     * 编辑admin
     * @param adminDTO
     * @return
     */
    Result<Boolean> updateAdmin(AdminDTO adminDTO,Integer pageNum);

    /**
     * 删除admin
     * @param adminId
     * @return
     */
    Result<Boolean> deleteAdmin(Long adminId);
}
