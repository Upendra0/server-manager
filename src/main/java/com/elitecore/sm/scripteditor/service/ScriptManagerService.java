/**
 * 
 */
package com.elitecore.sm.scripteditor.service;

import javax.servlet.ServletOutputStream;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.scripteditor.model.FileConfiguration;

/**
 * @author hiral.panchal
 *
 */
public interface ScriptManagerService {
	
	/**Fetch all active servers on page load*/
	public ResponseObject getServerList();
	
	/**Fetch all active files for the selected server */
	ResponseObject getFileList(long serverId);
	
	/**Returns the search result
	 * @param i 
	 * @param limit */
	public ResponseObject getSearchResult(long serverId, long fileId, int i, int limit);
	
	/**Open the file in editor*/
	public ResponseObject readFile(int fileId);
	
	/**Override the content of remote file*/
	public ResponseObject updateFile(FileConfiguration fileConfiguration);
	
	/**Execute the file if its executable
	 * @param fileConfiguration TODO*/
	public ResponseObject executeFile(FileConfiguration fileConfiguration);

	public int getSearchResultCount(int serverId, int fileId);

	public ResponseObject downloadLog(long fileId, ServletOutputStream outputStream);

}