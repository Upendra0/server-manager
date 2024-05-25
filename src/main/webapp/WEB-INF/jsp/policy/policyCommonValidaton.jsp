<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<a href="#validationMsgPopupDiv" class="fancybox" style="display: none;" id="validation_msg_popup_lnk">#</a>
	<div id="validationMsgPopupDiv" style=" width:100%; display: none;" >
	      <div class="modal-content">
	        <div class="modal-header padding10">
	           <h4 class="modal-title">
					<span><spring:message code="btn.label.warning" ></spring:message></span> 
				</h4>
	        </div>
	        <div class="modal-body padding10 inline-form">
	           <div id="validationMsg"> </div>
	        </div>
	        <div class="modal-footer padding10">
	            <button type="button" class="btn btn-grey btn-xs " id="validation_close_msg" data-dismiss="modal" onclick="closeFancyBox();"><spring:message code="btn.label.close" ></spring:message></button>
	        </div>
	    </div>  
	</div> 
