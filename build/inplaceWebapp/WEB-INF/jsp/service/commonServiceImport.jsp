<!-- MED-5089 -->
<script type="text/javascript">  
    document.onreadystatechange = function(){
        if(document.readyState === 'complete'){
        	  var message = '<%=session.getAttribute("responseMsg")%>';
			if (message != "null") {
				showSuccessMsg(message);
				<%session.removeAttribute("responseMsg");%>
			}
		}
	}
</script>