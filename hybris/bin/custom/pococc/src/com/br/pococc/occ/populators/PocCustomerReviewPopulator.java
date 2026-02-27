package com.br.pococc.occ.populators;

import br.com.poccore.dao.PocCustomerReviewDao;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.util.Assert;

public class PocCustomerReviewPopulator implements Populator<CustomerReviewModel, ReviewData> {

    private Converter<PrincipalModel, PrincipalData> principalConverter;

    /**
     * Populates Customer Review information into Review Data.
     * Customization add populating of Helpfulness Rating attribute.
     *
     * @param source                Source of information.
     * @param target                Target of information.
     * @throws ConversionException  Standard exception.
     */
    @Override
    public void populate(CustomerReviewModel source, ReviewData target) throws ConversionException {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");

        target.setId(source.getPk().getLongValueAsString());
        target.setComment(source.getComment());
        target.setDate(source.getCreationtime());
        target.setHeadline(source.getHeadline());
        target.setRating(source.getRating());
        target.setAlias(source.getAlias());

        target.setPrincipal(getPrincipalConverter().convert(source.getUser()));

        target.setHelpfulnessRating(source.getHelpfulnessRating());
    }

    public Converter<PrincipalModel, PrincipalData> getPrincipalConverter() {
        return principalConverter;
    }

    public void setPrincipalConverter(Converter<PrincipalModel, PrincipalData> principalConverter) {
        this.principalConverter = principalConverter;
    }
}
