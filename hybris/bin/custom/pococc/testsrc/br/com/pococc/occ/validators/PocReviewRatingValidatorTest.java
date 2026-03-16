package br.com.pococc.occ.validators;

import br.com.poc.occ.dto.product.PocReviewRatingData;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PocReviewRatingValidatorTest {

    @InjectMocks
    private PocCustomerReviewRatingValidator validator;

    @Test
    public void testValidate_Success() {
        PocReviewRatingData validateData = mock(PocReviewRatingData.class);
        when(validateData.getId()).thenReturn("4");
        when(validateData.getProductCode()).thenReturn("productCode");
        Errors errors = new BeanPropertyBindingResult(validateData, "validDto");

        validator.validate(validateData, errors );

        assertFalse(errors.hasErrors());
    }

    @Test
    public void testValidate_Fail() {
        PocReviewRatingData validateData = mock(PocReviewRatingData.class);
        when(validateData.getId()).thenReturn(null);
        when(validateData.getProductCode()).thenReturn("");
        Errors errors = new BeanPropertyBindingResult(validateData, "validDto");

        validator.validate(validateData, errors );
        assertTrue(errors.hasErrors());

        validator.validate(new Object(), errors );
        assertTrue(errors.hasErrors());
    }

    @Test
    public void testSupports() {
        assertFalse(validator.supports(String.class));
        assertTrue(validator.supports(PocReviewRatingData.class));
    }
}