package com.elitecore.sm.fileseqmgmt.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.pathlist.model.FileSequenceMgmt;

public interface FileSequenceMgmtDao extends GenericDAO<FileSequenceMgmt>{

	public boolean validateDuplicateDeviceName(FileSequenceMgmt fileSeqMgmt);
	public boolean validateDuplicateDeviceNameByReferenceDevice(FileSequenceMgmt fileSeqMgmt);
    public boolean updateStatusById(int fSeqId, StateEnum status, String referenceDeviceName);
}
