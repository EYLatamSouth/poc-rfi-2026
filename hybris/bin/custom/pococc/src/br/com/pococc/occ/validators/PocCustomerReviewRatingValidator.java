package br.com.pococc.occ.validators;

import br.com.poc.occ.dto.product.PocReviewRatingData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PocCustomerReviewRatingValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";

    @Override
    public boolean supports(Class<?> clazz) {
        return PocReviewRatingData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof PocReviewRatingData data) {
            if (StringUtils.isBlank(data.getProductCode())) {
                errors.rejectValue("productCode", FIELD_REQUIRED);
            }
            if (StringUtils.isBlank(data.getId())) {
                errors.rejectValue("id", FIELD_REQUIRED);
            }
        } else {
            errors.reject("Target is not PocReviewRatingData");
        }
    }
}