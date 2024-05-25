/**
 * 
 */
package com.elitecore.sm.common.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

/**
 * JqGridData<T> is generic class that is used to convert Model List into JSON format that is understood by the JQGrid. 
 * 
 * @author Sunil Gulabani
 * Mar 19, 2015
 */

public class JqGridData<T> {

	/** Total number of pages */
	private int total;
	/** The current page number */
	private int page;
	/** Total number of records */
	private int records;
	/** The actual data */
	private List<T> rows;

	public JqGridData(int total, int page, int records, List<T> rows){
		this.total = total;
		this.page = page;
		this.records = records;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public int getPage() {
		return page;
	}

	public int getRecords() {
		return records;
	}

	public List<T> getRows() {
		return rows;
	}

	public String getJsonString(){
		Map<String, Object> map = new HashMap<>();
		map.put("page", String.valueOf(page));
		map.put("total", String.valueOf(total));
		map.put("records", String.valueOf(records));
		map.put("rows", rows);
		return JSONValue.toJSONString(map);
	}
}