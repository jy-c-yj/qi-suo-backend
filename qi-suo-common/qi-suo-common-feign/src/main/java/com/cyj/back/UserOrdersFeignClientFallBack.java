package com.cyj.back;


import com.cyj.common.PageResult;
import com.cyj.common.Result;
import com.cyj.dvo.MerchantOrdersDVO;
import com.cyj.dvo.UserOrdersDetailDVO;
import com.cyj.feign.user.UserOrdersFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserOrdersFeignClientFallBack implements UserOrdersFeignClient {

    @Override
    public PageResult<MerchantOrdersDVO> merchantList(Long merchantId, Integer pageNum, Integer pageSize) {
        log.error("远程调用 -> qi-suo-user  方法 -> merchantList 出错");
        PageResult<MerchantOrdersDVO> pageResult = new PageResult<>();
        pageResult.setRows(java.util.List.of());
        pageResult.setTotal(0L);
        return pageResult;
    }

    @Override
    public Result<UserOrdersDetailDVO> detail(Long orderId) {
        log.error("远程调用 -> qi-suo-user  方法 -> detail 出错");
        return Result.error("查询订单详情失败");
    }

    @Override
    public Result<String> setStatus(Long orderId) {
        log.error("远程调用 -> qi-suo-user  方法 -> setStatus 出错");
        return Result.error("订单状态修改失败");
    }
}
