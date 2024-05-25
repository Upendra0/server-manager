/**
 * 
 */
package com.elitecore.sm.scripteditor.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.scripteditor.model.FileConfiguration;

/**
 * @author hiral.panchal
 *
 */
public interface FileConfigurationDao extends GenericDAO<FileConfiguration>{

	List<FileConfiguration> getAllActiveFilesForServer(long serverId);

	List<FileConfiguration> getFileDetails(long serverId,long fileId);

	int getSearchResultCount(int serverId, int fileId);

	List<FileConfiguration> getPaginatedSearchResult(long serverId, long fileId, int startIndex, int limit);

}
