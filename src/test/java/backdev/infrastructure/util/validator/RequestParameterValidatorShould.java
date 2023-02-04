package backdev.infrastructure.util.validator;

import backdev.infrastructure.util.validator.exception.MandatoryParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestParameterValidatorShould {

    private RequestParameterValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new RequestParameterValidator();
    }

    @Test
    public void
    validate_mandatory_string() {
        String valid = "something";
        String emptyOne = " ";
        String emptyTwo = "";

        Assertions
            .assertThat(validator.mandatoryString(valid, "someParameter"))
            .isEqualTo("something");

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryString(emptyOne, "someParameter"))
            .isInstanceOf(MandatoryParameterException.class)
            .hasMessage("Parameter [someParameter] is mandatory");

        Assertions
            .assertThatThrownBy(() -> validator.mandatoryString(emptyTwo, "someParameter"))
            .isInstanceOf(MandatoryParameterException.class)
            .hasMessage("Parameter [someParameter] is mandatory");
    }
}