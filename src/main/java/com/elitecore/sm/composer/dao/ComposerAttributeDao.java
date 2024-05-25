package com.elitecore.sm.composer.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.parser.model.ASN1ATTRTYPE;


/**
 * 
 * @author avani.panchal
 *
 */
public interface ComposerAttributeDao extends GenericDAO<ComposerAttribute> {

	public Map<String, Object> getAttributeConditionList(int mappingId);

	public int getAttrSeqNoByMappingId(int mappingId);

	public List<ComposerAttribute> getAllAttributeByMappingId(int mappingId);

	public List<ComposerAttribute> getAttributeListByMappingId(int mappingId);
	
	public ComposerAttribute checkUniqueAttributeNameForUpdate(int mappingId, String name);

	List<ComposerAttribute> getAsn1AttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	List<ComposerAttribute> getTapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	List<ComposerAttribute> getRapAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);
	
	List<ComposerAttribute> getNrtrdeAttributeListByMappingId(int mappingId, ASN1ATTRTYPE asn1attrtype);

	List<Integer> getAllAttributeSeqNumberByMappingId(int mappingId, int composerId);

	public Map<String, Object> getASN1AttributeConditionList(int mappingId, ASN1ATTRTYPE asn1attrtype);

	public List<Integer> getAllASN1AttributeSeqNumberByMappingId(ComposerAttribute composerAttr);

	public ComposerAttribute getComposerAttributeByAttributeId(int attributeId, int mappingId);

	public List<ComposerAttribute> getComposerAttributeListByGroupId(int gropuId, int mappingId);


}
