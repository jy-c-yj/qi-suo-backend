package com.cyj.filter;

import com.cyj.common.TraceConstants;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 链路 id 过滤器：全链路 traceId 的起点
 * <p>
 * 必须排在 {@link JwtFilter} 之前（用 @Order 提到最高优先级）。原因：JwtFilter 在 token 为空
 * 或命中白名单时直接放行，不会 mutate request，如果 traceId 写在它之后，
 * 登录、验证码这类白名单接口就拿不到 traceId——而这些恰恰是最需要审计的请求。
 * <p>
 * 网关是 WebFlux（Reactor），不能用 MDC：一个请求会在多个线程间跳，ThreadLocal 会串数据。
 * 所以这里只负责写请求头，由下游 Servlet 服务读取后放进 MDC。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // 上游已带则复用，保证跨系统调用能串成同一条链路；没有才新生成
        String traceId = exchange.getRequest().getHeaders().getFirst(TraceConstants.TRACE_ID_HEADER);
        if (traceId == null || traceId.isBlank()) {
            traceId = TraceConstants.generate();
        }

        ServerHttpRequest request = exchange.getRequest().mutate()
                .header(TraceConstants.TRACE_ID_HEADER, traceId)
                .build();


        exchange.getResponse().getHeaders().set(TraceConstants.TRACE_ID_HEADER, traceId);

        return chain.filter(exchange.mutate().request(request).build());
    }
}
