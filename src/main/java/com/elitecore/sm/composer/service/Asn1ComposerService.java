package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;

/**
 * The Interface Asn1ComposerService.
 *
 * @author sagar.r.patel
 */
public interface Asn1ComposerService {
	
	/**
	 * Gets the asn1 composer mapping by id.
	 *
	 * @param asn1ComposerMappingId the asn1 composer mapping id
	 * @return the asn1 composer mapping by id
	 */
	public ResponseObject getAsn1ComposerMappingById(int asn1ComposerMappingId);
	
	/**
	 * Update asn1 composer mapping.
	 *
	 * @param asn1ComposerMapping the asn1 composer mapping
	 * @return the response object
	 */
	public ResponseObject updateASN1ComposerMapping(ASN1ComposerMapping asn1ComposerMapping);

}
