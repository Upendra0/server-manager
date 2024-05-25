/**
 * 
 */
package com.elitecore.sm.composer.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.composer.model.Composer;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface ComposerDao extends GenericDAO<Composer> {

	public List<Composer> getComposerByName(String name);

	public Composer getComposerByNameForUpdate(String name);

	public List<Composer> getMappingAssociationDetails(int mappingId);

	public List<Composer>  getComposerListByPathListId(int id);

}
