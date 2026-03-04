package br.com.poccore.dao.impl;

import br.com.poccore.dao.FeatureFlagDao;
import br.com.poccore.model.FeatureFlagModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class DefaultFeatureFlagDao implements FeatureFlagDao {

    private static final String FIND_FEATURE_FLAG = "SELECT {ff.pk}" +
            " FROM {" + FeatureFlagModel._TYPECODE + " AS ff}" +
            " WHERE {ff.key} = ?key";

    private FlexibleSearchService flexibleSearchService;

    @Override
    public List<FeatureFlagModel> findFeatureFlagByKey(String name) {
        FlexibleSearchQuery fquery = new FlexibleSearchQuery(FIND_FEATURE_FLAG);
        fquery.addQueryParameter("key", name);

        SearchResult<FeatureFlagModel> result = getFlexibleSearchService().search(fquery);

        if (CollectionUtils.isEmpty(result.getResult())) {
            return null;
        }

        return result.getResult();
    }

    public FlexibleSearchService getFlexibleSearchService() {
        return flexibleSearchService;
    }

    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService) {
        this.flexibleSearchService = flexibleSearchService;
    }
}
