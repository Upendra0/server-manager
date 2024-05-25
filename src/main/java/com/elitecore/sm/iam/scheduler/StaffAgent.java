/**
 * 
 */
package com.elitecore.sm.iam.scheduler;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.elitecore.sm.iam.service.StaffService;
/**
 * @author Sunil Gulabani
 * Apr 16, 2015
 */
public class StaffAgent {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired(required=true)
	@Qualifier(value="staffService")
	private StaffService staffService;
	
	/**
	 * Releases lock agent.
	 */
    public void releaseLock() {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>> entered..." + new Date());
		int rowsUpdated = this.staffService.releaseStaffLockAgent();
		logger.info("rowsUpdated: " + rowsUpdated);
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>> exiting...");
    }
}