package com.br.pococc.occ.validators;

import br.com.poc.occ.dto.product.PocReviewRatingData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PocReviewRatingValidator implements Validator {

    private static final String FIELD_REQUIRED = "field.required";
    private static final String FIELD_LESS_THAN_ZERO = "field.lessThanZero";

    @Override
    public boolean supports(Class<?> clazz) {
        return PocReviewRatingData.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof PocReviewRatingData data) {
            if (StringUtils.isEmpty(data.getProductCode())) {
                errors.rejectValue("productCode", FIELD_REQUIRED);
            }
            if (data.getId() == null) {
                errors.rejectValue("id", FIELD_REQUIRED);
            } else if (data.getId() < 0) {
                errors.rejectValue("id", FIELD_LESS_THAN_ZERO);
            }
        } else {
            errors.reject("Target is not PocReviewRatingData");
        }
    }
}
