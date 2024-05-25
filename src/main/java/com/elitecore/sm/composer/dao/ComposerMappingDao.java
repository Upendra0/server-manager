/**
 * 
 */
package com.elitecore.sm.composer.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.composer.model.NRTRDEComposerMapping;
import com.elitecore.sm.composer.model.RAPComposerMapping;
import com.elitecore.sm.composer.model.TAPComposerMapping;
import com.elitecore.sm.composer.model.XMLComposerMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ComposerMappingDao extends GenericDAO<ComposerMapping> {

	public List<ComposerMapping>  getAllMappingBydeviceAndComposerType(int deviceId, int pluginMasterId);
	
	public int getMappingCount(String name);
	
	public ASCIIComposerMapping getAsciiComposerMappingById(int composerMappingId);
	
	public ASN1ComposerMapping getAsn1ComposerMappingById(int composerMappingId);
	
	public TAPComposerMapping getTapComposerMappingById(int composerMappingId);
	
	public RAPComposerMapping getRapComposerMappingById(int composerMappingId);
	
	public NRTRDEComposerMapping getNrtrdeComposerMappingById(int composerMappingId);

	public List<Composer> getMappingAssociationDetails(int mappingId);
	
	public void iterateOverComposerMapping(ComposerMapping composerMapping);
	
	public ComposerMapping getComposerMappingDetailsByNameAndType(String name, int pluginTypeId);
	
	public XMLComposerMapping getXMLComposerMappingById(int composerMappingId);
	
	public FixedLengthASCIIComposerMapping getFixedLengthAsciiComposerMappingById(int fixedLengthAsciiComposerMappingId);
	
	public List<ComposerMapping> getAllMappingById(Integer[] ids);
	
	public List<ComposerMapping> getAllMappingByDeviceId(Integer[] deviceIds);
		
}
