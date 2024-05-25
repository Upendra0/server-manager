<!-- jQuery 2.1.4 --> 
<!-- <script src="js/jQuery-2.1.4.min.js"></script>  -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div id="responseMsgDiv" class="divResponseMsg" style="display: none;">
	</div>
			<c:if test="${not empty error}">			
				<div class="form-group">
					<div class="input-group error">
						<span class="input-group-addon add-on last">
							<i class="glyphicon glyphicon-alert" title="" data-placement="left" data-toggle="tooltip" data-original-title="${error}"></i>
						</span>
					</div>
				</div>
			</c:if>

 			<c:if test="${not empty ERROR_MSG}">
				<div style="width=calc();" class="errorMessage" id="divErrorMsg">
					${ERROR_MSG}
				</div>
			</c:if>
			<c:if test="${not empty RESPONSE_MSG}">
				<div style="width=calc();" class="successMessage" id="divResponseMsg">
					${RESPONSE_MSG}
				</div>				
           	</c:if>
	
	<script type="text/javascript">
		function showErrorMsg(errorMsg){
			$("#responseMsgDiv").html(
					'<div style="width=calc();" class="errorMessage" id="divButton">'  +
						'<span id="span_error_txt" class="title">' + errorMsg + '</span>' +
					'</div>'	
			);
			
			$("#responseMsgDiv").show();
		}

		function showSuccessMsg(msg){
			$("#responseMsgDiv").html(
					'<div style="width=calc();" class="successMessage" id="divButton">'  +
						'<span id="span_success_txt" class="title">' + msg + '</span>' +
					'</div>'	
			);
			$("#responseMsgDiv").show();
		}
		
		function clearAllMessages(){
			clearResponseMsgDiv();
			clearResponseMsg();
			clearErrorMsg();
		}

		function clearResponseMsgDiv(){
			$('#responseMsgDiv').html('');
			$("#responseMsgDiv").hide();
			$('.divResponseMsg').html('');
		}
		function clearResponseMsg(){
			$('#divResponseMsg').html('');
			$("#divResponseMsg").hide();
			$('.divResponseMsg').html('');
		}
		function clearErrorMsg(){
			$('#divErrorMsg').html('');
			$("#divErrorMsg").hide();
			$('.divResponseMsg').html('');
		}
	</script>