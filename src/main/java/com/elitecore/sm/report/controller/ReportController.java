package com.elitecore.sm.report.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.EnumComposerAttributeHeader;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerAttribute;
import com.elitecore.sm.composer.service.ComposerAttributeService;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.EnumParserAttributeHeader;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.HtmlParserAttribute;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.VarLengthAsciiParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.XlsParserAttribute;
import com.elitecore.sm.parser.service.ParserAttributeService;
import com.elitecore.sm.parser.service.ParserGroupAttributeService;
import com.elitecore.sm.report.view.ReportView;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.EnumDbDriverAttributeHeader;        
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.parser.model.NatFlowParserAttribute;
/**
 * @author brijesh.soni
 */
@Controller
public class ReportController extends BaseController {
	
	 public static final String MODULE_NAME="DOWNLOAD_SAMPLE_ATTRIBUTE";
	 static HashMap<String,String> sampleheader=new HashMap<>();
	 static Map<String, List<String>> sampleDataMap=new HashMap<>();
	 static Map<String, List<String>> composerSampleDataMap=new HashMap<>();
	 static Map<String, List<String>> dbDriverSampleDataMap=new HashMap<>();
	 static String[] sourceField;
	 static String[] sourceFieldName;
	 static String[] unifiedField;
	 static String[] description;
	 static String[] defaultValue;
	 static String[] trimChar;
	 static String[] trimPosition;
	 static String[] sourceFieldFormat;
	 static String[] dateFormat;
	 static String[] ipPortSeperator;
	 static String[] portUnifiedField;
	 static String[] startLength;
	 static String[] endLength;
	 static String[] length;
	 static String[] paddinglength;
	 static String[] prefix;
	 static String[] postfix;
	 static String[] rightDelimiter;
	 static String[] asn1datatype;
	 static String[] childattributes;
	 static String[] isrecordinitializer;
	 static String[] sourcefielddataformat;
	 static String[] choiceidholderunifiedfield;
	 static String[] sourceFieldFormatbinary;
	 static String[] bitStartLength;
	 static String[] bitEndLength;
	 static String[] readAsBits;
	 static String[] seqNo;
	 static String[] destField;
	 static String[] dataType;
	 static String[] replaceCondList;
	 static String[] isPaddingEnable;
	 static String[] paddingType;
	 static String[] paddingChar;
	 static String[] cTrimPosition;
	 static String[] destinationFieldFormat;
	 static String[] argumentDataType;
	 static String[] fieldIdentifier;
	 static String[] fieldExtractionMethod;
	 static String[] fieldSectionId;
	 static String[] containsFieldAttribute;
	 static String[] location;
	 static String[] columnStartLocation;
	 static String[] columnIdentifier;
	 static String[] referenceRow;
	 static String[] columnEndsWith;
	 static String[] mandatory;
	 static String[] pageNumber;
	 static String[] columnStartsWith;
	 static String[] tdNo;
	 static String[] referenceCol;
	 static String[] tableFooter;
	 static String[] excelRow;
	 static String[] excelCol;
	 static String[] relativeExcelRow;
	 static String[] startsWith;
	 static String[] columnContains;
	 static String[] tableRowAttribute;
	 static String[] valueSeparator;
	 static String[] valueIndex;
	 static String[] multiLineAttribute;
	 static String[] multipleValues;
	 static String[] rowTextAlignment;
	 static String[] databaseFieldName;
	 static String[] parseAsJson;
	 static String[] groupName;
	 static String[] subGroupName;
	 static String[] multiRecord;
	 

 	 static { 
	     sourceField = new String[]{"AREA","URL","CODE"};
	     sourceFieldName = new String[]{"AREA","URL","CODE"};
		 unifiedField=new String[]{"General1","General2","General3"};
		 description=new String[]{"AREA","URL","CODE"};
		 defaultValue=new String[]{"AAA","UUU","CCCC"};
		 trimChar=new String[]{"**","@@","$$"};
		 trimPosition=new String[]{"BOTH","LEFT","RIGHT"};
		 sourceFieldFormat=new String[]{"STRING","INTEGER","DATE"};
		 sourceFieldFormatbinary=new String[]{"CHARS","CHARS","CHARS"};
		 dateFormat=new String[]{"yy-mm-dd","yy-mm-dd","yy-mm-dd"};
		 ipPortSeperator=new String[]{":","#","-"};
		 portUnifiedField=new String[]{"General11","General22","General33"};
		 startLength=new String[]{"5","6","7"};
		 endLength=new String[]{"5","6","7"};
		 readAsBits=new String[]{"False","False","False"};
		 bitStartLength=new String[]{"-1","-1","-1"};
		 bitEndLength=new String[]{"-1","-1","-1"};
		 length=new String[]{"1","1","1"};
		 paddinglength=new String[]{"1","1","1"};
		 prefix=new String[]{"pre","pre","pre"};
		 postfix=new String[]{"post","post","post"};
		 rightDelimiter =  new String[]{"1","2","3"};
		 multiRecord=new String[]{"False","False","False"};
		 asn1datatype=  new String[]{"1","2","3"};
		 childattributes = new String[]{"1","2","3"};
		 isrecordinitializer = new String[]{"FALSE","FALSE","FALSE"};
		 choiceidholderunifiedfield = new String[]{"General41","General42","General43"};
		 sourcefielddataformat = new String[]{"HexToDecimal","HexToDecimal","HexToDecimal"};
		 seqNo = new String[]{"1","2","3"}; 
		 destField = new String[]{"AREA","URL","CODE"};
		 dataType = new String[]{"STRING","DATE","INTEGER"};
		 replaceCondList = new String[]{"271:C","271:C,272:D","272:D"};
		 isPaddingEnable = new String[]{"false","true","false"};
		 paddingType=new String[]{"LEFT","LEFT","RIGHT"};
		 paddingChar = new String[]{"1","2","3"};
		 cTrimPosition=new String[]{"Both","Left","Right"};
		 destinationFieldFormat = new String[]{"TBCD","BCD","IPBINARY"};
		 argumentDataType = new String[]{"STRING","LONG","BOOLEAN"};
		 fieldIdentifier = new String[]{"Address:","Address:","Address:"};
		 fieldExtractionMethod = new String[] {"NEXT","NEXT","OWN"};
		 fieldSectionId = new String[]{"style2","style2","style2"};
		 containsFieldAttribute = new String[]{"td","td","td"};
		 location=new String[]{"220,45,350,280","220,45,350,280","220,45,350,280"};
		 columnStartLocation = new String[]{"45","46","47"};
		 columnIdentifier=new String[]{"T1","T2","T3"};
		 referenceRow = new String[]{"1,2","3,4","4,5"};
		 columnStartsWith = new String[]{"AREA","URL","CODE"};
		 columnEndsWith = new String[]{"AREA","URL","CODE"};
		 pageNumber = new String[]{"3","2","1"};
		 mandatory = new String[]{"Y","N","Y"};
		 tdNo =  new String[]{"1","2","3"};
		 referenceCol = new String[]{"1,2","3,4","4,5"};
		 tableFooter=new String[]{"FALSE","FALSE","FALSE"};
		 multiLineAttribute=new String[]{"FALSE","FALSE","FALSE"};
		 multipleValues=new String[]{"FALSE","FALSE","FALSE"};
		 rowTextAlignment=new String[]{"CENTER","LEFT","RIGHT"};
		 excelRow =  new String[]{"1","2","3"};
		 excelCol =  new String[]{"1","2","3"};
		 relativeExcelRow =  new String[]{"1","2","3"};
		 startsWith = new String[]{"STRING","LONG","BOOLEAN"};
		 columnContains = new String[]{"C1","C2","C3"};
		 tableRowAttribute=new String[]{"FALSE","FALSE","FALSE"};
		 valueSeparator=new String[]{"Total amount due","Advanced payment","VAT amount"};
		 valueIndex=new String[]{"1","2","3"};
		 databaseFieldName= new String[]{"General41","General42","General43"};
		 parseAsJson=new String[]{"FALSE","FALSE","FALSE"};
		 groupName=new String[]{"","",""};
		 subGroupName=new String[]{"","",""};
		 sampleDataMap=initializeSampleData(sampleDataMap);
		 initializeComposerSampleData();
		 initializeDbDriverSampleData();
     } 
	 
