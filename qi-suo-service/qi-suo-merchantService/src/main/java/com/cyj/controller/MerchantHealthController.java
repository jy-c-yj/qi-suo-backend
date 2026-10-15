package com.cyj.controller;



import com.cyj.Annotation.Log;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantHealthController {
    @RequestMapping("/health")
//    @PreAuthorize("hasAnyRole('user')")
    public String health() {
        return "merchantService is ok";
    }
}
