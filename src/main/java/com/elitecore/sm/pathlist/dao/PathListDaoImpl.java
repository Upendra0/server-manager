
package com.elitecore.sm.pathlist.dao;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.dao.CharacterRenameOperationDao;
import com.elitecore.sm.composer.dao.ComposerMappingDao;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.drivers.dao.DriversDao;
import com.elitecore.sm.license.model.License;
import com.elitecore.sm.parser.dao.ParserMappingDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.services.dao.ServicesDao;

@Repository(value = "pathListDao")
public class PathListDaoImpl extends GenericDAOImpl<PathList> implements PathListDao {
	
	@Autowired
	DriversDao driversDao;
	
	@Autowired
	ServicesDao servicesDao;
	
	@Autowired
	ParserMappingDao parserMappingDao;
	
	@Autowired 
	ComposerMappingDao composerMappingDao;
	
	@Autowired
	CharacterRenameOperationDao charRenameOperationDao;
	
	/**
	 * MarkDriver ,  service and server instance dirty , then update pathList
	 */
	@Override
	public void update (PathList pathList){
		
		if(pathList instanceof CollectionDriverPathList){
			driversDao.merge(pathList.getDriver());
		} else if(pathList instanceof ParsingPathList){
			servicesDao.merge(pathList.getService());
		} else if (pathList instanceof DistributionDriverPathList){
			driversDao.merge(pathList.getDriver());
		}
		
		getCurrentSession().merge(pathList);
		
	}
	
	/**
	 * MarkDriver ,  service and server instance dirty , then save pathList
	 */
	@Override
	public void save(PathList pathList){
		
		if(pathList instanceof CollectionDriverPathList){
			driversDao.merge(pathList.getDriver());
		} else if(pathList instanceof ParsingPathList || pathList instanceof ProcessingPathList || pathList instanceof DataConsolidationPathList){
			servicesDao.merge(pathList.getService());
		} else if (pathList instanceof DistributionDriverPathList){
			driversDao.merge(pathList.getDriver());
		}
		
		getCurrentSession().save(pathList);
	}
	
	/**
	 * MarkDriver ,  service and server instance dirty , then update pathList
	 */
	@Override
	public void merge(PathList pathList){
		getCurrentSession().merge(pathList);
		
		logger.debug("setting dirty flag for  the super entity of pathlist.");
		if(pathList instanceof CollectionDriverPathList){
			logger.debug("Found COLLECTION driver Pathlist object");
			driversDao.merge(pathList.getDriver());
		} else if(pathList instanceof ParsingPathList || pathList instanceof ProcessingPathList || pathList instanceof DataConsolidationPathList){
			logger.debug("Found PARSING Pathlist object");
			servicesDao.merge(pathList.getService());
		} else if (pathList instanceof DistributionDriverPathList){
			logger.debug("Found DISTRIBUTION driver pathlist object");
			driversDao.merge(pathList.getDriver());
		}
		
	}

	
	/**
	 * Fetch Path List count based on name , for check unique pathlist name
	 */
	@Override
	public int getPathListCount(String pathListName){
				
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (pathListName != null) {
			criteria.add(Restrictions.eq("name", pathListName));
		} 
		
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
		
	}
	
