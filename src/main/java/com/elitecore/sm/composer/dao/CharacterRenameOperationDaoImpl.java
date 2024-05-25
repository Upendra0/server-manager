/**
 * 
 */
package com.elitecore.sm.composer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.agent.dao.ServiceFileRenameConfigDao;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.pathlist.dao.PathListDao;
import com.elitecore.sm.pathlist.model.CharRenameOperation;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="charRenameOperationDao")
public class CharacterRenameOperationDaoImpl extends GenericDAOImpl<CharRenameOperation> implements CharacterRenameOperationDao {

	
	@Autowired
	ComposerDao composerDao;
	
	@Autowired
	ServiceFileRenameConfigDao serviceFileRenameConfigDao;
	
	@Autowired
	PathListDao pathListDao;
	
	/**
	 * Method will get all Rename operation by composer plug in id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CharRenameOperation> getAllRenameOperationsById(int pluginId) {
		logger.info(">> getAllRenameOperationsById ");
		Criteria criteria=getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("composer.id", pluginId));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllRenameOperationsById ");
		return criteria.list();
	}

	/**
	 * Method will get all Rename operation by ServiceFileRenameConfig in id
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CharRenameOperation> getAllRenameOperationsBySvcFileRenConfigId(int svcFileRenConfigId) {
		logger.info(">> getAllRenameOperationsById ");
		Criteria criteria=getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("svcFileRenConfig.id", svcFileRenConfigId));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllRenameOperationsById ");
		return criteria.list();
	}
	
	@Override
	public long getSeqNumberCountByPluginId(int sequenceNumber, int pluginId, int id) {
		Criteria criteria = getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("composer.id", pluginId));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNumber));
		if(id > 0) {
			criteria.add(Restrictions.ne("id", id));
		}
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public int getMaxSequenceNoForPlugin(int pluginId) {
		Criteria criteria = getCurrentSession().createCriteria(CharRenameOperation.class);
		if(pluginId > 0) {
			criteria.add(Restrictions.eq("composer.id", pluginId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("sequenceNo"));
		Object result = criteria.uniqueResult();
		if(result != null) {
			return (int) result;
		}
		return 0;
	}
	
	@Override
	public long getSeqNumberCountByIdForFileRenameAgent(int sequenceNumber, int svcFileRenConfigId, int id) {
		Criteria criteria = getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("svcFileRenConfig.id", svcFileRenConfigId));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNumber));
		if(id > 0) {
			criteria.add(Restrictions.ne("id", id));
		}
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	/**
	 * Method will call composer for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void update (CharRenameOperation charRenameOperation){
		if(charRenameOperation.getPathList() == null)
			composerDao.merge(charRenameOperation.getComposer());
		getCurrentSession().merge(charRenameOperation);//NOSONAR
		
	}
	
	/**
	 * Method will call composer for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void save(CharRenameOperation charRenameOperation){
		if(charRenameOperation.getPathList() == null)
			composerDao.merge(charRenameOperation.getComposer());
		getCurrentSession().save(charRenameOperation);
	}

	/**
	 * Method will call composer for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void merge(CharRenameOperation charRenameOperation){
		if(charRenameOperation.getPathList() == null)
			composerDao.merge(charRenameOperation.getComposer());
		getCurrentSession().merge(charRenameOperation);//NOSONAR
	}
	
	/**
	 * Fetch Char rename Operation dependents
	 * @param charRenameOperation
	 */
	@Override
	public void iterateOverCharRenameOperation(CharRenameOperation charRenameOperation){
		
		if(charRenameOperation !=null){
			Hibernate.initialize(charRenameOperation.getSvcFileRenConfig());	
		}
	}
	
	/**
	 * Method will call serviceFileRenameConfig for making super entity dirty. 
	 * 
	 */
	@Override
	public void saveCharRenameOperation(CharRenameOperation charRenameOperation){
		serviceFileRenameConfigDao.merge(charRenameOperation.getSvcFileRenConfig(),charRenameOperation.getSvcFileRenConfig().getService().getServerInstance());
		getCurrentSession().save(charRenameOperation);
	}
	
	/**
	 * Method will call serviceFileRenameConfig for making super entity dirty. 
	 * 
	 */
	@Override
	public void mergeCharOperationAgent(CharRenameOperation charRenameOperation){
		serviceFileRenameConfigDao.merge(charRenameOperation.getSvcFileRenConfig(),charRenameOperation.getSvcFileRenConfig().getService().getServerInstance());
		getCurrentSession().merge(charRenameOperation);
	}

	@Override
	public long getSeqNumberCountByCollectionServicePathListId(int sequenceNumber, int pathListId, int id) {
		Criteria criteria = getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("pathList.id", pathListId));
		criteria.add(Restrictions.eq("sequenceNo", sequenceNumber));
		if(id > 0) {
			criteria.add(Restrictions.ne("id", id));
		}
		criteria.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CharRenameOperation> getAllRenameOperationsByPathListId(int pathListId) {
		logger.info(">> getAllRenameOperationsById ");
		Criteria criteria=getCurrentSession().createCriteria(CharRenameOperation.class);
		criteria.add(Restrictions.eq("pathList.id", pathListId));
		criteria.add(Restrictions.eq("status", StateEnum.ACTIVE));
		criteria.addOrder(Order.asc("id"));
		logger.info("<< getAllRenameOperationsById ");
		return criteria.list();
	}

	@Override
	public int getMaxSequenceNoForCollectionPathListId(int pathId) {

		Criteria criteria = getCurrentSession().createCriteria(CharRenameOperation.class);
		if(pathId > 0) {
			criteria.add(Restrictions.eq("pathList.id", pathId));
		}
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("sequenceNo"));
		Object result = criteria.uniqueResult();
		if(result != null) {
			return (int) result;
		}
		return 0;	
	}
}
