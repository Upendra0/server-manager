/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.HtmlParserMapping;
import com.elitecore.sm.parser.model.JsonParserMapping;
import com.elitecore.sm.parser.model.MTSiemensParserMapping;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.XMLParserMapping;
import com.elitecore.sm.parser.model.XlsParserMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserMappingDao extends GenericDAO<ParserMapping> {

	public Map<String, Object>  getDeviceMappingBySearchParameters(SearchDeviceMapping device);
	
	public List<Parser> getMappingAssociationDetails(int mappingId);	
	
	public List<ParserMapping> getAllMappingById(Integer [] ids);
	
	public List<ParserMapping> getAllMappingByDeviceId(Integer [] deviceIds);
	
	public RegexParserMapping getRegExParserMappingById(int parserMappingId);
	
	public List<ParserMapping> getAllMappingBydeviceAndType(int deviceId, int pluginMasterId);
	
	public int getMappingCount(String name);
	
	public AsciiParserMapping getAsciiParserMappingById(int parserMappingId);
	
	public DetailLocalParserMapping getDetailLocalParserMappingById(int parserMappingId);
	
	public VarLengthAsciiParserMapping getVarLengthAsciiParserMappingById(int parserMappingId);
	
	public VarLengthBinaryParserMapping getVarLengthBinaryParserMappingById(int parserMappingId);
	
	public ASN1ParserMapping getAsn1ParserMappingById(int parserMappingId);
	
	public XMLParserMapping getXMLParserMappingById(int parserMappingId);
	
	public HtmlParserMapping getHtmlParserMappingById(int parserMappingId);
	
	public void iterateOverParserMapping(ParserMapping parserMapping);
	
	public ParserMapping getMappingDetailsByNameAndType(String name, int pluginTypeId);
	
	public List<Object[]> getAllMappingByDeviceTypeAndParserType(List<Integer> deviceIds, String plugInType);
	
	public FixedLengthASCIIParserMapping getFixedLengthASCIIParserMappingById(int fixedLengthASCIIParserMappingId);
	
	public RapParserMapping getRapParserMappingById(int rapParserMappingId);
	
	public TapParserMapping getTapParserMappingById(int tapParserMappingById);
	
	public NRTRDEParserMapping getNRTRDEParserMappingById(int nrtrdeParserMappingId);
	
	public FixedLengthBinaryParserMapping getFixedLengthBinaryParserMappingById(int fixedLengthBinaryParserMappingId );
	
	public PDFParserMapping getPDFParserMappingById(int pdfParserMappingId );

	public XlsParserMapping getXlsParserMappingById(int parserMappingId);
	
	public List<ParserMapping>  getUserDefinedMappingIds();
	
	public JsonParserMapping getJsonParserMappingById(int parserMappingId);
	
	public MTSiemensParserMapping getMTSiemensParserMappingById(int parserMappingId);
}
