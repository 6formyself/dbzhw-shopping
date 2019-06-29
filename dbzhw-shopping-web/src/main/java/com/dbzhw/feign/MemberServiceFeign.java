package com.dbzhw.feign;

import com.dbzhw.api.service.MemberService;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

@Component
@FeignClient(value = "member")
public interface MemberServiceFeign extends MemberService {
}
