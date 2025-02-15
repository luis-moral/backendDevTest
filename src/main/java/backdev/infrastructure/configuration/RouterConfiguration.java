package backdev.infrastructure.configuration;

import backdev.infrastructure.handler.product.similar.GetSimilarProductsHandler;
import backdev.infrastructure.handler.status.get.GetStatusHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Value("${endpoint.status.path}")
    private String statusEndpoint;

    @Value("${endpoint.product.path.similar}")
    private String similarProductsEndpoint;

    @Bean
    public RouterFunction<ServerResponse> routes(
        GetStatusHandler getStatusHandler,
        GetSimilarProductsHandler getSimilarProductsHandler
    ) {
        return
            RouterFunctions
                .route(RequestPredicates.GET(statusEndpoint), getStatusHandler::status)
                .andRoute(RequestPredicates.GET(similarProductsEndpoint), getSimilarProductsHandler::similar);
    }
}
