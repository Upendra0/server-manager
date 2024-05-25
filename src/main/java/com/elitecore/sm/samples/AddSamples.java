/**
 * 
 */
package com.elitecore.sm.samples;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;

import com.elitecore.sm.common.util.ConfigInitializer;
import com.elitecore.sm.config.service.ConfigService;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.iam.service.AccessGroupService;
import com.elitecore.sm.iam.service.MenuService;
import com.elitecore.sm.parser.service.ParserService;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.systemparam.service.SystemParameterService;

/**
 * @author Sunil Gulabani
 * Apr 17, 2015
 */
public class AddSamples {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public AddSamples(
						String hbm2ddl_auto, 
						ConfigService configService,
						MenuService menuService,
						AccessGroupService accessGroupService,
						SystemParameterService systemParameterService,
						ServerService serverService,
						ServicesService servicesService,
						ServerInstanceService serverInstanceService,
						DriversService driversService,
						ParserService parserService,
						ConfigInitializer configController,
						ServletContext servletContext
					) {
		logger.info("hbm2ddl_auto: " + hbm2ddl_auto);
		SystemParametersSamples systemParametersSample = new SystemParametersSamples();
		if(hbm2ddl_auto!=null && hbm2ddl_auto.equals("create")){
			systemParametersSample.addSystemParameters(systemParameterService);
		}
		
		systemParametersSample.addSampleImage(systemParameterService,servletContext);
	}
}