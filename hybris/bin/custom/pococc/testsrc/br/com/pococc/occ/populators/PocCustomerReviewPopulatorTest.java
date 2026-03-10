package br.com.pococc.occ.populators;

import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PocCustomerReviewPopulatorTest {

    @InjectMocks
    private PocCustomerReviewPopulator populator;
    @Mock
    private Converter<PrincipalModel, PrincipalData> principalConverter;
    @Mock
    private CustomerReviewModel source;

    private ReviewData target = new ReviewData();
    private final Integer helpfulnessRating= 10;

    @Before
    public void setUp() {
        when(source.getHelpfulnessRating()).thenReturn(helpfulnessRating);
    }

    @Test
    public void testPopulate_Success() {
        populator.populate(source, target);
        assertEquals(helpfulnessRating, target.getHelpfulnessRating());
    }
}