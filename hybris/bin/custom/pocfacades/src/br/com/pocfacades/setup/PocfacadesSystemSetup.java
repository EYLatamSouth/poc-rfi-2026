/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package br.com.pocfacades.setup;

import static br.com.pocfacades.constants.PocfacadesConstants.PLATFORM_LOGO_CODE;

import br.com.pocfacades.service.PocFacadesService;
import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import br.com.pocfacades.constants.PocfacadesConstants;


@SystemSetup(extension = PocfacadesConstants.EXTENSIONNAME)
public class PocfacadesSystemSetup
{
	private final PocFacadesService pocfacadesService;

	public PocfacadesSystemSetup(final PocFacadesService pocfacadesService)
	{
		this.pocfacadesService = pocfacadesService;
	}

	@SystemSetup(process = SystemSetup.Process.ALL, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		pocfacadesService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return PocfacadesSystemSetup.class.getResourceAsStream("/pocfacades/sap-hybris-platform.png");
	}
}
