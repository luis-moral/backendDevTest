package backdev.infrastructurre.util.validator;

import backdev.infrastructurre.util.validator.exception.MandatoryParameterException;

public class RequestParameterValidator {

    public String mandatoryString(String value, String parameterName) {
        value = value.trim();

        if (value.isEmpty()) {
            throw new MandatoryParameterException(parameterName);
        }

        return value;
    }
}
