/**
 * 
 */
package com.elitecore.sm.scripteditor.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.scripteditor.model.FileConfiguration;

/**
 * @author hiral.panchal
 *
 */
@Repository(value = "FileConfigDAO")
public class FileConfigurationDaoImpl extends GenericDAOImpl<FileConfiguration> implements FileConfigurationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<FileConfiguration> getAllActiveFilesForServer(long serverId) {
		
		logger.debug("ServerConfigurationDaoImpl.getAllActiveFilesForServer() called.");
		
		Criteria criteria = getCurrentSession().createCriteria(FileConfiguration.class);
		criteria.add(Restrictions.eq("status",StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("tblmserverconfigmst.serverId",serverId));
		
		logger.debug("ServerConfigurationDaoImpl.getAllActiveFilesForServer() end.");
		
		return criteria.list();
	}

	@Override
	public List<FileConfiguration> getFileDetails(long serverId,long fileId) {
		
		logger.debug("ServerConfigurationDaoImpl.getFileDetails() called.");
		
		Criteria criteria = getCurrentSession().createCriteria(FileConfiguration.class);
		if(fileId > 0L){
			
			criteria.add(Restrictions.eq("fileId",fileId));
		}else{
			
			criteria.add(Restrictions.eq("tblmserverconfigmst.serverId",serverId));
		}
		
		logger.debug("ServerConfigurationDaoImpl.getFileDetails() end.");
//		criteria.setMaxResults(1);
		List<FileConfiguration> fileConfigList = (List<FileConfiguration>) criteria.list();
		logger.debug("fileConfign:"+fileConfigList.size());
		return fileConfigList;
	}

	@Override
	public int getSearchResultCount(int serverId, int fileId) {
		
		logger.info("[IN] FileConfigurationDaoImpl.getSearchResultCount() CALLED"+serverId+","+fileId);
		Criteria criteria = getCurrentSession().createCriteria(FileConfiguration.class);
		if(fileId > 0L){
			
			criteria.add(Restrictions.eq("fileId",new Long(fileId)));
		}else{
			
			criteria.add(Restrictions.eq("tblmserverconfigmst.serverId",new Long(serverId)));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@Override
	public List<FileConfiguration> getPaginatedSearchResult(long serverId, long fileId, int startIndex, int limit) {

		
		logger.debug("ServerConfigurationDaoImpl.getPaginatedSearchResult() called serverId:"+serverId+",fileId:"+fileId);
		
		Criteria criteria = getCurrentSession().createCriteria(FileConfiguration.class);
		if(fileId > 0L){
			
			criteria.add(Restrictions.eq("fileId",fileId));
		}else{
			
			criteria.add(Restrictions.eq("tblmserverconfigmst.serverId",serverId));
		}
		
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		
		List<FileConfiguration> fileConfigList = (List<FileConfiguration>) criteria.list();
		logger.debug("fileConfign:"+fileConfigList.size());
		return fileConfigList;
	}
}
