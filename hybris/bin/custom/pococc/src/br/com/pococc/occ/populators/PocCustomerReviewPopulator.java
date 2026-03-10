package br.com.pococc.occ.populators;

import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

public class PocCustomerReviewPopulator implements Populator<CustomerReviewModel, ReviewData> {

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
        target.setHelpfulnessRating(source.getHelpfulnessRating());
    }
}