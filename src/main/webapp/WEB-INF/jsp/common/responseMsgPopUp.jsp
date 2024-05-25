<!-- jQuery 2.1.4 --> 
<!-- <script src="js/jQuery-2.1.4.min.js"></script>  -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

	<div id ="responseMsgDivpopUp" class="responseMsgPopUp" style="display: none;"></div>
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
				<div style="width=calc();" class="errorMessage" id="divErrorMsgPopUp">
					${ERROR_MSG}
				</div>
			</c:if>
			<c:if test="${not empty RESPONSE_MSG}">
				<div style="width=calc();" class="successMessage" id="divResponseMsgPopUp">
					${RESPONSE_MSG}
				</div>				
           	</c:if>
	
	<script type="text/javascript">
		function showErrorMsgPopUp(errorMsg){
			$(".responseMsgPopUp").html(
					'<div style="width: calc();" class="errorMessage" id="divButtonPopUp">'  +
						'<span class="title">' + errorMsg + '</span>' +
					'</div>'	
			);
			$(".responseMsgPopUp").show();
		}

		function showSuccessMsgPopUp(msg){
			$(".responseMsgPopUp").html(
					'<div style="width:calc();" class="successMessage" id="divButtonPopUp">'  +
						'<span id="span_success_txt" class="title">' + msg + '</span>' +
					'</div>'	
			);
			$(".responseMsgPopUp").show();
		}
		
		function clearAllMessagesPopUp(){
			clearResponseMsgDivPopUp();
			clearResponseMsgPopUp();
			clearErrorMsgpopUp();
		}

		function clearResponseMsgDivPopUp(){
			$('#responseMsgDivpopUp').html('');
			$('.responseMsgPopUp').html('');
			$('.successMessage').html('');
			$('.errorMessage').html('');
			$(".responseMsgPopUp").hide();
			
		}
		function clearResponseMsgPopUp(){
			$('#divResponseMsgPopUp').html('');
			$('.responseMsgPopUp').html('');
			$('.successMessage').html('');
			$('.errorMessage').html('');
			$(".responseMsgPopUp").hide();
			
		}
		function clearErrorMsgpopUp(){
			$('#divErrorMsgPopUp').html('');
			$('.responseMsgPopUp').html('');
			$('.successMessage').html('');
			$('.errorMessage').html('');
			$(".responseMsgPopUp").hide();
			
		}
		function clearErrorMsg2(){
			$('#divErrorMsgPopUp').html('');
			$('.responseMsgPopUp').html('');
			$('.errorMessage').html('');
			$(".responseMsgPopUp").hide();
			
		}
	</script>