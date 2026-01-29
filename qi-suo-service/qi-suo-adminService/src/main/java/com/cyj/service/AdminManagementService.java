package com.cyj.service;

import com.cyj.common.Result;
import com.cyj.dto.NewAdminDTO;
import com.cyj.dvo.AdminDVO;

import java.util.List;

public interface AdminManagementService {
    /**
     * 查询admin列表
     * @return
     */
    Result<List<AdminDVO>> getAdminList();

    /**
     *新增admin
     * @param newAdminDTO
     * @return
     */
    Result<AdminDVO> newAdmin(NewAdminDTO newAdminDTO);
}