	/**
	 * Fetch PathList from DB by driver id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PathList> getPathListByDriverId(int driverId){
		
		Criteria criteria =getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("driver.id",driverId));
		criteria.addOrder(Order.asc("createdDate"));
		
		return (List<PathList>)criteria.list();
		
	}
	
	/**
	 * Fetch list of pathlist by name
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PathList> getPathListByName(String pathListName){
		
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.eq("name", pathListName).ignoreCase());
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		return  criteria.list();
		
	}
	
	/**
	 * Fetch Parsing Pathlist by id
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PathList> getParsingPathListbyId(int pathListId){
		
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.eq("id", pathListId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		return  criteria.list();
		
	}
	
	/**
	 * Method will get distribution driver path list by pathlist id.
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PathList getDistributionPathListById(int id){
		logger.debug("Fetching distribution driver pathlist for id :"  +id);
		Criteria criteria = getCurrentSession().createCriteria(DistributionDriverPathList.class);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<PathList> pathListDetail = (List<PathList>)criteria.list();
		
		if(pathListDetail != null && !pathListDetail.isEmpty()){
			logger.info("Found distribution driver path list for id : " + id);
			return pathListDetail.get(0);
		}else{
			logger.info("Failed to get distribution driver path list for id  : " + id);
			return null;
		}
	}
	
	/**
	 * Fetch all depadants of parsing pathlist
	 */
	@Override
	public void irerateOverParsingPathList(ParsingPathList pathlist){
		
		logger.debug("Iterate over parsing pathlist");
		if(pathlist!=null && pathlist.getParserWrappers()!=null){
			
				List<Parser> parserList=pathlist.getParserWrappers();
				Iterator<Parser> parserListItr = parserList.iterator();
				while(parserListItr.hasNext()){
					Parser parser = parserListItr.next();
					if(!StateEnum.DELETED.equals(parser.getStatus())){
						Hibernate.initialize(parser.getParserType());
						ParserMapping parserMapping=parser.getParserMapping();
						if(parserMapping !=null && !StateEnum.DELETED.equals(parserMapping.getStatus())){
							parserMappingDao.iterateOverParserMapping(parserMapping);
						}
					}else{
						parserListItr.remove();
					}
				}
				pathlist.setParserWrappers(parserList);
		}
	}
	
	/**
	 * Fetch all Dependents of distribution driver pathlist
	 * @param distributionPathList
	 */
	@Override
	public void iterateOverDistributionPathList(DistributionDriverPathList distributionPathList){
		
		logger.debug("Iterate over Distribution pathlist");
		if(distributionPathList !=null && distributionPathList.getComposerWrappers() !=null){
			
				List<Composer> composerList=distributionPathList.getComposerWrappers();
				Iterator<Composer> itr = composerList.iterator();
				while(itr.hasNext()){
					Composer composer = itr.next();
					if(!StateEnum.DELETED.equals(composer.getStatus())){
						logger.debug("Going to fetch Composer detail" );
						Hibernate.initialize(composer.getComposerType());
						ComposerMapping composerMapping=composer.getComposerMapping();
						if(composerMapping !=null && !StateEnum.DELETED.equals(composerMapping.getStatus())){
							logger.debug("Going to fetch Composer detail" );
							composerMappingDao.iterateOverComposerMapping(composerMapping);
						}
					
						if(composer.getCharRenameOperationList() !=null){
							List<CharRenameOperation> charReanmeOperationList=composer.getCharRenameOperationList();
							Iterator<CharRenameOperation> itrCharRename = charReanmeOperationList.iterator();
							while(itrCharRename.hasNext()){
								CharRenameOperation charRenameOperation = itrCharRename.next();
								if(!StateEnum.DELETED.equals(charRenameOperation.getStatus())){
									logger.debug("Going to fetch CharRenameOperation detail" );
									charRenameOperationDao.iterateOverCharRenameOperation(charRenameOperation);
								}else{
									itrCharRename.remove();
								}
							}
							composer.setCharRenameOperationList(charReanmeOperationList);
						}
					}else{
						itr.remove();
					}
				}
			}
		}
	
	/**
	 * Get Parsing pathlist by service id
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParsingPathList> getParsingPathListByServiceId(int serviceId){
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<ParsingPathList> parsingPathList=criteria.list();
		
		if(parsingPathList !=null && !parsingPathList.isEmpty()){
			for(ParsingPathList parsingPath:parsingPathList){
				Hibernate.initialize(parsingPath.getParserWrappers());
				List<Parser> parserList = parsingPath.getParserWrappers();
				if(parserList != null && !parserList.isEmpty()){
					for(Parser parser: parserList){
						Hibernate.initialize(parser.getParserType().getAlias());
					}
				}
			}
		}
		
		return parsingPathList;
	}
	
	
	/**
	 * Get pathlist by service id
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PathList> getPathListByServiceId(int serviceId){
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		return criteria.list();
	}

	/**
	 * Get Maximum Path Id
	 * @param serviceId the service id
	 * 
	 * @return maximum path id
	 */
	@Override
	public String getServiceMaxPathId(int serviceId) {
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("pathId"));
		
