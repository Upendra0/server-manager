function isNumber(evt) {
	var charCode = (evt.charCode) ? evt.charCode : ((evt.which) ? evt.which : evt.keyCode);
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function isAlphanumeric(evt) {
	var charCode = (evt.charCode) ? evt.charCode : ((evt.which) ? evt.which : evt.keyCode);
    if (!(charCode > 47 && charCode < 58) && // numeric (0-9)
    	!(charCode > 64 && charCode < 91) && // upper alpha (A-Z)
    	!(charCode > 96 && charCode < 123)) {
        return false;
    }
    return true;
}

function isAlpha(evt) {
	var charCode = (evt.charCode) ? evt.charCode : ((evt.which) ? evt.which : evt.keyCode);
    if (!(charCode > 64 && charCode < 91) && // upper alpha (A-Z)
    	!(charCode > 96 && charCode < 123)) {
        return false;
    }
    return true;
}

function isTemplateValue(evt){
	var charCode = (evt.charCode) ? evt.charCode : ((evt.which) ? evt.which : evt.keyCode);
	if(isNumber(evt) || charCode == 44){
		return true;
	}else{
		return false;
	}
}