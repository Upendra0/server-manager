var dynamicActionData;

function getAllData(tagName,response,typeOfData) {
	$.ajax({
		url : 'getUnifiedFieldList',
		type : 'GET',
		cache : false,
		async : false,
		data : {
			"tagName" : tagName,
			"typeOfData" : typeOfData
		},
		success : function(data) {
			response($.ui.autocomplete.filter(data,extractLast(tagName)));
			//dynamicActionData = data;
		},
		error : function(xhr, st, err) {
			alert("Error");
		}
	});
}
function split(val) {
	return val.split(/,\s*/);
}
function extractLast(term) {
	return split(term).pop();
}
function triggerAutoSuggest(id,url){
	$(id)	// don't navigate away from the field on tab when selecting an item
	.on("keydown",function(event) {
				if (event.keyCode === $.ui.keyCode.TAB
						&& $(this).autocomplete("instance").menu.active) {
					event.preventDefault();
				}
	}).autocomplete({
				minLength : 0,
				source : function(request, response) {
					getAllData(request.term,response,url);
					//response($.ui.autocomplete.filter(dynamicActionData,extractLast(request.term)));
				},
				focus : function() {
					// prevent value inserted on focus
					return false;
				},
				select : function(event, ui) {
					var terms = split(this.value);
					terms.pop();
					terms.push(ui.item.value);
					// add placeholder to get the comma-and-space at the end
					terms.push("");
					this.value = terms.join(",");
					return false;
				}
			});	
}
function loadUnfiedAutoComplete(tagname,url) {
	  $( "#"+tagname ).autocomplete({
	    source: function(request, response) {
	    	getAllData(request.term,response,url);
	//response(dynamicActionData);
	 	},
	    appendTo : $( "#"+tagname ).parent(),
	  });
}