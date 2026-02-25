package com.br.pococc.occ.controllers;

import br.com.poc.occ.dto.user.product.PocProductQuestionWsDTO;
import br.com.pocfacades.customerinquiry.PocCustomerInquiryFacade;
import com.br.pococc.occ.validators.PocProductQuestionValidator;
import de.hybris.bootstrap.annotations.UnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class PocUsersControllerTest {

    @InjectMocks
    private PocUsersController pocUsersController;

    @Mock
    private PocProductQuestionValidator pocProductQuestionValidator;

    @Mock
    private PocCustomerInquiryFacade pocCustomerInquiryFacade;

    @Before
    public void setUp() {}

    @Test
    public void testSendProductQuestion() {
        PocProductQuestionWsDTO dto =  new PocProductQuestionWsDTO();
        String productCode = "productCode";

        ResponseEntity<?> response = pocUsersController.sendProductQuestion(productCode, dto);

        verify(pocProductQuestionValidator).validate(eq(dto), any());
        verify(pocCustomerInquiryFacade).createCustomerInquiry(productCode, dto);
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
    }

}
