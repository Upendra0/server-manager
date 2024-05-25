/**
 * 
 */
package com.elitecore.sm.composer.service;

import java.util.List;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.PathList;

/**
 * @author Ranjitsinh Reval
 *
 */
public interface CharRenameOperationService {

	public ResponseObject addCharRenameOperationParams(CharRenameOperation charRenameOperation);
	
	public ResponseObject updateCharRenameOperationParams(CharRenameOperation charRenameOperation);
	
	public ResponseObject getCharRenameParamsById(int pluginId);
	
	public ResponseObject getAllRenameOperationsBySvcFileRenConfigId(int pluginId);	
	
	public ResponseObject deleteCharRenameOperationParams(int id, boolean isAgent);
	
	public void validateCharRenameOperationParameters(CharRenameOperation charRenameOperation,  List<ImportValidationErrors> importErrorList);
	
	public ResponseObject addCharRenameOperationToFileRenameAgent(CharRenameOperation charRenameOperation); 

	public ResponseObject updateCharRenameOperationParamsForFileRenameAgent(CharRenameOperation charRenameOperation);
	
	public long getSeqNumberCountByPluginId(int sequenceNumber,int pluginId, int id);
	
	public long getSeqNumberCountByIdForFileRenameAgent(int sequenceNumber,int svcFileRenConfigId, int id);
	
	public List<CharRenameOperation> importCharacterRenameOperationUpdateMode(Composer dbComposer, Composer exportedComposer);
	
	public ResponseObject importOrDeleteComposerCharRenameOperation(Composer composer, boolean isImport);
	
	public long getSeqNumberCountByCollectionServicePathListId(int sequenceNumber,int pathListId, int id);
	
	public ResponseObject getCollectionCharRenameParamsById(int pathListId);

	public ResponseObject importOrDeleteCharRenameOperation(PathList pathList, boolean b);

	public List<CharRenameOperation> importCharacterRenameOperationUpdateMode(PathList dbPath, PathList exportedPath);
	
	public List<CharRenameOperation> importCharacterRenameOperationAddMode(PathList dbPath, PathList exportedPath);
	
	public List<CharRenameOperation> importCharacterRenameOperationAddMode(Composer dbComposer, Composer exportedComposer);
	
}
