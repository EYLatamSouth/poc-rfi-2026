package com.br.pococc.occ.populators;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    private final PK pk = PK.fromLong(100L);
    private final String comment = "comment";
    private final Date creationTime = new Date();
    private final String headline = "head line";
    private final Double rating = 5.0;
    private final String alias = "alias";
    private final UserModel user = new UserModel();
    private final Integer helpfulnessRating= 10;

    @Before
    public void setUp() {
        populator.setPrincipalConverter(principalConverter);

        when(source.getPk()).thenReturn(pk);
        when(source.getComment()).thenReturn(comment);
        when(source.getCreationtime()).thenReturn(creationTime);
        when(source.getHeadline()).thenReturn(headline);
        when(source.getRating()).thenReturn(rating);
        when(source.getAlias()).thenReturn(alias);
        when(source.getUser()).thenReturn(user);
        when(source.getHelpfulnessRating()).thenReturn(helpfulnessRating);

        when(principalConverter.convert(user)).thenReturn(new PrincipalData());
    }
    
    @Test
    public void testPopulate_Success() {
        populator.populate(source, target);
        assertEquals(pk.getLongValueAsString(), target.getId());
        assertEquals(comment, target.getComment());
        assertEquals(creationTime, target.getDate());
        assertEquals(headline, target.getHeadline());
        assertEquals(rating, target.getRating());
        assertEquals(alias, target.getAlias());
        assertEquals(helpfulnessRating, target.getHelpfulnessRating());
        assertNotNull(target.getPrincipal());

    }

    @Test(expected = IllegalArgumentException.class)
    public void testPopulate_SourceNull() {
        populator.populate(null, target);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPopulate_TargetNull() {
        populator.populate(source, null);
    }
}