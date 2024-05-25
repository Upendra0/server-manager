/**
 * 
 */
package com.elitecore.sm.common.aspect;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;

/**
 * @author Sunil Gulabani
 * Aug 4, 2015
 */
@Aspect
public class AspectControllerLogging extends BaseAspect{
	/**
	 * It will log each method calls of *Controller
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around(" !execution(* com.elitecore.sm.errorreprocess.controller.ErrorReprocessBatchController.reprocessProcessingFileRecords(..)) && !execution(* com.elitecore.sm.errorreprocess.controller.ErrorReprocessBatchController.viewFileDetails(..)) && !execution(* com.elitecore.sm.errorreprocess.controller.ErrorReprocessBatchController.getFileRecordDetails(..)) && !execution(* com.elitecore.sm.errorreprocess.controller.ErrorReprocessBatchController.downloadProcessingFile(..)) && (execution(* com.elitecore.sm.*.controller.*.*(..)) || execution(* com.elitecore.sm.*.controller.*.*.*(..))) ")
    public Object aroundController(ProceedingJoinPoint proceedingJoinPoint) throws SMException {
        logger.info("Before invoking: " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "#"  + proceedingJoinPoint.getSignature().getName()+ "() method.");
        Object value;
        long startTime = System.nanoTime();
        HttpServletRequest request = null;
        try {
        	request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        	
        	if(!proceedingJoinPoint.getSignature().getName().equals(BaseConstants.INIT_BINDER_METHOD_NAME))
        		printRequestParameters(request);
        }	catch(IllegalStateException e){
        	logger.error("Exception Occured: "+e);
            	// this is when at app startup, this aspect comes into picture but no HTTPServlet request is found so reports error.
             
        }  catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
        if(!proceedingJoinPoint.getSignature().getName().equals(BaseConstants.INIT_BINDER_METHOD_NAME))
        	printMethodArgs(proceedingJoinPoint);

        try {
        	value = proceedingJoinPoint.proceed();
        } catch (Throwable e) {
        	logger.error(e.getMessage(), e);
        	if(e instanceof MigrationSMException) {
        		throw (MigrationSMException) e;
        	}
        	throw new SMException(e.getMessage());
        }
        
        try {
			if(request!=null){
				long endTime = System.nanoTime();
				request.setAttribute("EXECUTION_TIME", TimeUnit.MILLISECONDS.toSeconds((endTime - startTime)/1000000));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        logger.info("After invoking " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "#" + proceedingJoinPoint.getSignature().getName() + "() method. Return value="+value);
        return value;
    }
}
