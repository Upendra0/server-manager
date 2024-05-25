/**
 * 
 */
package com.elitecore.sm.common.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.exception.SMException;

/**
 * @author Sunil Gulabani
 * Jun 27, 2015
 */
@Aspect
public class AspectServiceLogging extends BaseAspect{
	/**
	 * It will log each method calls of *ServiceImpl
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("(!execution(*  com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchServiceImpl.reprocessFileDetailRecord(..)) &&  !execution(*  com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchServiceImpl.getFileRecordDetails(..)) && !execution(*  com.elitecore.sm.errorreprocess.service.ErrorReprocessBatchServiceImpl.downloadFile(..)) ) && execution(* com.elitecore.sm.*.service.*.*(..)))")
	//@Around("execution(public * com.elitecore.sm.*.service.*.*(..))")
	//@Around("execution(* com.elitecore.sm.core.dao.GenericDAOImpl.*(..))")
    public Object aroundService(ProceedingJoinPoint proceedingJoinPoint) throws SMException{
        logger.info("Before invoking: " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "#"  + proceedingJoinPoint.getSignature().getName()+ "() method.");
        Object value;
        try {
        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        	printRequestParameters(request);
        }  catch(IllegalStateException e){
        	logger.error("Exception Occured:"+e);
        	// this is when at app startup, this aspect comes into picture but no HTTPServlet request is found so reports error.
        } catch (Exception e) {
        	logger.error(e.getMessage(), e);
        }
        
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
        logger.info("After invoking " + proceedingJoinPoint.getSignature().getDeclaringTypeName() + "#" + proceedingJoinPoint.getSignature().getName() + "() method. Return value="+value);
        return value;
    }
}
