/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import java.sql.Blob;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.systemparam.model.ImageModel;

/**
 * @author vandana.awatramani
 *
 */
@Repository(value = "imageModelDao")
public class ImageModelDaoImpl extends GenericDAOImpl<ImageModel> implements
		ImageModelDao {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Blob getCustLogoImageAsBlob(int imageId) throws SMException{
		Blob custLogo = null;
		Criteria criteria = getCurrentSession().createCriteria(ImageModel.class);
		criteria.add(Restrictions.eq("id",imageId));
		criteria.setProjection(Projections.property("image"));
		List list = criteria.list();
		if(list!=null){
			Iterator iterator = list.iterator();
			while(iterator.hasNext()){
				custLogo = (Blob)iterator.next();
				//custLogo.getBinaryStream();
			}
		}
		return custLogo;
	}
	
}
