package com.elitecore.sm.rulelookup.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.LongType;
import org.springframework.stereotype.Repository;

import com.elitecore.core.util.Constant;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.dao.GenericDAOImpl;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;
import com.elitecore.sm.rulelookup.model.RuleLookupData;
import com.elitecore.sm.rulelookup.model.RuleLookupTableData;
import com.elitecore.sm.util.DateFormatter;

@Repository(value="ruleLookUpTableDataDao")
public class RuleLookupDataDaoImpl extends GenericDAOImpl<RuleLookupData> implements IRuleLookupDataDao{
	
	@SuppressWarnings("unchecked")
	@Override
	public List<List<String>> getRuleLookupTableDataByTableId(int tableId){
		Criteria criteria = getCurrentSession().createCriteria(RuleLookupData.class);
		List<List<String>> name = new ArrayList<>();
		criteria.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		criteria.add(Restrictions.eq("ruleLookUpTable.id",tableId));
		ProjectionList projectionList = Projections.projectionList();
		String uniqueFields = getUniqueFieldsByTableId(tableId);
		Integer uniqueFiledLength = uniqueFields.split(",").length - 1;
		Integer paramNumber = 2;
		projectionList.add(Projections.property("strParam1"));
		
		if(uniqueFiledLength >= 1) {
			for(Integer i = 1;i<= uniqueFiledLength;i++) 
				projectionList.add(Projections.property("key"+i));			
		}
		
		for(Integer i = paramNumber;i<=20;i++) 
			projectionList.add(Projections.property("strParam"+i));
		
		criteria.setProjection(projectionList);
		List<Object> list = criteria.list();
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			Object object[] = (Object[])it.next();
			List<String> temp = new ArrayList<>();
			for(int i=0;i<object.length;i++){
				//if(object[i]!=null && StringUtils.isNotBlank(object[i].toString()))
					temp.add(object[i]+"");
			}
			name.add(temp);
			//name.add(StringUtils.join(temp.toArray(), ","));
		}
		return name;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getLookupFieldsByTableId(int tableId){
		
				Criteria criteria = getCurrentSession().createCriteria(RuleLookupTableData.class);
				List<String> name = new ArrayList<>();
				
				criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
				criteria.add(Restrictions.eq( BaseConstants.ID,tableId));
				
				List<Object> list = criteria.list();
				Iterator<Object> it = list.iterator();
				while (it.hasNext()) {
					Object object = it.next();
					
					RuleLookupTableData table = (RuleLookupTableData) object ;
					List<LookupFieldDetailData> data = table.getLookUpFieldDetailData();
					Iterator<LookupFieldDetailData> itr = data.iterator();
					while (itr.hasNext()) {
						LookupFieldDetailData fields =  itr.next();
						String fieldName = fields.getIsUnique() ? fields.getViewFieldName() + BaseConstants.TABLE_FIELD_UNIQUE_SEPERATOR : fields.getViewFieldName(); 
						name.add(fieldName);
					}
					
				}
		return name;
	}	
	
	@Override
	public Map<String, Object> getRuleTableFieldConditionList(int tableId) {
		Map<String, Object> returnMap = new HashMap<>();
		HashMap<String, String> aliases = new HashMap<>();
		List<Criterion> conditionList = new ArrayList<>();
		conditionList.add(Restrictions.eq(BaseConstants.STATUS,StateEnum.ACTIVE));
		conditionList.add(Restrictions.eq("ruleLookUpTable.id", tableId));
		returnMap.put("aliases", aliases);
		returnMap.put("conditions", conditionList);
		return returnMap;
	}
	
	
	@Override
	public Long getTotalCountUsingSQL(String sql, Map<String, Object> conditions ) {
		Long count = (long) 0;
		try{
		SQLQuery query = getCurrentSession().createSQLQuery(sql);
		query.addScalar("count", LongType.INSTANCE);

		if (conditions != null) {
			for (String key : conditions.keySet()) {
				query.setParameter(key, conditions.get(key));
			}
		}
		count = (Long) query.uniqueResult();
		}catch(Exception e){	
			logger.error(e);
		    logger.info("Invalid SQL query");
		}
		return count;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public List<Map<String, Object>> getListUsingSQL(String nativeQuery, Map<String, Object> conditions, int offset, int limit ,
			ResponseObject responseObject ){
		List<Map<String, Object>> resultList = new ArrayList<>();
		try{
		SQLQuery query = getCurrentSession().createSQLQuery(nativeQuery);

		if (conditions != null) {
			for (String key : conditions.keySet()) {
				query.setParameter(key, conditions.get(key));
			}
		}
		if( offset > 0 || limit > 0 ){
			query.setFirstResult(offset);//first record is 2
			query.setMaxResults(limit);//records from 2 to (2+3) 5
		}
		
		query.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		List data = query.list();

		Map<String, Object> resultRow;
		for (Object object : data) {
			Map row = (Map) object;
			if (row != null) {
				resultRow = new HashMap<>();//sonar issue line 368
				for (Object key : row.keySet()) {
					if (row.get(key) instanceof Date)
						resultRow.put(key.toString().toUpperCase(), DateFormatter.formatDate((Date) row.get(key)));
					else
						resultRow.put(key.toString().toUpperCase(), row.get(key));
				}
				resultList.add(resultRow);
			}
		}
	  }catch(Exception e){
		  logger.error(e);
		  responseObject.setSuccess(false);			
		  logger.info("Invalid SQL query");
		  return null;
	  }
		
		return resultList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String getUniqueFieldsByTableId(int tableId) {
		Criteria criteria = getCurrentSession().createCriteria(LookupFieldDetailData.class);
		StringBuffer name = new StringBuffer();
		
		criteria.add(Restrictions.eq(BaseConstants.STATUS, StateEnum.ACTIVE));
		criteria.add(Restrictions.eq("ruleLookUpTable.id",tableId));
		criteria.add(Restrictions.eq("isUnique", Boolean.TRUE));
		
		List<LookupFieldDetailData> list = criteria.list();
		for (LookupFieldDetailData field :list) {
			name.append(field.getViewFieldName());
			name.append(",");
		}
		name.setLength(name.length() - 1);
		return name.toString();
	}
}
