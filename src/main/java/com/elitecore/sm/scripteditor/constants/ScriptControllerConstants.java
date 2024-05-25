/**
 * 
 */
package com.elitecore.sm.scripteditor.constants;

/**
 * @author hiral.panchal
 *
 */
public interface ScriptControllerConstants {


	String INIT_SCRIPT_MANAGER = "initScriptManager";
	String SEARCH_SCRIPT_MANAGER = "searchServerFiles";
	String FILE_LIST_FOR_SERVER = "getFileList";
	String EDIT_FILE = "openfileineditor";
	String UPDATE_FILE = "savefile";
	String EXECUTE_FILE = "executeFile";
	String DOWNLOAD_FILE = "downloadFile";
	
	/**File Types Constants*/
	
	int FILE_EXECUTABLE_ONLY = 1;
	int FILE_EXECUTABLE_EDITABLE_BOTH = 2;
	int FILE_EDITABLE_ONLY = 3;
	int FILE_READ_ONLY = 4;
	
}
