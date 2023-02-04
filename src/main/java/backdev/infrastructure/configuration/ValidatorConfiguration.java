package backdev.infrastructure.configuration;

import backdev.infrastructure.util.validator.RequestParameterValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public RequestParameterValidator requestParameterValidator() {
        return new RequestParameterValidator();
    }
}
