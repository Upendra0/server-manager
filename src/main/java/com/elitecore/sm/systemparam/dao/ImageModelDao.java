/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import java.sql.Blob;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.systemparam.model.ImageModel;

/**
 * @author vandana.awatramani
 *
 */
public interface ImageModelDao extends GenericDAO<ImageModel> {

	public Blob getCustLogoImageAsBlob(int imageId) throws SMException;	
}
