package com.elitecore.sm.productconfig.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.productconfig.model.ProfileEntity;

/**
 * 
 * @author avani.panchal
 *
 */
@Repository(value="productConfigurationDao")
public class ProductConfigurationDaoImpl extends GenericDAOImpl<ProfileEntity> implements ProductConfigurationDao{
	
	/**
	 * Find Profile Detail using server type id
	 * @param serverTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ProfileEntity> findProfileDetailByServerTypeId(int serverTypeId,int licenseId,boolean isdefault){
		
		logger.debug("Inside findProfileDetailByServerTypeId for Server Type " +serverTypeId );
		Criteria criteria=getCurrentSession().createCriteria(ProfileEntity.class);
		criteria.add(Restrictions.eq("serverType.id", serverTypeId));
		if(licenseId!=-1)
			criteria.add(Restrictions.eq("license.id", licenseId));
		criteria.add(Restrictions.eq("isDefault", isdefault));
		criteria.addOrder(Order.asc("id"));
		
		return criteria.list();
	}
	
	/**
	 * Fetch Custom profile entity by id and server type
	 * @param id
	 * @param serverTypeId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ProfileEntity findProfileEntityByIdAndServerType(int id , int serverTypeId){
		
		logger.debug("Inside findProfileEntityByIdAndServerType , entity Id :  "+ id+" serverTypeId: "+ serverTypeId);
		Criteria criteria=getCurrentSession().createCriteria(ProfileEntity.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("serverType.id", serverTypeId ));
		criteria.add(Restrictions.eq("isDefault", false));
		
		List<ProfileEntity> profileEntityList=criteria.list();
		return (profileEntityList!=null && !profileEntityList.isEmpty())?profileEntityList.get(0):null;
		
	}
	
	@Override
	public List<ProfileEntity> findProfileDetailByServerTypeId(int serverTypeId,String entityType,String entityAlias){
		
		logger.debug("Inside findProfileDetailByServerTypeId for Server Type " +serverTypeId );
		Criteria criteria=getCurrentSession().createCriteria(ProfileEntity.class);
		criteria.add(Restrictions.eq("serverType.id", serverTypeId));
		criteria.add(Restrictions.eq("entityType", entityType));
		criteria.add(Restrictions.eq("entityAlias", entityAlias));
		criteria.addOrder(Order.asc("id"));
		
		return criteria.list();
	}

	@Override
	public ProfileEntity findCustomProfileEntity(int serverTypeId, String entityAlias, boolean isDefault) {
		
		logger.debug("Inside findCustomProfileEntity for Server Type " +serverTypeId );
		Criteria criteria=getCurrentSession().createCriteria(ProfileEntity.class);
		criteria.add(Restrictions.eq("serverType.id", serverTypeId));
		criteria.add(Restrictions.eq("entityAlias", entityAlias));
		criteria.add(Restrictions.eq("isDefault", isDefault));
		criteria.add(Restrictions.isNull("license.id"));
		List<ProfileEntity> profileEntityList=criteria.list();
		return (profileEntityList!=null && !profileEntityList.isEmpty())?profileEntityList.get(0):null;
	}
	
}