	@Autowired
	private ParserAttributeService parserAttributeService;
	
	@Autowired
	private ParserGroupAttributeService parserGroupAttributeService;


	@Autowired
	private ComposerAttributeService composerAttributeService;
	
	@Autowired
	private DriversService driversService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_EXCEL_PARSER_COMPOSER_ATTRIBUTE_LIST, method = RequestMethod.POST)
	public ModelAndView getExcelReport(@RequestParam(value = "exportedMappingId", required = true) int mappingId,
			@RequestParam(value = "plugInType", required = true) String plugInType,
			@RequestParam(value = "streamType", required = true) String streamType) {

		ModelAndView model = new ModelAndView(new ReportView());
		ResponseObject responseObject = new ResponseObject();
		if (BaseConstants.UPSTREAM.equals(streamType))
			responseObject = parserAttributeService.getAttributeListByMappingId(mappingId, BaseConstants.UPSTREAM);
		else
			responseObject = composerAttributeService.getAttributeListByMappingId(mappingId, BaseConstants.DOWNSTREAM);
		
		List<Object> attributeList = null;
		if (responseObject.isSuccess()) {
			attributeList = (List<Object>) responseObject.getObject();
		}
		model.addObject("attributeList", attributeList);
		model.addObject("plugInType", plugInType);
		model.addObject("streamType", streamType);
		return model;
	}
	

	private static void initializeComposerSampleData() {
		composerSampleDataMap.put(EnumComposerAttributeHeader.SEQ_NO.getName(), Arrays.asList(seqNo));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DESTINATION_FIELD_NAME.getName(), Arrays.asList(destField));
		composerSampleDataMap.put(EnumComposerAttributeHeader.UNIFIED_FIELD_NAME.getName(), Arrays.asList(unifiedField));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DESCRIPTION.getName(), Arrays.asList(description));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DATA_TYPE.getName(), Arrays.asList(dataType));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DEFAULT_VALUE.getName(), Arrays.asList(defaultValue));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DATE_FORMAT.getName(), Arrays.asList(dateFormat));
		composerSampleDataMap.put(EnumComposerAttributeHeader.TRIM_CHARACTER.getName(), Arrays.asList(trimChar));
		composerSampleDataMap.put(EnumComposerAttributeHeader.TRIM_POSITION.getName(), Arrays.asList(cTrimPosition));
		composerSampleDataMap.put(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName(), Arrays.asList(replaceCondList));
		composerSampleDataMap.put(EnumComposerAttributeHeader.ENABLE_PADDING.getName(), Arrays.asList(isPaddingEnable));
		composerSampleDataMap.put(EnumComposerAttributeHeader.PADDING_LENGTH.getName(), Arrays.asList(paddinglength));
		composerSampleDataMap.put(EnumComposerAttributeHeader.TYPE.getName(), Arrays.asList(paddingType));
		composerSampleDataMap.put(EnumComposerAttributeHeader.PADDING_CHARACTER.getName(), Arrays.asList(paddingChar));
		composerSampleDataMap.put(EnumComposerAttributeHeader.PREFIX.getName(), Arrays.asList(prefix));
		composerSampleDataMap.put(EnumComposerAttributeHeader.SUFFIX.getName(), Arrays.asList(postfix));
		composerSampleDataMap.put(EnumComposerAttributeHeader.ASN1_DATATYPE.getName(), Arrays.asList(asn1datatype));
		composerSampleDataMap.put(EnumComposerAttributeHeader.DESTINATION_FIELD_FORMAT.getName(), Arrays.asList(destinationFieldFormat));
		composerSampleDataMap.put(EnumComposerAttributeHeader.ARGUMENT_DATATYPE.getName(), Arrays.asList(argumentDataType));
		composerSampleDataMap.put(EnumComposerAttributeHeader.CHOICE_ID.getName(), Arrays.asList(seqNo));
		composerSampleDataMap.put(EnumComposerAttributeHeader.CHILD_ATTRIBUTES.getName(), Arrays.asList(childattributes));
		composerSampleDataMap.put(EnumComposerAttributeHeader.LENGTH.getName(), Arrays.asList(length));
	}


	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_SAMPLE_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadSampleAttribute(@RequestParam(value = "parserType", required = false) String parserType,
			@RequestParam(value = "mappingId", required = true) String mappingId,
			@RequestParam(value = "colNamesFromGrid", required = false) String colNamesFromGrid,
			@RequestParam(value = "sampleRequired", required = false) String sampleRequired,
			@RequestParam(value = "requestActionType", required = false) String requestActionType,
			@RequestParam(value = "groupAttrId",defaultValue = "0", required = false) int groupId,
			HttpServletResponse response) {
		logger.debug(MODULE_NAME + " >> " + this.getClass().getName()
				+ " >> going to download sample attribute file for " + parserType);
		String sampleData = new String();
		String fileName = "";
		if("YES".equals(sampleRequired)){	
			fileName = parserType + "_sample_attribute" + ".csv";
		} else {
			DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
			fileName = "AttributeMapping" + "_" + dateFormatter.format(new Date()) + ".csv";
		}
		response.setContentType("application/csv");
		response.reset();
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);
		if(EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)){
			colNamesFromGrid =colNamesFromGrid +",Group Name,Sub Group Name";
		}

		String[] colNamesFromGridArray = colNamesFromGrid.split(",");
		
		if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)) {
			colNamesFromGrid = "";
			List<String> colNamesFromGridList = new ArrayList<String>();
			for(int i=0; i<colNamesFromGridArray.length; i++) {
				 String columnName = colNamesFromGridArray[i];
				if(!((BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE.equals(requestActionType) && (EnumParserAttributeHeader.START_LENGTH.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.END_LENGTH.getName().equalsIgnoreCase(columnName))) ||
						(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE.equals(requestActionType) && (EnumParserAttributeHeader.DEFAULT_VALUE.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.TRIM_POSITION.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.TRIM_CHAR.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.PREFIX.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.POSTFIX.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.RIGHT_DELIMETER.getName().equalsIgnoreCase(columnName) || EnumParserAttributeHeader.LENGTH.getName().equalsIgnoreCase(columnName))))) {
					colNamesFromGridList.add(columnName);
					if (i == colNamesFromGridArray.length - 1) {
						colNamesFromGrid = colNamesFromGrid + columnName ;
					} else {
						colNamesFromGrid = colNamesFromGrid + columnName + ",";
					}
				}
			}
			Object[] objArray = colNamesFromGridList.toArray();
			colNamesFromGridArray = Arrays.copyOf(objArray, objArray.length, String[].class);
		}
		
		List<ParserAttribute> resultList = new ArrayList<>();
		List<ParserAttribute> resultAttList = new ArrayList<>();
		if(groupId > 0){
			ResponseObject responseObject =parserGroupAttributeService.
					getAttachedAttributeListByGroupId(groupId,Integer.parseInt(mappingId));
			if(responseObject.isSuccess() && responseObject.getObject()!=null){
				resultList = (List<ParserAttribute>)responseObject.getObject();
			}
				
		}else{
			resultList = (List<ParserAttribute>) this.parserAttributeService
					.getAttributeListByMappingId(Integer.parseInt(mappingId), "UPSTREAM").getObject();
			for(ParserAttribute attribute : resultList){
				if(!attribute.isAssociatedByGroup())
				{
					if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
						VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute)attribute;
						ASN1ATTRTYPE attrType = ASN1ATTRTYPE.ATTRIBUTE; 
						if(BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE.equals(requestActionType)){
							attrType = ASN1ATTRTYPE.HEADER; 
						}
						if(attrType.equals(varLengthBinaryParserAttribute.getAttrType())) {
							 resultAttList.add(attribute);
						}
					 } else {
						 resultAttList.add(attribute);
					 }
				}
				if(attribute.isAssociatedByGroup() && EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)){
					 resultAttList.add(attribute);//get all attributes
				}
			}
			resultList=resultAttList;	
		}
		/*if(associatedWithGroup!=null && associatedWithGroup.equals("TRUE"))
		{
			for(ParserAttribute attribute : resultList){
				if(attribute.isAssociatedByGroup())
				{
					resultList1.add(attribute);
				}
				
			}
			resultList=resultList1;	
		}*/
		
		// check if mapping has no attribute then download dummy data
		if (resultList != null && resultList.isEmpty() && "YES".equals(sampleRequired)) {
			sampleData=prepareDummySampleDataString(colNamesFromGridArray,sampleData,parserType);
		} else {
			List<Map<String, String>> listOfAttrMap = new ArrayList<>();
			if (resultList != null) {
				for (ParserAttribute attribute : resultList) {					
					HashMap<String, String> parserAttrMap = new HashMap<>();
					parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD.getName(), attribute.getSourceField());
					parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), attribute.getUnifiedField());
					parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
					parserAttrMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), attribute.getDescription());
					parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), attribute.getDefaultValue());
					String trimCharData=attribute.getTrimChars();
					if(trimCharData!=null){
						if(trimCharData.contains("\"")) {
						   trimCharData=trimCharData.replaceAll("\"",BaseConstants.QUOTES);
						}
					    parserAttrMap.put(EnumParserAttributeHeader.TRIM_CHAR.getName(), "\""+trimCharData+"\"");
					}
					if (EngineConstants.ASCII_PARSING_PLUGIN.equals(parserType)) {
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(), attribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DATE_FORMAT.getName(), attribute.getDateFormat());
						AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) attribute;
						parserAttrMap.put(EnumParserAttributeHeader.IP_PORT_SEPERATOR.getName(), asciiParserAttribute.getIpPortSeperator());
						parserAttrMap.put(EnumParserAttributeHeader.IP_PORT_UNIFIED_FIELD.getName(), asciiParserAttribute.getPortUnifiedField());
					}else if (EngineConstants.VARIABLE_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)) {
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(), attribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DATE_FORMAT.getName(), attribute.getDateFormat());
						VarLengthAsciiParserAttribute varLengthAsciiParserAttribute = (VarLengthAsciiParserAttribute) attribute;
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_NAME.getName(), varLengthAsciiParserAttribute.getSourceFieldName());
					}else if(EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
						VarLengthBinaryParserAttribute varLengthBinaryParserAttribute = (VarLengthBinaryParserAttribute) attribute;		
						if("ATTRIBUTE".equals(varLengthBinaryParserAttribute.getAttrType().name())){
							parserAttrMap.put("Type",BaseConstants.VAR_LENGTH_BINARY_PARSER_ATTRIBUTE);
						}else if("HEADER".equals(varLengthBinaryParserAttribute.getAttrType().name())){
							parserAttrMap.put("Type",BaseConstants.VAR_LENGTH_BINARY_HEADER_PARSER_ATTRIBUTE);							
						}
						if(parserAttrMap.get("Type").equals(requestActionType)) {	
							parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),varLengthBinaryParserAttribute.getSourceFieldFormat());
							parserAttrMap.put(EnumParserAttributeHeader.DATE_FORMAT.getName(), attribute.getDateFormat());
							if("ATTRIBUTE".equals(varLengthBinaryParserAttribute.getAttrType().name())){
								parserAttrMap.put(EnumParserAttributeHeader.PREFIX.getName(),varLengthBinaryParserAttribute.getPrefix());
								parserAttrMap.put(EnumParserAttributeHeader.POSTFIX.getName(),varLengthBinaryParserAttribute.getPostfix());
								parserAttrMap.put(EnumParserAttributeHeader.LENGTH.getName(),String.valueOf(varLengthBinaryParserAttribute.getLength()));
								parserAttrMap.put(EnumParserAttributeHeader.RIGHT_DELIMETER.getName(),varLengthBinaryParserAttribute.getRightDelimiter());
							} 
							if("HEADER".equals(varLengthBinaryParserAttribute.getAttrType().name())){
								parserAttrMap.put(EnumParserAttributeHeader.START_LENGTH.getName(),String.valueOf(varLengthBinaryParserAttribute.getStartLength()));
								parserAttrMap.put(EnumParserAttributeHeader.END_LENGTH.getName(),String.valueOf(varLengthBinaryParserAttribute.getEndLength()));
							}
							parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_NAME.getName(), varLengthBinaryParserAttribute.getSourceFieldName());
					    }
					} else if(EngineConstants.XML_PARSING_PLUGIN.equals(parserType) || EngineConstants.NATFLOW_PARSING_PLUGIN.equals(parserType)){
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(), attribute.getSourceFieldFormat());
						if (EngineConstants.NATFLOW_PARSING_PLUGIN.equals(parserType)){
							NatFlowParserAttribute natFlowParserAttribute= (NatFlowParserAttribute)attribute;
							parserAttrMap.put(EnumParserAttributeHeader.DESTINATION_DATE_FORMAT.getName(), natFlowParserAttribute.getDestDateFormat());
						}
					}else if(EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)){
						FixedLengthBinaryParserAttribute binaryParserAttribute = (FixedLengthBinaryParserAttribute) attribute;		
						parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), binaryParserAttribute.getUnifiedField());
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),binaryParserAttribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), binaryParserAttribute.getDescription());
						parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), binaryParserAttribute.getDefaultValue());
						parserAttrMap.put(EnumParserAttributeHeader.START_LENGTH.getName(),String.valueOf(binaryParserAttribute.getStartLength()));
						parserAttrMap.put(EnumParserAttributeHeader.END_LENGTH.getName(),String.valueOf(binaryParserAttribute.getEndLength()));		
						parserAttrMap.put(EnumParserAttributeHeader.READ_AS_BITS.getName(),String.valueOf(binaryParserAttribute.isReadAsBits()));
						parserAttrMap.put(EnumParserAttributeHeader.BIT_START_LENGTH.getName(),String.valueOf(binaryParserAttribute.getBitStartLength()));
						parserAttrMap.put(EnumParserAttributeHeader.BIT_END_LENGTH.getName(),String.valueOf(binaryParserAttribute.getBitEndLength()));
						parserAttrMap.put(EnumParserAttributeHeader.PREFIX.getName(),binaryParserAttribute.getPrefix());
						parserAttrMap.put(EnumParserAttributeHeader.POSTFIX.getName(),binaryParserAttribute.getPostfix());
						parserAttrMap.put(EnumParserAttributeHeader.LENGTH.getName(),String.valueOf(binaryParserAttribute.getLength()));
						parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
						parserAttrMap.put(EnumParserAttributeHeader.RIGHT_DELIMETER.getName(),binaryParserAttribute.getRightDelimiter());
						parserAttrMap.put(EnumParserAttributeHeader.MULTI_RECORD.getName(),String.valueOf(binaryParserAttribute.isMultiRecord()));
					}else if(EngineConstants.PDF_PARSING_PLUGIN.equals(parserType)){
						PDFParserAttribute pdfParserAttribute = (PDFParserAttribute) attribute;		
						parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), pdfParserAttribute.getUnifiedField());
						parserAttrMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), pdfParserAttribute.getDescription());
						parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), pdfParserAttribute.getDefaultValue());
						parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
						if(pdfParserAttribute.getLocation()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.LOCATION.getName(), "\""+pdfParserAttribute.getLocation()+"\"");
						}
						if(pdfParserAttribute.getReferenceRow()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.REFERENCE_ROW.getName(), "\""+pdfParserAttribute.getReferenceRow()+"\"");
						}
						if(pdfParserAttribute.getPageNumber()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.PAGE_NUMBER.getName(), "\""+pdfParserAttribute.getPageNumber()+"\"");
						}
						if(pdfParserAttribute.getMandatory()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.MANDATORY.getName(), "\""+pdfParserAttribute.getMandatory()+"\"");
						}																		
						if(pdfParserAttribute.getValueSeparator()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.VALUE_SEPARATOR.getName(), "\""+pdfParserAttribute.getValueSeparator()+"\"");
						}
						if(pdfParserAttribute.getColumnEndsWith()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.COLUMN_ENDS_WITH.getName(), "\""+pdfParserAttribute.getColumnEndsWith()+"\"");
						}
						if(pdfParserAttribute.getTrimChars()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.TRIM_CHAR.getName(), "\""+pdfParserAttribute.getTrimChars()+"\"");
						}
						if(pdfParserAttribute.getReferenceCol()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.REFERENCE_COL.getName(), "\""+pdfParserAttribute.getReferenceCol()+"\"");
						}
						parserAttrMap.put(EnumParserAttributeHeader.TABLE_FOOTER.getName(), String.valueOf(pdfParserAttribute.isTableFooter()));
						parserAttrMap.put(EnumParserAttributeHeader.COLUMN_IDENTIFIER.getName(), pdfParserAttribute.getColumnIdentifier());
						parserAttrMap.put(EnumParserAttributeHeader.COLUMN_START_LOCATION.getName(), pdfParserAttribute.getColumnStartLocation());
						parserAttrMap.put(EnumParserAttributeHeader.COLUMN_STARTS_WITH.getName(), pdfParserAttribute.getColumnStartsWith());
						parserAttrMap.put(EnumParserAttributeHeader.ROW_TEXT_ALIGHMENT.getName(), pdfParserAttribute.getRowTextAlignment());
						parserAttrMap.put(EnumParserAttributeHeader.MULTILINE_ATTRIBUTE.getName(), String.valueOf(pdfParserAttribute.isMultiLineAttribute()));
						parserAttrMap.put(EnumParserAttributeHeader.MULTIPLE_VALUES.getName(), String.valueOf(pdfParserAttribute.isMultipleValues()));
					}
					else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType)){
						FixedLengthASCIIParserAttribute fixedLengthAsciiParserAttribute = (FixedLengthASCIIParserAttribute) attribute;		
						parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), fixedLengthAsciiParserAttribute.getUnifiedField());
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),fixedLengthAsciiParserAttribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), fixedLengthAsciiParserAttribute.getDescription());
						parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), fixedLengthAsciiParserAttribute.getDefaultValue());
						parserAttrMap.put(EnumParserAttributeHeader.START_LENGTH.getName(),String.valueOf(fixedLengthAsciiParserAttribute.getStartLength()));
						parserAttrMap.put(EnumParserAttributeHeader.END_LENGTH.getName(),String.valueOf(fixedLengthAsciiParserAttribute.getEndLength()));					
						parserAttrMap.put(EnumParserAttributeHeader.PREFIX.getName(),fixedLengthAsciiParserAttribute.getPrefix());
						parserAttrMap.put(EnumParserAttributeHeader.POSTFIX.getName(),fixedLengthAsciiParserAttribute.getPostfix());
						String lengthData=fixedLengthAsciiParserAttribute.getLength();
						if(lengthData!=null){
							if(lengthData.contains("\"")) {
								lengthData=lengthData.replaceAll("\"",BaseConstants.QUOTES);
							}
						    parserAttrMap.put(EnumParserAttributeHeader.LENGTH.getName(), "\""+lengthData+"\"");
						}
						parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
						parserAttrMap.put(EnumParserAttributeHeader.RIGHT_DELIMETER.getName(),fixedLengthAsciiParserAttribute.getRightDelimiter());
					}else if(EngineConstants.HTML_PARSING_PLUGIN.equals(parserType)){
							HtmlParserAttribute htmlParserAttribute = (HtmlParserAttribute) attribute;		
							parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), htmlParserAttribute.getUnifiedField());
							parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),htmlParserAttribute.getSourceFieldFormat());
							parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), htmlParserAttribute.getDefaultValue());
							parserAttrMap.put(EnumParserAttributeHeader.FIELD_IDENTIFIER.getName(),htmlParserAttribute.getFieldIdentifier());
							parserAttrMap.put(EnumParserAttributeHeader.FIELD_EXTRACTION_METHOD.getName(),htmlParserAttribute.getFieldExtractionMethod());					
							parserAttrMap.put(EnumParserAttributeHeader.FILD_SECTION_ID.getName(),htmlParserAttribute.getFieldSectionId());
							parserAttrMap.put(EnumParserAttributeHeader.CONTAINS_FIELD_ATTRIBUTE.getName(),htmlParserAttribute.getContainsFieldAttribute());
							parserAttrMap.put(EnumParserAttributeHeader.TD_NO.getName(),htmlParserAttribute.getTdNo());	
							parserAttrMap.put(EnumParserAttributeHeader.VALUE_SEPARATOR.getName(),"\""+htmlParserAttribute.getValueSeparator()+"\"");
							parserAttrMap.put(EnumParserAttributeHeader.VALUE_INDEX.getName(),htmlParserAttribute.getValueIndex());
					}else if(EngineConstants.XLS_PARSING_PLUGIN.equals(parserType)){
						XlsParserAttribute xlsParserAttribute = (XlsParserAttribute) attribute;		
						parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), xlsParserAttribute.getUnifiedField());
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),xlsParserAttribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), xlsParserAttribute.getDefaultValue());
						parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), xlsParserAttribute.getTrimPosition());
						parserAttrMap.put(EnumParserAttributeHeader.STARTS_WITH.getName(), xlsParserAttribute.getStartsWith());
						parserAttrMap.put(EnumParserAttributeHeader.TABLE_FOOTER.getName(), String.valueOf(xlsParserAttribute.isTableFooter()));
						parserAttrMap.put(EnumParserAttributeHeader.EXCEL_ROW.getName(), xlsParserAttribute.getExcelRow());
						parserAttrMap.put(EnumParserAttributeHeader.EXCEL_COL.getName(), xlsParserAttribute.getExcelCol());
						parserAttrMap.put(EnumParserAttributeHeader.RELATIVE_EXCEL_ROW.getName(), xlsParserAttribute.getRelativeExcelRow());
						parserAttrMap.put(EnumParserAttributeHeader.COLUMN_STARTS_WITH.getName(), xlsParserAttribute.getColumnStartsWith());
						parserAttrMap.put(EnumParserAttributeHeader.COLUMN_CONTAINS.getName(), xlsParserAttribute.getColumnContains());
						parserAttrMap.put(EnumParserAttributeHeader.TABLE_ROW_ATTRIBUTE.getName(),  String.valueOf(xlsParserAttribute.isTableRowAttribute()));
					}else if(EngineConstants.TAP_PARSING_PLUGIN.equals(parserType)){
						TAPParserAttribute tapParserAttribute = (TAPParserAttribute) attribute;		
						if(tapParserAttribute.getTrimChars()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.TRIM_CHAR.getName(), "\""+tapParserAttribute.getTrimChars()+"\"");
						}
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),tapParserAttribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.ASN1_DATA_TYPE.getName(), tapParserAttribute.getASN1DataType());
						if(tapParserAttribute.getChildAttributes()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName(), "\""+tapParserAttribute.getChildAttributes()+"\"");
						}
						String recordIntializer= "False";
						if(tapParserAttribute.isRecordInitilializer()!=null) {
							recordIntializer = tapParserAttribute.isRecordInitilializer().substring(0, 1).toUpperCase()+tapParserAttribute.isRecordInitilializer().substring(1);
						}
						parserAttrMap.put(EnumParserAttributeHeader.IS_RECORD_INITIALIZER.getName(),recordIntializer);		
						parserAttrMap.put(EnumParserAttributeHeader.CHOICEID_HOLDER_UNIFIED_fIELD.getName(), tapParserAttribute.getUnifiedFieldHoldsChoiceId());
						if(tapParserAttribute.getSrcDataFormat()!=null){
							parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_DATA_FORMAT.getName(), tapParserAttribute.getSrcDataFormat().name());
						}
						parserAttrMap.put(EnumParserAttributeHeader.PARSE_AS_JSON.getName(), tapParserAttribute.isParseAsJson());
						if(tapParserAttribute.isAssociatedByGroup()) {
							String groupName=tapParserAttribute.getParserGroupAttribute().getName();
							parserAttrMap.put(EnumParserAttributeHeader.GROUP_NAME.getName(), groupName);
							parserAttrMap.put(EnumParserAttributeHeader.SUB_GROUP_NAME.getName(), parserGroupAttributeService.getSubGroupNameListByGroupId(tapParserAttribute.getParserGroupAttribute()));
						}
					}else if(EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)){
						ASN1ParserAttribute asn1ParserAttribute = (ASN1ParserAttribute) attribute;	
						if("ATTRIBUTE".equals(asn1ParserAttribute.getAttrType().name())){
							parserAttrMap.put("Type","ASN1_PARSER_ATTRIBUTE");
						}else if("HEADER".equals(asn1ParserAttribute.getAttrType().name())){
							parserAttrMap.put("Type","ASN1_HEADER_PARSER_ATTRIBUTE");							
						}else if("TRAILER".equals(asn1ParserAttribute.getAttrType().name())){
							parserAttrMap.put("Type","ASN1_TRAILER_PARSER_ATTRIBUTE");							
						}
						
						if(parserAttrMap.get("Type").equals(requestActionType))
						{	
							parserAttrMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), asn1ParserAttribute.getUnifiedField());
							parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(),asn1ParserAttribute.getSourceFieldFormat());
							parserAttrMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), asn1ParserAttribute.getDescription());
							parserAttrMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), asn1ParserAttribute.getDefaultValue());
							parserAttrMap.put(EnumParserAttributeHeader.ASN1_DATA_TYPE.getName(), asn1ParserAttribute.getASN1DataType());
							if(asn1ParserAttribute.getChildAttributes()!=null){
								parserAttrMap.put(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName(), "\""+asn1ParserAttribute.getChildAttributes()+"\"");
							}
							if(asn1ParserAttribute.isRecordInitilializer()!=null) {
								String recordIntializer = asn1ParserAttribute.isRecordInitilializer().substring(0, 1).toUpperCase()+asn1ParserAttribute.isRecordInitilializer().substring(1);
								parserAttrMap.put(EnumParserAttributeHeader.IS_RECORD_INITIALIZER.getName(),recordIntializer);		
							}
							parserAttrMap.put(EnumParserAttributeHeader.CHOICEID_HOLDER_UNIFIED_fIELD.getName(), asn1ParserAttribute.getUnifiedFieldHoldsChoiceId());
							if(asn1ParserAttribute.getSrcDataFormat()!=null){
								parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_DATA_FORMAT.getName(), asn1ParserAttribute.getSrcDataFormat().name());
							}
							parserAttrMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
							
						}			
					}else if (EngineConstants.JSON_PARSING_PLUGIN.equals(parserType)) {
						parserAttrMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(), attribute.getSourceFieldFormat());
						parserAttrMap.put(EnumParserAttributeHeader.DATE_FORMAT.getName(), attribute.getDateFormat());
					}
					if( (!EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType) && !EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)) || parserAttrMap.get("Type").equals(requestActionType)){
						listOfAttrMap.add(parserAttrMap);
					}	
						
				}
			}
			if("YES".equals(sampleRequired) && EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType) && listOfAttrMap.isEmpty())
			{
				sampleData=prepareDummySampleDataString(colNamesFromGridArray,sampleData,parserType);
			}
			for (Map<String, String> listMap : listOfAttrMap) {
				for (int i = 0; i < colNamesFromGridArray.length; i++) {
					if (i == colNamesFromGridArray.length - 1) {
						if (listMap.get(colNamesFromGridArray[i]) == null) {
						} else {
							sampleData += listMap.get(colNamesFromGridArray[i]);
						}
					} else {
						if (listMap.get(colNamesFromGridArray[i]) == null)
							sampleData += ",";
						else
							sampleData += listMap.get(colNamesFromGridArray[i]) + ",";
					}
				}
				sampleData += "\n";
			}
		}
		try {
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream outputStream = response.getOutputStream();
			logger.debug(MODULE_NAME + " >> " + "writing data into file");
			outputStream.write(colNamesFromGrid.getBytes());
			outputStream.println();
			outputStream.write(sampleData.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.error(MODULE_NAME + " >> " + "error occured while writing data into file : ",e);
		}
		return null;	
	}

	public static Map<String, List<String>> initializeSampleData(Map<String, List<String>> sampleDataMap) {
		sampleDataMap.put(EnumParserAttributeHeader.SOURCE_FIELD.getName(), Arrays.asList(sourceField));
		sampleDataMap.put(EnumParserAttributeHeader.SOURCE_FIELD_NAME.getName(), Arrays.asList(sourceFieldName));
		sampleDataMap.put(EnumParserAttributeHeader.UNIFIED_FIELD.getName(), Arrays.asList(unifiedField));
		sampleDataMap.put(EnumParserAttributeHeader.DESCRIPTION.getName(), Arrays.asList(description));
		sampleDataMap.put(EnumParserAttributeHeader.DEFAULT_VALUE.getName(), Arrays.asList(defaultValue));
		sampleDataMap.put(EnumParserAttributeHeader.TRIM_CHAR.getName(), Arrays.asList(trimChar));
		sampleDataMap.put(EnumParserAttributeHeader.TRIM_POSITION.getName(), Arrays.asList(trimPosition));
		sampleDataMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName(), Arrays.asList(sourceFieldFormat));
		sampleDataMap.put(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT_BINARY.getName(), Arrays.asList(sourceFieldFormatbinary));
		sampleDataMap.put(EnumParserAttributeHeader.DATE_FORMAT.getName(), Arrays.asList(dateFormat));
		sampleDataMap.put(EnumParserAttributeHeader.IP_PORT_SEPERATOR.getName(), Arrays.asList(ipPortSeperator));
		sampleDataMap.put(EnumParserAttributeHeader.IP_PORT_UNIFIED_FIELD.getName(), Arrays.asList(portUnifiedField));
		sampleDataMap.put(EnumParserAttributeHeader.START_LENGTH.getName(), Arrays.asList(startLength ));
		sampleDataMap.put(EnumParserAttributeHeader.END_LENGTH.getName(), Arrays.asList(endLength));
		sampleDataMap.put(EnumParserAttributeHeader.READ_AS_BITS.getName(), Arrays.asList(readAsBits));
		sampleDataMap.put(EnumParserAttributeHeader.BIT_START_LENGTH.getName(), Arrays.asList(bitStartLength));
		sampleDataMap.put(EnumParserAttributeHeader.BIT_END_LENGTH.getName(), Arrays.asList(bitEndLength));
		sampleDataMap.put(EnumParserAttributeHeader.PREFIX.getName(), Arrays.asList(prefix));
		sampleDataMap.put(EnumParserAttributeHeader.POSTFIX.getName(), Arrays.asList(postfix));
		sampleDataMap.put(EnumParserAttributeHeader.LENGTH.getName(), Arrays.asList(length));		
		sampleDataMap.put(EnumParserAttributeHeader.RIGHT_DELIMETER.getName(), Arrays.asList(rightDelimiter));
		sampleDataMap.put(EnumParserAttributeHeader.ASN1_DATA_TYPE.getName(), Arrays.asList(asn1datatype));
		sampleDataMap.put(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName(), Arrays.asList(childattributes));
		sampleDataMap.put(EnumParserAttributeHeader.IS_RECORD_INITIALIZER.getName(), Arrays.asList(isrecordinitializer));		
		sampleDataMap.put(EnumParserAttributeHeader.CHOICEID_HOLDER_UNIFIED_fIELD.getName(), Arrays.asList(choiceidholderunifiedfield));
		sampleDataMap.put(EnumParserAttributeHeader.SOURCE_FIELD_DATA_FORMAT.getName(), Arrays.asList(sourcefielddataformat));
		sampleDataMap.put(EnumParserAttributeHeader.FIELD_IDENTIFIER.getName(), Arrays.asList(fieldIdentifier));
		sampleDataMap.put(EnumParserAttributeHeader.FILD_SECTION_ID.getName(), Arrays.asList(fieldSectionId));		
		sampleDataMap.put(EnumParserAttributeHeader.FIELD_EXTRACTION_METHOD.getName(), Arrays.asList(fieldExtractionMethod));
		sampleDataMap.put(EnumParserAttributeHeader.CONTAINS_FIELD_ATTRIBUTE.getName(), Arrays.asList(containsFieldAttribute));
		sampleDataMap.put(EnumParserAttributeHeader.LOCATION.getName(), Arrays.asList(location));
		sampleDataMap.put(EnumParserAttributeHeader.COLUMN_IDENTIFIER.getName(), Arrays.asList(columnIdentifier));
		sampleDataMap.put(EnumParserAttributeHeader.COLUMN_START_LOCATION.getName(), Arrays.asList(columnStartLocation));
		sampleDataMap.put(EnumParserAttributeHeader.COLUMN_STARTS_WITH.getName(), Arrays.asList(columnStartsWith));
		sampleDataMap.put(EnumParserAttributeHeader.REFERENCE_ROW.getName(), Arrays.asList(referenceRow));
		sampleDataMap.put(EnumParserAttributeHeader.PAGE_NUMBER.getName(), Arrays.asList(pageNumber));
		sampleDataMap.put(EnumParserAttributeHeader.MANDATORY.getName(), Arrays.asList(mandatory));
		sampleDataMap.put(EnumParserAttributeHeader.COLUMN_ENDS_WITH.getName(), Arrays.asList(columnEndsWith));
		sampleDataMap.put(EnumParserAttributeHeader.TD_NO.getName(), Arrays.asList(tdNo));
		sampleDataMap.put(EnumParserAttributeHeader.REFERENCE_COL.getName(), Arrays.asList(referenceCol));
		sampleDataMap.put(EnumParserAttributeHeader.TABLE_FOOTER.getName(), Arrays.asList(tableFooter));
		sampleDataMap.put(EnumParserAttributeHeader.STARTS_WITH.getName(), Arrays.asList(startsWith));
		sampleDataMap.put(EnumParserAttributeHeader.EXCEL_ROW.getName(), Arrays.asList(excelRow));
		sampleDataMap.put(EnumParserAttributeHeader.EXCEL_COL.getName(), Arrays.asList(excelCol));
		sampleDataMap.put(EnumParserAttributeHeader.RELATIVE_EXCEL_ROW.getName(), Arrays.asList(relativeExcelRow));
		sampleDataMap.put(EnumParserAttributeHeader.COLUMN_CONTAINS.getName(), Arrays.asList(columnContains));
		sampleDataMap.put(EnumParserAttributeHeader.TABLE_ROW_ATTRIBUTE.getName(),  Arrays.asList(tableRowAttribute));
		sampleDataMap.put(EnumParserAttributeHeader.VALUE_SEPARATOR.getName(), Arrays.asList(valueSeparator));
		sampleDataMap.put(EnumParserAttributeHeader.VALUE_INDEX.getName(), Arrays.asList(valueIndex));
		sampleDataMap.put(EnumParserAttributeHeader.ROW_TEXT_ALIGHMENT.getName(), Arrays.asList(rowTextAlignment));
		sampleDataMap.put(EnumParserAttributeHeader.MULTILINE_ATTRIBUTE.getName(), Arrays.asList(multiLineAttribute));
		sampleDataMap.put(EnumParserAttributeHeader.MULTIPLE_VALUES.getName(), Arrays.asList(multipleValues));
		sampleDataMap.put(EnumParserAttributeHeader.PARSE_AS_JSON.getName(), Arrays.asList(parseAsJson));
		sampleDataMap.put(EnumParserAttributeHeader.GROUP_NAME.getName(), Arrays.asList(groupName));
		sampleDataMap.put(EnumParserAttributeHeader.SUB_GROUP_NAME.getName(), Arrays.asList(subGroupName));
		sampleDataMap.put(EnumParserAttributeHeader.MULTI_RECORD.getName(), Arrays.asList(multiRecord));
		return sampleDataMap;
	}
	
	public String prepareDummySampleDataString(String[] colNamesFromGridArray,String sampleData,String parserType){
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < colNamesFromGridArray.length; i++) {
				if (i == colNamesFromGridArray.length - 1)
					sampleData += sampleDataMap.get(colNamesFromGridArray[i]).get(j);
				else{
					if(colNamesFromGridArray[i].equals(EnumParserAttributeHeader.TRIM_CHAR.getName()) && sampleDataMap.get(EnumParserAttributeHeader.TRIM_CHAR.getName()).get(j)!=null){
						sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.TRIM_CHAR.getName()).get(j)+"\"" + ",";
					}
					else if(EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN.equals(parserType) && colNamesFromGridArray[i].equals(EnumParserAttributeHeader.LENGTH.getName()))
					{
						sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.LENGTH.getName()).get(j) +"\"" + ",";

					}
					else if((EngineConstants.VARIABLE_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType) || EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN.equals(parserType)) && colNamesFromGridArray[i].equals(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT.getName()))
					{
						sampleData += sampleDataMap.get(EnumParserAttributeHeader.SOURCE_FIELD_FORMAT_BINARY.getName()).get(j) + ",";

					}else if(EngineConstants.ASN1_PARSING_PLUGIN.equals(parserType)  && colNamesFromGridArray[i].equals(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName())){
						if(sampleDataMap.get(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName()).get(j)!=null){
							sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName()).get(j)+"\"" + ",";
						}	
					}else if(EngineConstants.PDF_PARSING_PLUGIN.equals(parserType)  && ((colNamesFromGridArray[i].equals(EnumParserAttributeHeader.REFERENCE_COL.getName()) || colNamesFromGridArray[i].equals(EnumParserAttributeHeader.LOCATION.getName()) || (colNamesFromGridArray[i].equals(EnumParserAttributeHeader.REFERENCE_ROW.getName()))))){
						if(colNamesFromGridArray[i].equals(EnumParserAttributeHeader.LOCATION.getName()) && sampleDataMap.get(EnumParserAttributeHeader.LOCATION.getName()).get(j)!=null){
							sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.LOCATION.getName()).get(j)+"\"" + ",";
						}else if(colNamesFromGridArray[i].equals(EnumParserAttributeHeader.REFERENCE_ROW.getName()) && sampleDataMap.get(EnumParserAttributeHeader.REFERENCE_ROW.getName()).get(j)!=null){
							sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.REFERENCE_ROW.getName()).get(j)+"\"" + ",";
						}else if(colNamesFromGridArray[i].equals(EnumParserAttributeHeader.REFERENCE_COL.getName()) && sampleDataMap.get(EnumParserAttributeHeader.REFERENCE_COL.getName()).get(j)!=null){
							sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.REFERENCE_COL.getName()).get(j)+"\"" + ",";
						}	
					}else if(EngineConstants.HTML_PARSING_PLUGIN.equals(parserType) && colNamesFromGridArray[i].equals(EnumParserAttributeHeader.VALUE_SEPARATOR.getName()) && sampleDataMap.get(EnumParserAttributeHeader.VALUE_SEPARATOR.getName()).get(j)!=null){
						sampleData += "\""+sampleDataMap.get(EnumParserAttributeHeader.VALUE_SEPARATOR.getName()).get(j)+"\"" + ",";
					}else{	
						sampleData += sampleDataMap.get(colNamesFromGridArray[i]).get(j) + ",";
					}
				}
			}
			sampleData += "\n";
		}
		return sampleData;
	}
	
	public String prepareDummyComposerSampleDataString(String[] colNamesFromGridArray,String sampleData,String parserType){
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < colNamesFromGridArray.length; i++) {
				if (i == colNamesFromGridArray.length - 1)
					sampleData += composerSampleDataMap.get(colNamesFromGridArray[i]).get(j);
				else{
					if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(parserType)  && colNamesFromGridArray[i].equals(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName())){
						if(composerSampleDataMap.get(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName()).get(j)!=null){
							sampleData += "\""+composerSampleDataMap.get(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName()).get(j)+"\"" + ",";
						}	
					}else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType)  && colNamesFromGridArray[i].equals(EnumParserAttributeHeader.CHILD_ATTRIBUTES.getName())){
						if(composerSampleDataMap.get(EnumComposerAttributeHeader.CHILD_ATTRIBUTES.getName()).get(j)!=null){
							sampleData += "\""+composerSampleDataMap.get(EnumComposerAttributeHeader.CHILD_ATTRIBUTES.getName()).get(j)+"\"" + ",";
						}	
					}else{
						sampleData += composerSampleDataMap.get(colNamesFromGridArray[i]).get(j) + ",";
					}
				}
			}
			sampleData += "\n";
		}
		return sampleData;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_SAMPLE_COMPOSER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadSampleComposerAttribute(@RequestParam(value = "parserType", required = false) String parserType,
			@RequestParam(value = "mappingId", required = true) String mappingId,
			@RequestParam(value = "colNamesFromGrid", required = false) String colNamesFromGrid,
			@RequestParam(value = "sampleRequired", required = false) String sampleRequired,
			@RequestParam(value = "requestActionType", required = false) String requestActionType,
			HttpServletResponse response) {
		logger.debug(MODULE_NAME + " >> " + this.getClass().getName()
				+ " >> going to download sample attribute file for " + parserType);
		String sampleData = "";
		String fileName;
		if("YES".equals(sampleRequired)){	
			fileName = parserType + "_sample_attribute" + ".csv";
		} else {
			DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
			fileName = "AttributeMapping" + "_" + dateFormatter.format(new Date()) + ".csv";
		}
		response.setContentType("application/csv");
		response.reset();
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);

		String[] colNamesFromGridArray = colNamesFromGrid.split(",");
		List<ComposerAttribute> resultList = (List<ComposerAttribute>) this.composerAttributeService
				.getAttributeListByMappingId(Integer.parseInt(mappingId), BaseConstants.DOWNSTREAM).getObject();
		
		
		// check if mapping has no attribute then download dummy data
		if (resultList!=null && resultList.isEmpty() && "YES".equals(sampleRequired)) {
			sampleData=prepareDummyComposerSampleDataString(colNamesFromGridArray,sampleData,parserType);
		} else {
			List<Map<String, String>> listOfAttrMap = new ArrayList<>();
			if (resultList != null) {
				
				for (ComposerAttribute attribute : resultList) {					
					HashMap<String, String> composerAttrMap = new HashMap<>();
					composerAttrMap.put(EnumComposerAttributeHeader.DESTINATION_FIELD_NAME.getName(), attribute.getDestinationField());
					composerAttrMap.put(EnumComposerAttributeHeader.UNIFIED_FIELD_NAME.getName(), attribute.getUnifiedField());
					composerAttrMap.put(EnumComposerAttributeHeader.DESCRIPTION.getName(), attribute.getDescription());
					composerAttrMap.put(EnumComposerAttributeHeader.DEFAULT_VALUE.getName(), attribute.getDefualtValue());
					composerAttrMap.put(EnumComposerAttributeHeader.TRIM_CHARACTER.getName(), attribute.getTrimchars());
					
					if(EngineConstants.ASCII_COMPOSER_PLUGIN.equals(parserType)) {
						composerAttrMap.put(EnumComposerAttributeHeader.SEQ_NO.getName(), String.valueOf(attribute.getSequenceNumber()));
						if(attribute.getDataType() != null){
							composerAttrMap.put(EnumComposerAttributeHeader.DATA_TYPE.getName(), attribute.getDataType().toString());
						}
						composerAttrMap.put(EnumComposerAttributeHeader.DATE_FORMAT.getName(), attribute.getDateFormat());
						composerAttrMap.put(EnumComposerAttributeHeader.TRIM_POSITION.getName(), attribute.getTrimPosition());
						ASCIIComposerAttr asciiComposerAttribute = (ASCIIComposerAttr) attribute;
						composerAttrMap.put(EnumComposerAttributeHeader.REPLACE_CONDITION_LIST.getName(), "\""+asciiComposerAttribute.getReplaceConditionList()+"\"");
						composerAttrMap.put(EnumComposerAttributeHeader.ENABLE_PADDING.getName(), String.valueOf(asciiComposerAttribute.isPaddingEnable()));
						composerAttrMap.put(EnumComposerAttributeHeader.PADDING_LENGTH.getName(), String.valueOf(asciiComposerAttribute.getLength()));
						composerAttrMap.put(EnumComposerAttributeHeader.TYPE.getName(), asciiComposerAttribute.getPaddingType().getValue().toUpperCase());
						composerAttrMap.put(EnumComposerAttributeHeader.PADDING_CHARACTER.getName(), asciiComposerAttribute.getPaddingChar());
						composerAttrMap.put(EnumComposerAttributeHeader.PREFIX.getName(), asciiComposerAttribute.getPrefix());
						composerAttrMap.put(EnumComposerAttributeHeader.SUFFIX.getName(), asciiComposerAttribute.getSuffix());
					} else if(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN.equals(parserType)){
						FixedLengthASCIIComposerAttribute fixedLengthAsciiParserAttribute = (FixedLengthASCIIComposerAttribute) attribute;	
						composerAttrMap.put(EnumComposerAttributeHeader.SEQ_NO.getName(), String.valueOf(attribute.getSequenceNumber()));
						composerAttrMap.put(EnumComposerAttributeHeader.PREFIX.getName(),fixedLengthAsciiParserAttribute.getPrefix());
						composerAttrMap.put(EnumComposerAttributeHeader.SUFFIX.getName(),fixedLengthAsciiParserAttribute.getSuffix());
						composerAttrMap.put(EnumComposerAttributeHeader.LENGTH.getName(), String.valueOf(fixedLengthAsciiParserAttribute.getFixedLength()));
						composerAttrMap.put(EnumComposerAttributeHeader.TYPE.getName(), fixedLengthAsciiParserAttribute.getPaddingType().getValue().toUpperCase());
						composerAttrMap.put(EnumComposerAttributeHeader.TRIM_POSITION.getName(), fixedLengthAsciiParserAttribute.getTrimPosition());
						if(attribute.getDataType() != null){
							composerAttrMap.put(EnumComposerAttributeHeader.DATA_TYPE.getName(), attribute.getDataType().toString());
						}
						composerAttrMap.put(EnumComposerAttributeHeader.DATE_FORMAT.getName(),fixedLengthAsciiParserAttribute.getFixedLengthDateFormat());
						composerAttrMap.put(EnumComposerAttributeHeader.PADDING_CHARACTER.getName(),fixedLengthAsciiParserAttribute.getPaddingChar());
					} else if(EngineConstants.XML_COMPOSER_PLUGIN.equals(parserType)){
						XMLComposerAttribute XMLComposerAttribute = (XMLComposerAttribute) attribute;	
						composerAttrMap.put(EnumComposerAttributeHeader.SEQ_NO.getName(), String.valueOf(attribute.getSequenceNumber()));
						if(attribute.getDataType() != null){
							composerAttrMap.put(EnumComposerAttributeHeader.DATA_TYPE.getName(), attribute.getDataType().toString());
						}
						
					} else if(EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType)){
						ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute) attribute;	
						if("ATTRIBUTE".equals(asn1ComposerAttribute.getAttrType().name())){
							composerAttrMap.put("Type","ASN1_COMPOSER_ATTRIBUTE");
						}else if("HEADER".equals(asn1ComposerAttribute.getAttrType().name())){
							composerAttrMap.put("Type","ASN1_HEADER_COMPOSER_ATTRIBUTE");							
						}else if("TRAILER".equals(asn1ComposerAttribute.getAttrType().name())){
							composerAttrMap.put("Type","ASN1_TRAILER_COMPOSER_ATTRIBUTE");							
						}
						
						if(composerAttrMap.get("Type").equals(requestActionType)) {	
							composerAttrMap.put(EnumComposerAttributeHeader.ASN1_DATATYPE.getName(), asn1ComposerAttribute.getasn1DataType());
							composerAttrMap.put(EnumComposerAttributeHeader.DESTINATION_FIELD_FORMAT.getName(), asn1ComposerAttribute.getDestFieldDataFormat());
							composerAttrMap.put(EnumComposerAttributeHeader.ARGUMENT_DATATYPE.getName(), asn1ComposerAttribute.getArgumentDataType());
							composerAttrMap.put(EnumComposerAttributeHeader.CHOICE_ID.getName(), asn1ComposerAttribute.getChoiceId());
							if(asn1ComposerAttribute.getChildAttributes() != null){
								composerAttrMap.put(EnumComposerAttributeHeader.CHILD_ATTRIBUTES.getName(), "\""+asn1ComposerAttribute.getChildAttributes()+"\"");
							}
						}			
					}
					if (!EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType) || composerAttrMap.get("Type").equals(requestActionType)) {
						listOfAttrMap.add(composerAttrMap);
					}
				}
			}
			if("YES".equals(sampleRequired) && EngineConstants.ASN1_COMPOSER_PLUGIN.equals(parserType) && listOfAttrMap.isEmpty()) {
				sampleData=prepareDummyComposerSampleDataString(colNamesFromGridArray,sampleData,parserType);
			}
			for (Map<String, String> listMap : listOfAttrMap) {
				for (int i = 0; i < colNamesFromGridArray.length; i++) {
					if (i == colNamesFromGridArray.length - 1) {
						if (listMap.get(colNamesFromGridArray[i]) == null) {
						} else {
							sampleData += listMap.get(colNamesFromGridArray[i]);
						}
					} else {
						if (listMap.get(colNamesFromGridArray[i]) == null)
							sampleData += ",";
						else
							sampleData += listMap.get(colNamesFromGridArray[i]) + ",";
					}
				}
				sampleData += "\n";
			}
		}
		try {
			response.setCharacterEncoding("UTF-8");
			ServletOutputStream outputStream = response.getOutputStream();
			logger.debug(MODULE_NAME + " >> " + "writing data into file");
			outputStream.write(colNamesFromGrid.getBytes());
			outputStream.println();
			outputStream.write(sampleData.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.error(MODULE_NAME + " >> " + "error occured while writing data into file : ",e);
		}
		return null;
	}
	
	private static void initializeDbDriverSampleData() {
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.DATABASE_FIELD_NAME.getName(), Arrays.asList(databaseFieldName));
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.UNIFIED_FIELD_NAME.getName(), Arrays.asList(unifiedField));
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.DATA_TYPE.getName(), Arrays.asList(dataType));
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.DEFAULT_VALUE.getName(), Arrays.asList(defaultValue));
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.ENABLE_PADDING.getName(), Arrays.asList(isPaddingEnable));	
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.PADDING_LENGTH.getName(), Arrays.asList(paddinglength));	
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.TYPE.getName(), Arrays.asList(paddingType));	
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.PADDING_CHARACTER.getName(), Arrays.asList(paddingChar));	
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.PREFIX.getName(), Arrays.asList(prefix));	
		dbDriverSampleDataMap.put(EnumDbDriverAttributeHeader.SUFFIX.getName(), Arrays.asList(postfix));
	}	
	public String prepareDummyDistributionSampleDataString(String[] colNamesFromGridArray,String sampleData){
		for (int j = 0; j < 3; j++) {
			for (int i = 0; i < colNamesFromGridArray.length; i++) {
				if (i == colNamesFromGridArray.length - 1)
					sampleData += dbDriverSampleDataMap.get(colNamesFromGridArray[i]).get(j);
				else{
					sampleData += dbDriverSampleDataMap.get(colNamesFromGridArray[i]).get(j) + ",";		
				}
			}
			sampleData += "\n";
		}
		return sampleData;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.DOWNLOAD_DISTRIBUTION_DB_DRIVER_ATTRIBUTE, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView downloadSampleDistributionDbDriverAttribute(@RequestParam(value = "driverId", required = true) String driverId,
			@RequestParam(value = "colNamesFromGrid", required = false) String colNamesFromGrid,
			@RequestParam(value = "sampleRequired", required = false) String sampleRequired,
			HttpServletResponse response) {
		logger.debug(MODULE_NAME + " >> " + this.getClass().getName()
				+ " >> going to download sample attribute file for " + driverId);
		String sampleData = "";
		String fileName;
		if(BaseConstants.YES.equals(sampleRequired)){	
			fileName = BaseConstants.DISTRIBUTION_DB_DRIVER + BaseConstants.SAMPLE_ATTRIBUTE_CSV;
		} else {
			DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
			fileName = BaseConstants.ATTRIBUTEMAPPING + dateFormatter.format(new Date()) + ".csv";
		}
		response.setContentType(BaseConstants.APPLICATION_CSV);
		response.reset();
		String headerKey = BaseConstants.CONTENT_DISPOSITION;
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);

		String[] colNamesFromGridArray = colNamesFromGrid.split(",");
		List<DatabaseDistributionDriverAttribute> resultList = (List<DatabaseDistributionDriverAttribute>) this.driversService
				.getAttributeListByDriverId(Integer.parseInt(driverId));
		
		
		// check if mapping has no attribute then download dummy data
		if (resultList!=null && resultList.isEmpty() && BaseConstants.YES.equals(sampleRequired)) {
			sampleData=prepareDummyDistributionSampleDataString(colNamesFromGridArray,sampleData);
		} else {
			List<Map<String, String>> listOfAttrMap = new ArrayList<>();
			if (resultList != null) {
				
				for (DatabaseDistributionDriverAttribute attribute : resultList) {					
					HashMap<String, String> dbDriverAttrMap = new HashMap<>();
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.DATABASE_FIELD_NAME.getName(), attribute.getDatabaseFieldName());
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.UNIFIED_FIELD_NAME.getName(), attribute.getUnifiedFieldName());
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.DATA_TYPE.getName(), attribute.getDataType());
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.DEFAULT_VALUE.getName(), attribute.getDefualtValue());
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.ENABLE_PADDING.getName(), String.valueOf(attribute.isPaddingEnable()));	
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.PADDING_LENGTH.getName(), String.valueOf(attribute.getLength()));	
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.TYPE.getName(), String.valueOf(attribute.getPaddingType()));	
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.PADDING_CHARACTER.getName(), attribute.getPaddingChar());	
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.PREFIX.getName(), attribute.getPrefix());	
					dbDriverAttrMap.put(EnumDbDriverAttributeHeader.SUFFIX.getName(), attribute.getSuffix());
					listOfAttrMap.add(dbDriverAttrMap);
				}		
			}
			if(BaseConstants.YES.equals(sampleRequired) && listOfAttrMap.isEmpty()) {
				sampleData=prepareDummyDistributionSampleDataString(colNamesFromGridArray,sampleData);
			}
			for (Map<String, String> listMap : listOfAttrMap) {
				for (int i = 0; i < colNamesFromGridArray.length; i++) {
					if (i == colNamesFromGridArray.length - 1) {
						if (listMap.get(colNamesFromGridArray[i]) == null) {
						} else {
							sampleData += listMap.get(colNamesFromGridArray[i]);
						}
					} else {
						if (listMap.get(colNamesFromGridArray[i]) == null)
							sampleData += ",";
						else
							sampleData += listMap.get(colNamesFromGridArray[i]) + ",";
					}
				}
				sampleData += "\n";
			}
		}
		try {
			response.setCharacterEncoding(BaseConstants.UTF);
			ServletOutputStream outputStream = response.getOutputStream();
			logger.debug(MODULE_NAME + " >> " + "writing data into file");
			outputStream.write(colNamesFromGrid.getBytes());
			outputStream.println();
			outputStream.write(sampleData.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (Exception e) {
			logger.error(MODULE_NAME + " >> " + "error occured while writing data into file : ",e);
		}
		return null;
	}
}