		return (String) criteria.uniqueResult();
	}
	
	/**
	 * Get Maximum Path Id
	 * @param driverId the driver id
	 * 
	 * @return maximum path id
	 */
	@Override
	public String getDriverMaxPathId(int driverId) {
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.eq("driver.id", driverId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.setProjection(Projections.max("pathId"));
		
		return (String) criteria.uniqueResult();
	}
	
	@Override
	public int getPathListCountByNameAndService(String pathListName, int serviceId, int driverId){
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		if (pathListName != null) {
			criteria.add(Restrictions.eq("name", pathListName));
		}
		if(serviceId > 0) {
			criteria.add(Restrictions.eq("service.id", serviceId));
		}
		if(driverId > 0) {
			criteria.add(Restrictions.eq("driver.id", driverId));
		}
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue();
	}
	
	@Override
	public boolean validateDuplicateDeviceName(PathList pathList){
		CollectionDriverPathList collectionPathList = (CollectionDriverPathList)pathList;
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.ne("id", collectionPathList.getId()));
		
		if (collectionPathList.getReferenceDevice() != null) {
			criteria.add(Restrictions.eq("referenceDevice", collectionPathList.getReferenceDevice()));
		} 
		
		return (((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue() > 0) ? true : false;
	}

	@Override
	public void iterateOverCollectionPathList(CollectionDriverPathList pathlist) {

		
		logger.debug("Iterate over Collection pathlist");
	
				if(pathlist.getCharRenameOperationList() !=null){
					List<CharRenameOperation> charReanmeOperationList=pathlist.getCharRenameOperationList();
					Iterator<CharRenameOperation> itrCharRename = charReanmeOperationList.iterator();
					while(itrCharRename.hasNext()){
						CharRenameOperation charRenameOperation = itrCharRename.next();
						if(!StateEnum.DELETED.equals(charRenameOperation.getStatus())){
							logger.debug("Going to fetch CharRenameOperation detail" );
							charRenameOperationDao.iterateOverCharRenameOperation(charRenameOperation);
						}else{
							itrCharRename.remove();
						}
					}
					pathlist.setCharRenameOperationList(charReanmeOperationList);
				}
	}
	
	/**
	 * Get pathlist by service id
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PathList getPathListByServiceIdAndPathId(int serviceId, String pathId){
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		
		criteria.add(Restrictions.eq("service.id", serviceId));
		criteria.add(Restrictions.eq("pathId", pathId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		List<PathList> pathListDetail = (List<PathList>)criteria.list();
		
		if(pathListDetail != null && !pathListDetail.isEmpty()){
			logger.info("Found path list for service id : " + serviceId +" , path id : " + pathId);
			return pathListDetail.get(0);
		}else{
			logger.info("Failed to get path list for service id : " + serviceId +" , path id : " + pathId);
			return null;
		}
	}
	
	/**
	 * Get pathlist by service id
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PathList> getPathListByCircleId(int circleId){
		Criteria criteria=getCurrentSession().createCriteria(PathList.class);
		
		criteria.add(Restrictions.eq("circle.id", circleId));
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		
		return criteria.list();
	}
	
	/**
	 * Fetch Path List count based on name , for check circle is associated to any parsing service path or not
	 */
	@Override
	public boolean isCircleAssociatedWithPathList(int circleId){
				
		Criteria criteria = getCurrentSession().createCriteria(PathList.class);
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));		
		criteria.add(Restrictions.eq("circle.id", circleId));	
		return ((Number) criteria.setProjection(Projections.rowCount()).uniqueResult()).intValue()>0?true:false;
	}
	
}
