/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.dao.PathListDao;

/**
 * @author Ranjitsinh Reval
 *
 */
@Repository(value="parserDao")
public class ParserDaoImpl extends GenericDAOImpl<Parser> implements ParserDao{

	
	@Autowired
	PathListDao pathListDao;
	/**
	 * Method will get Natflow Parser plug-in by plug-in Id.
	 * @param id
	 * @return NATFlowParser
	 */
	@Override
	public NATFlowParserMapping getNatFlowParserById(int id) {
		NATFlowParserMapping natFlowParser = (NATFlowParserMapping) getCurrentSession().load(NATFlowParserMapping.class, id);
		return natFlowParser;
	}
	/**
	 * Method will update natflow parser configuration.
	 * @param natFlowParser
	 * @return int
	 */
	@Override
	public int updateNatFlowParserConfiguration(NATFlowParserMapping natFlowParser) {
		getCurrentSession().merge(natFlowParser);
		if(natFlowParser != null){
			return natFlowParser.getId();
		}else{
			return 0;	
		}	
	}
	
	/**
	 * Get list of parser by name
	 * @param name
	 * @return List of parser with name
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Parser> getParserByName(String parserName){
		
		Criteria criteria=getCurrentSession().createCriteria(Parser.class);
		criteria.add(Restrictions.eq("name", parserName).ignoreCase());
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		
		List<Parser> parserList = (List<Parser>)criteria.list();
		
		return parserList;
	}
	
	/** 
	 * Execute hql query in database and calculate result count
	 * @param query
	 * @return Hql query result count  
	 */
	@Override
	@SuppressWarnings("unchecked")
	public long getQueryCountUsingHql(String query){
		
		long count = 0;
		
		List<Object[]> liResult = (List<Object[]>)getCurrentSession().createQuery(query).list();
		
		if(liResult != null)
			return Long.valueOf(String.valueOf(liResult.size()));
		else
			return count;
				
	}
	
	/** 
	 * Get paginated hql query result from db
	 * @param query
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return Paginated query response as list of object
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[]> getPaginatedList(String query, int startIndex, int limit, String sidx, String sord){
		
		List<Object[]> liResult = getCurrentSession()
						.createQuery(query)
						.setFirstResult(startIndex)//first record is 2
						.setMaxResults(limit).list();//records from 2 to (2+3) 5

		
		return liResult;
	}
	
	
	/**
	 * MarkDriver ,  service and server instance dirty , then update parser
	 */
	@Override
	public void update (Parser parser){
		
		pathListDao.merge(parser.getParsingPathList());
		
		getCurrentSession().merge(parser);
		
	}
	
	/**
	 * MarkDriver ,  service and server instance dirty , then save parser
	 */
	@Override
	public void save(Parser parser){
		
		pathListDao.merge(parser.getParsingPathList());
		
		getCurrentSession().save(parser);
	}
	
	/**
	 * MarkDriver ,  service and server instance dirty , then update parser
	 */
	@Override
	public void merge(Parser parser){
		
		pathListDao.merge(parser.getParsingPathList());
		
		getCurrentSession().merge(parser);
	}
	
	/**
	 * Get count of parsers by path list
	 * @param pathListId the path list id
	 * @return count of parsers per path list
	 */
	@Override
	public long getParserCountByPathList(int pathListId) {
		Criteria criteria = getCurrentSession().createCriteria(Parser.class);
		criteria.add(Restrictions.eq("parsingPathList.id", pathListId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));
		criteria.setProjection(Projections.rowCount());
		return (long)criteria.uniqueResult();
	}
	
	/**
	 * Get parser by pathlist id
	 * 
	 * @param name
	 * @return parser
	 */
	@Override
	@Transactional(readOnly = true)
	public Parser getParserByPathListId(int pathListId) {

		Criteria criteria = getCurrentSession().createCriteria(Parser.class);
		criteria.add(Restrictions.eq("parsingPathList.id", pathListId));
		criteria.add(Restrictions.ne("status", StateEnum.DELETED));

		return (Parser) criteria.uniqueResult();
	}
}
