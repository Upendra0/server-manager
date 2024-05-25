/**
 * 
 */
package com.elitecore.sm.parser.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.XMLParserMapping;
import com.elitecore.sm.parser.model.XlsParserMapping;
import com.elitecore.sm.pathlist.model.ParsingPathList;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserMappingService {

	public ResponseObject getAllMappingById(Integer[] ids);
	
	public ResponseObject getAllMappingByDeviceId(Integer[] deviceIds);

	public ResponseObject deleteMapping(ParserMapping parserMapping, int staffId);

	public ResponseObject getMappingByDeviceAndParserType(int deviceId, String parserType);

	public ResponseObject getMappingDetailsById(int mappingId, String pluginType);

	public ResponseObject updateAndAssociateParserMapping(ParserMapping parserMapping, String parserType, int plugInId, String actionType, int staffId);

	public ResponseObject createOrUpdateParserMappingDetails(ParserMapping parserMapping, int mappingId, int staffId, String actionType,
			String parserType, int pluginId);

	public ResponseObject createNewMapping(ParserMapping parserMapping, int staffId, int pluginId);

	public void validateImportedMappingDetails(ParserMapping parserMapping, List<ImportValidationErrors> importErrorList);

	public ResponseObject importParserMappingAndDependents(Parser parser, boolean isImport, int importMode);

	public ResponseObject getMappingDetailsByNameAndType(String name, int pluginTypeId);

	public ResponseObject getMappingListByDeviceTypeAndParserType(int deviceId, String pluginType);

	public ResponseObject setAndUpdateNetflowParserDetail(NATFlowParserMapping newNatflowParserMapping, NATFlowParserMapping dbNatflowParserMapping);

	public ResponseObject setAndUpdateAsciiParserDetail(AsciiParserMapping newasciiParserMapping, AsciiParserMapping dbasciiParserMapping);
	
	public ResponseObject setAndUpdateVarLengthAsciiParserDetail(VarLengthAsciiParserMapping newVarLengthAsciiParserMapping, VarLengthAsciiParserMapping dbVarLengthAsciiParserMapping);

	public ParserMapping getParserMappingById(int parserMappingId);

	public ResponseObject getMappingAssociationDetails(int mappingId, String decodeType);
	
	public ResponseObject setAndUpdateFixedLengthASCIIParserDetail(FixedLengthASCIIParserMapping newFixedLengthASCIIParser,
			FixedLengthASCIIParserMapping dbFixedLengthASCIIParser);

	public ResponseObject setAndUpdateXMLParserDetail(XMLParserMapping newxmlParserMapping, XMLParserMapping dbxmlParserMapping);
	
	public void importParserMappingForUpdateMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping);
	
	public void importParserMappingForAddAndKeepBothMode(Parser exportedParser, ParsingPathList pathList, int importMode);
	
	public ResponseObject setAndUpdateRapParserDetail(RapParserMapping newrapParserMapping,RapParserMapping dbrapParserMapping);
	
	public ResponseObject setAndUpdateTapParserDetail(TapParserMapping newtapParserMapping,TapParserMapping dbtapParserMapping);

	public ResponseObject setAndUpdateNrtrdeParserDetail(NRTRDEParserMapping newnrtrdeapParserMapping,NRTRDEParserMapping dbnrtrdeapParserMapping);
	
	public ResponseObject setAndUpdateFixedLengthBinaryParserDetail(FixedLengthBinaryParserMapping newfixedLengthBinaryParserMapping , FixedLengthBinaryParserMapping dbfixedLengthBinaryParserMapping );
	
	public ResponseObject setAndUpdatePDFParserDetail(PDFParserMapping newPDFParserMapping , PDFParserMapping dbPDFParserMapping );

	public ParserAttribute getParserAttributeFromList(List<ParserAttribute> parserAttributeList, String sourceField, String unifiedField, boolean associatedFlag);
	
	public void importParserMappingForAddMode(ParserMapping dbParserMapping, ParserMapping exportedParserMapping);
	
	public ResponseObject setAndUpdateHtmlParserDetail(HtmlParserMapping newhtmlParserMapping, HtmlParserMapping dbhtmlParserMapping);
	
	public ResponseObject setAndUpdateXlsParserDetail(XlsParserMapping newXlsParserMapping, XlsParserMapping dbXlsParserMapping);
	
	public void updateParserMappingBasicParameters(ParserMapping dbParserMapping, ParserMapping exportedParserMapping);
	
	public ResponseObject setAndUpdateVarLengthBinaryParserDetail(VarLengthBinaryParserMapping newVarLengthBinaryParserMapping, VarLengthBinaryParserMapping dbVarLengthBinaryParserMapping);
	
	public ResponseObject setAndUpdateDetailLocalParserDetail(DetailLocalParserMapping newdetailLocalParserMapping,
			DetailLocalParserMapping dbdetailLocalParserMapping);


}
