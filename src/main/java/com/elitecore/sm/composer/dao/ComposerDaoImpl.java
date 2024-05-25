/**
 * 
 */
package com.elitecore.sm.composer.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.pathlist.dao.PathListDao;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="composerDao")
public class ComposerDaoImpl extends GenericDAOImpl<Composer> implements ComposerDao {

	@Autowired
	PathListDao pathListDao;
	
	/**
	 * Method will get composer by composer name
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Composer> getComposerByName(String name) {
		logger.debug("Fetching composer by name.");
		Criteria criteria = getCurrentSession().createCriteria(Composer.class);
		criteria.add(Restrictions.eq("name", name).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		return (List<Composer>)criteria.list();
	}
	
	/**
	 * Method will get composer object by composer name.
	 * @param name
	 * @return
	 */
	@Override
	public Composer getComposerByNameForUpdate(String name) {
		logger.debug("Fetching composer details by composer name  " + name);
		Criteria criteria = getCurrentSession().createCriteria(Composer.class);
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		if (name != null) {
			criteria.add(Restrictions.eq("name", name).ignoreCase());
		} 
		@SuppressWarnings("unchecked")
		List<Composer> composerList = criteria.list(); 
		if(composerList != null && !composerList.isEmpty()){
			logger.info("Found composer object for name : " + name);
			return composerList.get(0);
		}else{
			logger.info("Found unique composer : " + name);
			return null;
		}
	}
	
	/**
	 * Method will call path-list for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void update (Composer composer){
		pathListDao.update(composer.getMyDistDrvPathlist());
		getCurrentSession().update(composer);
		
	}
	
	/**
	 * Method will call path-list for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void save(Composer composer){
		pathListDao.save(composer.getMyDistDrvPathlist());
		getCurrentSession().save(composer);
	}
	
	/**
	 * Method will call path-list for making super entity dirty. 
	 * @param composer
	 */
	@Override
	public void merge(Composer composer){
		pathListDao.merge(composer.getMyDistDrvPathlist());
		getCurrentSession().merge(composer);
	}
	
	/**
	 * Method will fetch all mapping association details for selected mapping.
	 * @param mappingId
	 * @return List<Parser>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Composer> getMappingAssociationDetails(int mappingId) {
		logger.debug(">> getMappingAssociationDetails : mappingId : " + mappingId);
		
		Criteria criteria=getCurrentSession().createCriteria(Composer.class);
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("composerMapping.id", mappingId));
		
		logger.debug("<< getMappingAssociationDetails ");
		return criteria.list();
	}

	/**
	 * Get composer list by pathlist id
	 * 
	 * @param name
	 * @return composer
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Composer> getComposerListByPathListId(int pathListId) {

		Criteria criteria = getCurrentSession().createCriteria(Composer.class);
		criteria.add(Restrictions.eq("myDistDrvPathlist.id", pathListId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		return (List<Composer>) criteria.list();
	}

}
