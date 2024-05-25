package com.elitecore.sm.parser.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.NRTRDEParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.RAPATTRTYPE;
import com.elitecore.sm.parser.model.RAPParserAttribute;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.TAPATTRTYPE;
import com.elitecore.sm.parser.model.TAPParserAttribute;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ParserAttributeDao extends GenericDAO<ParserAttribute> {

	public List<ParserAttribute> getAttributeListByMappingId(int mappingId);
	
	public List<ParserAttribute> getAsn1AttributeListByMappingId(int mappingId,ASN1ATTRTYPE asn1attrtype);
	
	public Map<String, Object> getAttributeConditionList(int mappingId);
	
	public int getAttributeCountByName(int mappingId, String sourceField, String unifiedField, int attributeId);
	
	public int getAttributeCountByIPPortName(int mappingId, String unifiedField, int attributeId);
	
	public List<RegexParserAttribute> getRegExAttributeByPatternId(int regExPatternId);
	
	public ParserAttribute checkUniqueAttributeNameForUpdate(int mappingId, String unifiedField,String name,String pluginType); // MED-8349
	
	public List<ParserAttribute> getVarLengthBinaryAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	public ASN1ParserAttribute getASN1ParserAttributeById(int attributeId);

	public Map<String, Object> getASN1AttributeConditionList(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	public RAPParserAttribute getRAPParserAttributeById(int attributeId);
	
	public List<ParserAttribute>  getRapAttributeListByMappingId(int mapping,RAPATTRTYPE rapattrtype);
	
	public TAPParserAttribute getTAPParserAttributeById(int attributeId);
	
	public List<ParserAttribute>  getTapAttributeListByMappingId(int mapping,TAPATTRTYPE tapattrtype);
	
	public NRTRDEParserAttribute getNrtrdeParserAttributeById(int attributeId);
	
	public List<ParserAttribute>  getNrtrdeAttributeListByMappingId(int mapping,ASN1ATTRTYPE asn1attrtype);
	
	public ParserAttribute getParserAttributeByAttributeId(int attributeId, int mappingId);
	
	public List<ParserAttribute> getParserAttributeListByGroupId(int gropuId, int mappingId);
	
	public int getAttributeCountByNameForRegex(int mappingId, String sourceField, String unifiedField, int attributeId);
	
	public int updateAsciiParserTypeByMappingId(int mappingId);
	
	public List<RegexParserAttribute> getRegExAttributeByPatternIdAndUnifiedField(int regExPatternId,String unifiedField);

}
