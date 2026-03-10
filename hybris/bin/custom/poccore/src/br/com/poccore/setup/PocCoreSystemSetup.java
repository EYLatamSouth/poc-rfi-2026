/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package br.com.poccore.setup;

import static br.com.poccore.constants.PoccoreConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.commerceservices.setup.AbstractSystemSetup;
import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;
import java.util.List;

import br.com.poccore.constants.PoccoreConstants;
import br.com.poccore.service.PoccoreService;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.initialization.SystemSetupParameter;

@SystemSetup(extension = PoccoreConstants.EXTENSIONNAME)
public class PocCoreSystemSetup extends AbstractSystemSetup {
    private final PoccoreService poccoreService;

    public PocCoreSystemSetup(final PoccoreService poccoreService) {
        this.poccoreService = poccoreService;
    }

    @SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.PROJECT)
    public void createEssentialData(final SystemSetupContext context) {
        importImpexFile(context, "/poccore/import/customer-inquiry-config.impex");
        importImpexFile(context, "/poccore/import/customer-inquiry-data.impex");
        importImpexFile(context, "/poccore/import/oauth-client.impex");
        importImpexFile(context, "/poccore/import/solr.impex");
        importImpexFile(context, "/poccore/import/feature-flags.impex");
        importImpexFile(context, "/poccore/import/user-group.impex");

        poccoreService.createLogo(PLATFORM_LOGO_CODE);
    }

    private InputStream getImageStream() {
        return PocCoreSystemSetup.class.getResourceAsStream("/poccore/sap-hybris-platform.png");
    }

    @Override
    public List<SystemSetupParameter> getInitializationOptions() {
        return List.of();
    }
}
