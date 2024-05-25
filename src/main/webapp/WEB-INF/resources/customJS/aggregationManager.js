function loadConditionGrid(defConditionList){
	var uniqueGridId = "condition_grid";
	var selectAllCheckboxId = "selectAllConditionAttribute";
	var childCheckboxName = "conditionAttributeCheckbox";
					
	$("#"+uniqueGridId).jqGrid({
        datatype: "local",
        colNames:[
                  "id",
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='attributeHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
                  "conditionExpressionValue",
                  jsSpringMsg.coreExpressionLabel,
                  jsSpringMsg.conditionAction ,
		],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
					return conditionAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
		           }},
		          {name:'conditionExpressionValue',index:'conditionExpressionValue',hidden:true,sortable:false},
		          {name:'conditionExpression',index:'conditionExpression',sortable:false},
				  {name:'conditionAction',index:'conditionAction',sortable:false},
        ],			        
        rowNum:jsSpringMsg.totalGridRows,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
        pager: "#condition_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        onSelectRow: function(rowid) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        data :defConditionList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
		pgtext : jsSpringMsg.pagerText,
		
		
	}).navGrid("#condition_gridPagingDiv",{edit:false,add:false,del:false,search:false});	
	$(".ui-jqgrid-titlebar").hide();
}


function conditionAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "conditionAttributeCheckbox";
	return '<input type="checkbox" id="'+uniqueId+rowObject["conditionExpressionValue"]+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

function aggttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "aggAttributeCheckbox";
	return '<input type="checkbox" id="'+uniqueId+rowObject["outputfieldname"]+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

function loadKeyAttributeGrid(defKeyAttributeList){
	var uniqueGridId = "keyattribute_grid";
	var selectAllCheckboxId = "selectAllKeyAttribute";
	var childCheckboxName = "keyAttributeCheckbox";
				
	
	$("#"+uniqueGridId).jqGrid({
        datatype: "local",
        colNames:[
                  "id",
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='attributeHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
                  jsSpringMsg.keyAttributeAggfieldLabel
		],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '15%', formatter: function(cellvalue, options, rowObject) {
					return keyAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
		           }},
		          {name:'aggregationFieldName',index:'aggregationFieldName',sortable:false}
        ],			        
        rowNum:jsSpringMsg.totalGridRows,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
        pager: "#keyattribute_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        onSelectRow: function(rowid) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        data :defKeyAttributeList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
		pgtext : jsSpringMsg.pagerText,
		
		
	}).navGrid("#keyattribute_gridPagingDiv",{edit:false,add:false,del:false,search:false});
	
	$("#keyattribute_gridPagingDiv_left").css("width", "");
	$(".ui-jqgrid-titlebar").hide();
}


function keyAttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, rootCheckboxId, childCheckboxName) {
	var uniqueId = "keyAttributeCheckbox";
	return '<input type="checkbox" id="'+uniqueId+rowObject["aggregationFieldName"]+'" name="'+childCheckboxName+'" value="'+rowObject["id"]+'" onchange="updateRootCheckbox(this, \''+uniqueGridId+'\', \''+rootCheckboxId+'\', \''+childCheckboxName+'\')"/>';
}

function loadAggAttributeGrid(defAggAttributeList){
	var uniqueGridId = "aggattribute_grid";
	var selectAllCheckboxId = "selectAllAggAttribute";
	var childCheckboxName = "aggAttributeCheckbox";
	
	$("#"+uniqueGridId).jqGrid({
        datatype: "local",
        colNames:[
                  "id",
                  "<input type='checkbox' id='"+selectAllCheckboxId+"' onclick='attributeHeaderCheckbox(event, this, \""+childCheckboxName+"\")'></input>",
                  jsSpringMsg.aggAttributeOutputFieldNameLabel,
                  "operationexpressionValue",
                  jsSpringMsg.aggAttributeOpeationExpressionLabel,
                  jsSpringMsg.aggAttributeOutputFieldDataTypeLabel,
		],
		colModel:[
		          {name:'id',index:'id',hidden:true,sortable:false},
		          {name:'checkbox',index:'checkbox',sortable:false,align:'center', width : '30%', formatter: function(cellvalue, options, rowObject) {
					return aggttributeCheckboxFormatter(cellvalue, options, rowObject, uniqueGridId, selectAllCheckboxId, childCheckboxName);	
		           }},
		          {name:'outputfieldname',index:'outputfieldname',sortable:false},
				  {name:'operationexpression',index:'operationexpression',hidden:true,sortable:false},
				  {name:'operationexpressionText',index:'operationexpressionText',sortable:false},
				  {name:'aggoutputfielddatatypefield',index:'aggoutputfielddatatypefield',sortable:false},
        ],			        
        rowNum:jsSpringMsg.totalGridRows,
        rowList:[10,20,60,100],
        height: 'auto',
        mtype:'POST',
        pager: "#aggattribute_gridPagingDiv",
        contentType: "application/json; charset=utf-8",
        viewrecords: true,
        //multiselect: true,
        timeout : 120000,
        loadtext: jsSpringMsg.loadingText,
        caption: "",
        beforeRequest:function(){
            $(".ui-dialog-titlebar").hide();
        }, 
        onSelectRow: function(rowid) {
            $('#'+rowid).removeClass('ui-state-highlight');
        },
        data :defAggAttributeList,
		onPaging: function () {
		},
		loadError : function(xhr,st,err) {
			handleGenericError(xhr,st,err);
		},
		onSelectAll:function(){
		},
		recordtext: jsSpringMsg.totalRecordText ,
        emptyrecords: jsSpringMsg.emptyRecordText,
		pgtext : jsSpringMsg.pagerText,
		
		
	}).navGrid("#aggattribute_gridPagingDiv",{edit:false,add:false,del:false,search:false});
	$(".ui-jqgrid-titlebar").hide(); 
}

/*
 * this function will handle root checkbox from child checkbox
 */
function updateRootCheckbox(element, gridId, rootCheckboxId, childCheckboxName) {
	if(!element.checked){
		// if current child checkbox is not checked : uncheck root checkbox
		$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',false);
	} else {
		//if current child checkboox is checked and all checkbox are checked then check root checkbox
		var count = jQuery("#"+gridId).jqGrid('getGridParam', 'records');
		if ($('input:checkbox[name="'+childCheckboxName+'"]:checked').length === count) {
			$('input:checkbox[id="'+rootCheckboxId+'"]').prop('checked',true);
	    }
	}
}

function addPopupListValues(target,optionId,optionValue,optionText){
	$('#'+ target).append($('<option>', {
		id: optionId,
		value: optionValue,
		text: optionText
	}));
}

/*
 * this function will handle select all/ de select all event
 */
function attributeHeaderCheckbox(e, element, childCheckboxName) {
    e = e || event;/* get IE event ( not passed ) */
    e.stopPropagation ? e.stopPropagation() : e.cancelBubble = false;

    //if root checkbox is checked :-> all child checkbox should ne checked
    if(element.checked) {
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',true);
    } else {
    	//if root checkbox is unchecked :-> all child checkbox should be unchecked
    	$('input:checkbox[name="'+childCheckboxName+'"]').prop('checked',false);
    }
}