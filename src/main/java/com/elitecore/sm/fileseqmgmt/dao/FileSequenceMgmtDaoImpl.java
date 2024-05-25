package com.elitecore.sm.fileseqmgmt.dao;

import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.FileSequenceMgmt;
import com.elitecore.sm.util.EliteUtils;

@org.springframework.stereotype.Service(value = "fileSequenceMgmtDao")
public class FileSequenceMgmtDaoImpl extends GenericDAOImpl<FileSequenceMgmt> implements FileSequenceMgmtDao {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public boolean validateDuplicateDeviceName(FileSequenceMgmt fileSeqMgmt) {
		Criteria criteria = getCurrentSession().createCriteria(FileSequenceMgmt.class);
		criteria.add(Restrictions.ne("id", fileSeqMgmt.getId()));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		if (fileSeqMgmt.getReferenceDevice() != null) {
			criteria.add(Restrictions.eq("referenceDevice", fileSeqMgmt.getReferenceDevice()));
		}

		return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue() > 0) ? true : false;
	}

	@Override
	public boolean validateDuplicateDeviceNameByReferenceDevice(FileSequenceMgmt fileSeqMgmt) {
		if (fileSeqMgmt != null) {
			Criteria criteria = getCurrentSession().createCriteria(FileSequenceMgmt.class);
			criteria.add(Restrictions.eq("referenceDevice", fileSeqMgmt.getReferenceDevice()));
			criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
			return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue() > 0) ? true
					: false;
		} else {
			return false;
		}
	}

	// update status by ID
	// For updating and renamigg reference device name and status
	@Override
	public boolean updateStatusById(int fSeqId, StateEnum status,String referenceDeviceName) {
		StringBuilder queryStr = new StringBuilder();
		Query query = null;
		String deletedReferenceDeviceName = "";
		try {
			logger.debug("updating File Sequence Tracking with [id=" + fSeqId + ", status=" + status.getValue() + "].");

			queryStr.append("UPDATE TBLTFILESEQMGMT SET STATUS=:status, LASTUPDATEDDATE=:lastupdateddate");

			if (status == StateEnum.DELETED) {
				deletedReferenceDeviceName = EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,referenceDeviceName);
				System.out.println("deletedReferenceDeviceName" + deletedReferenceDeviceName);
				queryStr.append(",REFERENCEDEVICE=:referencedevice");
			}
			queryStr.append(" WHERE ID=:id");
			query = getCurrentSession().createSQLQuery(queryStr.toString());
			query.setParameter("status", status.getValue());
			query.setParameter("id", fSeqId);
			query.setParameter("lastupdateddate", new Date());
			
			if(status == StateEnum.DELETED) {
				query.setParameter("referencedevice", deletedReferenceDeviceName);
			}
			if (query.executeUpdate() == 1) {
				logger.debug(
						"File Sequence Tracking status for id= [" + fSeqId + "] moved to [" + status.getValue() + "].");
				return true;
			}
		} catch (Exception ex) {
			logger.trace(ex);
		}
		logger.error("Unable to File sequence tracking status for [id=" + fSeqId + ", status=" + status.getValue() + "].");
		return false;
	}
	
	
// UPDATE status by ID Hibernate IMPLEMENTATION - REFERENCE
//		@Override
//		public boolean updateStatusById(int fSeqId, StateEnum status) {
//			FileSequenceMgmt fSeqMgmt;
//			try {
//				logger.debug("updating File Sequence Tracking with [id=" + fSeqId + ", status=" + status.getValue() + "].");
//				getCurrentSession().beginTransaction();
//				fSeqMgmt = (FileSequenceMgmt) getCurrentSession().get(FileSequenceMgmt.class, fSeqId);
//				logger.debug("File OBJ:  " + fSeqMgmt);
//				if (fSeqMgmt != null) {
//					fSeqMgmt.setStatus(status);
//					getCurrentSession().update(fSeqMgmt);
//					getCurrentSession().getTransaction().commit();
//					return true;
//				}
//			} catch (Exception ex) {
//				logger.trace(ex);
//				// System.out.println(ex.getStackTrace());
//			}
//			return false;
	//
//		}
}
