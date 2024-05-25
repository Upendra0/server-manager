package com.elitecore.sm.drivers.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.common.model.SequenceManagement;


public interface SequenceMgmtDao extends GenericDAO<SequenceManagement> {

	public SequenceManagement getControlFileSequenceBySequenceId(int sequenceId);
}
