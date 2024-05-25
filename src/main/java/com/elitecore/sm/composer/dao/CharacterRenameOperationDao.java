package com.elitecore.sm.composer.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.pathlist.model.CharRenameOperation;

/**
 * @author Ranjitsinh Reval
 *
 */

public interface CharacterRenameOperationDao extends GenericDAO<CharRenameOperation> {
	
	public List<CharRenameOperation> getAllRenameOperationsById(int id);
	
	public void iterateOverCharRenameOperation(CharRenameOperation charRenameOperation);
	
	public void saveCharRenameOperation(CharRenameOperation charRenameOperation);
	
	public List<CharRenameOperation> getAllRenameOperationsBySvcFileRenConfigId(int svcFileRenConfigId);

	public void mergeCharOperationAgent(CharRenameOperation charRenameOperation);
	
	public long getSeqNumberCountByPluginId(int sequenceNumber, int pluginId, int id);
	
	public long getSeqNumberCountByIdForFileRenameAgent(int sequenceNumber, int svcFileRenConfigId, int id);
	
	public int getMaxSequenceNoForPlugin(int pluginId);
	
	public long getSeqNumberCountByCollectionServicePathListId(int sequenceNumber, int pathListId, int id);
	
	public List<CharRenameOperation> getAllRenameOperationsByPathListId(int id);

	public int getMaxSequenceNoForCollectionPathListId(int id);
	
}
