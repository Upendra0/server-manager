package com.elitecore.sm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.log4j.Logger;

import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.yafred.asn1.grammar.ASNLexer;
import com.yafred.asn1.grammar.ASNParser;
import com.yafred.asn1.model.Assignment;
import com.yafred.asn1.model.ChoiceType;
import com.yafred.asn1.model.Component;
import com.yafred.asn1.model.IA5StringType;
import com.yafred.asn1.model.ModuleDefinition;
import com.yafred.asn1.model.NamedType;
import com.yafred.asn1.model.SequenceOfType;
import com.yafred.asn1.model.SequenceType;
import com.yafred.asn1.model.SetType;
import com.yafred.asn1.model.Specification;
import com.yafred.asn1.model.Type;
import com.yafred.asn1.model.TypeAssignment;
import com.yafred.asn1.parser.Asn1ModelValidator;
import com.yafred.asn1.parser.SpecificationAntlrVisitor;

public class ASN1DefinitionToAttributeList {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static Specification model;	
	private static Map <String,String> asnTypeMap = new HashMap<String, String>();
	public static final String ELEMENTS_ARRAY = "ELEMENTS_ARRAY-";
	public static final String CHOICE_NO = "CHOICE_NO-";
	public static  String recordMailAtt = "sample_ber.sm_sgsn_pgwcdr.GPRSRecord";//NOSONAR
	public static String recordMailAttBase = "sample_ber.sm_sgsn_pgwcdr.";//NOSONAR
	public static final String LIST_STATIC_PREFIX = "List";
	
	public static final String SEQUENCE_OF_STATIC_PREFIX = "_SeqOf";
	public static final String SEQUENCE_FIELD_SEPARATOR = "#";
	public static final String LEAF_NODE_DATA_TYPE_PREFIX = "com.objsys.asn1j";
	public static final String RESET_CHILD_ATTRIBUTES = "RESET_CHILD_ATTRIBUTES";
	public static List<ASN1ParserAttribute> masterAttList = new ArrayList<>();//NOSONAR
	public static final  String UNIFIELD_FIELD_FOR_CHOICE_ID = "UFFCID";
	public static List<String> attNameList = new ArrayList<>();//NOSONAR
	public static LinkedHashMap<String, String> masterHashMap = new LinkedHashMap<String, String>();//NOSONAR
	public static LinkedHashMap<String, String> valueAssignmentMasterHashMap = new LinkedHashMap<String, String>();//NOSONAR
	public static LinkedHashMap<String, ASN1ParserAttribute> masterAttributeMap = new LinkedHashMap<String, ASN1ParserAttribute>();//NOSONAR
	public static int cnt = 0;//NOSONAR
	
	private static void initAsnType() {
		asnTypeMap.put("INTEGER", "com.objsys.asn1j.runtime.Asn1Integer");
		asnTypeMap.put("TBCD-STRING", "com.objsys.asn1j.runtime.Asn1OctetString");
		asnTypeMap.put("TBCDSTRING", "com.objsys.asn1j.runtime.Asn1OctetString");
		asnTypeMap.put("OCTET STRING", "com.objsys.asn1j.runtime.Asn1OctetString");
		asnTypeMap.put("AddressString", "com.objsys.asn1j.runtime.Asn1OctetString");
		asnTypeMap.put("ISDN-AddressString", "com.objsys.asn1j.runtime.Asn1OctetString");
		asnTypeMap.put("IA5String", "com.objsys.asn1j.runtime.Asn1IA5String");
		asnTypeMap.put("BOOLEAN", "com.objsys.asn1j.runtime.Asn1Boolean");
		asnTypeMap.put("ENUMERATED", "com.objsys.asn1j.runtime.Asn1Enumerated");
		asnTypeMap.put("BIT STRING", "com.objsys.asn1j.runtime.Asn1BitString");
		asnTypeMap.put("GraphicString", "com.objsys.asn1j.runtime.Asn1OctetString");
	}
	
	public static void main(String args[]) {
		try {
			File f = new File("asn_definition/DMC_sm_sgsn_pgwcdr2.asn");
			InputStream stream = new FileInputStream(f);
			//CharStream charStream = CharStreams.fromFileName("asn_definition/DMC_sm_sgsn_pgwcdr.asn");
			CharStream charStream = CharStreams.fromStream(stream);
			ASNLexer lexer = new ASNLexer(charStream);
			TokenStream tokens = new CommonTokenStream(lexer);
			ASNParser parser = new ASNParser(tokens);
			ParseTree tree = parser.specification();
			initAsnType();
			SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
			model = visitor.visit(tree);
			iterateOverDefinition();
			printXML();
		}catch (Exception e) {//NOSONAR
		
		}
		
		
	}
	
	public static List<ASN1ParserAttribute> converDictionaryToPlugin(File file, ASN1ParserMapping mapping) throws Exception{
			masterAttList.clear();
			attNameList.clear();
			masterHashMap.clear();
			valueAssignmentMasterHashMap.clear();
			masterAttributeMap.clear();
			
			recordMailAtt = mapping.getRecMainAttribute();
			if(recordMailAtt != null && recordMailAtt.contains(".")){
				recordMailAttBase = recordMailAtt.substring(0, recordMailAtt.lastIndexOf(".")+1);
			}else{
				recordMailAttBase = recordMailAtt;
			}
			InputStream stream = new FileInputStream(file);
			CharStream charStream = CharStreams.fromStream(stream);
			ASNLexer lexer = new ASNLexer(charStream);
			TokenStream tokens = new CommonTokenStream(lexer);
			ASNParser parser = new ASNParser(tokens);
			ParseTree tree = parser.specification();
			if(0 != parser.getNumberOfSyntaxErrors()) {
				throw new Exception("Uploaded Dictionary has errors or is not in standard ITU X.680 format.");//NOSONAR
			}
			initAsnType();
			SpecificationAntlrVisitor visitor = new SpecificationAntlrVisitor();
			model = visitor.visit(tree);
			if(model == null || model.getModuleDefinitionList() == null || model.getModuleDefinitionList().isEmpty()) {
				throw new Exception("Uploaded Dictionary has errors or is not in standard ITU X.680 format.");//NOSONAR
			}
			iterateOverDefinition();
			return masterAttList;
	}

	public static void iterateOverDefinition() throws Exception{
		List<ModuleDefinition> lstDefList = model.getModuleDefinitionList();
		if (lstDefList != null && lstDefList.size() > 0) {
			for (ModuleDefinition def : lstDefList) {
				if (def != null) {
					for (Assignment assignment : def.getAssignmentList()) {
						if (assignment.isTypeAssignment()) {
							initialzevalueAssignmentMap(assignment);
						}
					}
					for (Assignment assignment : def.getAssignmentList()) {
						
						if (assignment.isTypeAssignment()) {
							setAttribute(assignment);
						}
					}
					for(String key : masterHashMap.keySet()){
						if(key.contains(SEQUENCE_FIELD_SEPARATOR)){
							key = key.substring(0, key.indexOf(SEQUENCE_FIELD_SEPARATOR));
						}
						if(!attNameList.contains(key)){
							String typeKey = masterHashMap.get(key);
							ASN1ParserAttribute att = masterAttributeMap.get(typeKey);
							if(att != null){
								ASN1ParserAttribute missingAtt = new ASN1ParserAttribute();
								missingAtt.setSourceField(key);
								if(att.getChildAttributes() != null){
									missingAtt.setChildAttributes(att.getChildAttributes());
								}else{
									missingAtt.setChildAttributes("");	
								}
								if(att.getUnifiedFieldHoldsChoiceId() != null && !att.getUnifiedFieldHoldsChoiceId().isEmpty()){
									missingAtt.setUnifiedFieldHoldsChoiceId(att.getUnifiedFieldHoldsChoiceId());
								}else{
									missingAtt.setUnifiedFieldHoldsChoiceId("");
								}
								missingAtt.setASN1DataType(att.getASN1DataType());
								missingAtt.setUnifiedField(att.getUnifiedField());
								masterAttList.add(missingAtt);
							}
						}
					}
					
					Iterator<ASN1ParserAttribute> attIterator = masterAttList.iterator();
					while(attIterator.hasNext()){
						ASN1ParserAttribute attribute = attIterator.next();
						
						if(attribute.getUnifiedField() != null && !attribute.getUnifiedField().isEmpty()){
							attribute.setUnifiedField(getTrimmedFieldName(attribute.getUnifiedField()));
						}
						if((attribute.getASN1DataType() != null && attribute.getChildAttributes() != null && attribute.getChildAttributes().contains(RESET_CHILD_ATTRIBUTES))){
							for(ASN1ParserAttribute tmpAtt : masterAttList){
								if(tmpAtt.getASN1DataType() != null && tmpAtt.getASN1DataType().equals(attribute.getASN1DataType())){
									if(tmpAtt.getChildAttributes() != null && !tmpAtt.getChildAttributes().isEmpty() && !tmpAtt.getChildAttributes().contains(RESET_CHILD_ATTRIBUTES)){
										attribute.setChildAttributes(tmpAtt.getChildAttributes());
									}
								}
							}
						}
						if(attribute.getSourceField() != null && !attribute.getSourceField().isEmpty() && 
								attribute.getSourceField().contains("_") && 
								(!attribute.getSourceField().contains(ELEMENTS_ARRAY) && !attribute.getSourceField().contains(CHOICE_NO))){
							String field = attribute.getSourceField();
							String [] fieldArr = field.split("_");
							try{
								if(Integer.parseInt(fieldArr[fieldArr.length-1]) > -1){
									attribute.setSourceField(field.substring(0,field.lastIndexOf("_")));
									
								}
							}catch(Exception ex){//NOSONAR
								
							}
							
						}
						
						if(attribute.getChildAttributes() != null && !attribute.getChildAttributes().isEmpty() && 
								attribute.getChildAttributes().contains("_") && 
								(!attribute.getChildAttributes().contains(ELEMENTS_ARRAY) && !attribute.getChildAttributes().contains(CHOICE_NO))){
							String childAttrs = "";
							String [] childARR = attribute.getChildAttributes().split(",");
							for(int i =0; i<childARR.length; i++){
								
								String field = childARR[i];
								if(field.contains("_")){
									String [] fieldArr = field.split("_");
									try{
										if(Integer.parseInt(fieldArr[fieldArr.length-1]) > -1){
											
											field = field.substring(0,field.lastIndexOf("_"));
										}
									}catch(Exception ex){//NOSONAR
										//ignore
									}
									
								}
								if(childAttrs.isEmpty()){
									childAttrs = field;
								}else{
									childAttrs = childAttrs+","+field;
								}
							}
							attribute.setChildAttributes(childAttrs);
						}						
					}								
				}
			}
		}
		Asn1ModelValidator asn1ModelValidator = new Asn1ModelValidator();
		asn1ModelValidator.visit(model);
	}
	public static String getChildList(Type objType, String fieldName, String sourceField) throws Exception{
		List<Component> lstComponent = null;
		String childAtt = null;
		
		if (objType.isChoiceType()) {
			ChoiceType objChoiceType = (ChoiceType) objType;
			lstComponent = objChoiceType.getRootComponentList();
		} else if (objType.isSetType()) {
			SetType objSetType = (SetType) objType;
			lstComponent = objSetType.getRootComponentList();
		} else if (objType.isSequenceType()) {
			SequenceType objSequenceType = (SequenceType) objType;
			lstComponent = objSequenceType.getRootComponentList();
		}
		
		if(lstComponent != null && !lstComponent.isEmpty()){
			childAtt = "";
			for (int i = 0; i <= lstComponent.size() -1 ; i++) {
				if (lstComponent.get(i).isNamedType()) {
					NamedType objNamedType = (NamedType) lstComponent.get(i);
					String dataType = getLeafDataType(objNamedType.getType());
					
					if (objType.isChoiceType()) {
						String fieldNM = "";
						if(objNamedType.getType().isSequenceOfType()){
							SequenceOfType sq = (SequenceOfType)objNamedType.getType();
							fieldNM = objNamedType.getName();
							if(fieldNM.contains("-")){
								fieldNM = updateAndSetFieldInMap(fieldName.replace("-", "_")+"_"+CHOICE_NO+(i+1), sq.getElement().getType().getName(), true);
							}else{
								fieldNM = updateAndSetFieldInMap(fieldName+"_"+CHOICE_NO+(i+1), sq.getElement().getType().getName(), true);	
							}
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;
							}
						}else{
							if(fieldName.contains("-")){
								fieldNM = updateAndSetFieldInMap(fieldName.replace("-", "_")+"_"+CHOICE_NO+(i+1), objNamedType.getType().getName(), false);
							}else{
								fieldNM = updateAndSetFieldInMap(fieldName+"_"+CHOICE_NO+(i+1), objNamedType.getType().getName(), false);	
							}
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;
							}
						}
						
					} else if (objType.isSequenceType()) {
						String fieldNM = "";
						if(objNamedType.getType().isSequenceOfType()){
							SequenceOfType sq = (SequenceOfType)objNamedType.getType();
							fieldNM = objNamedType.getName();
							if(fieldNM.contains("-")){
								fieldNM = updateAndSetFieldInMap(objNamedType.getName().replace("-", "_"), sq.getElement().getType().getName(), true);
							}else{
								fieldNM = updateAndSetFieldInMap(objNamedType.getName(), sq.getElement().getType().getName(), true);	
							}
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;
							}
						}else{
							fieldNM = objNamedType.getName();
							if(fieldNM.contains("-")){
								fieldNM = updateAndSetFieldInMap(objNamedType.getName().replace("-", "_")+"_"+ELEMENTS_ARRAY+(i+1), objNamedType.getType().getName(), false);
							}else{
								fieldNM = updateAndSetFieldInMap(objNamedType.getName(), objNamedType.getType().getName(), false);	
							}
							
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;	
							}
						}
					}else{
						String fieldNM = "";
						if(objNamedType.getType().isSequenceOfType()){
							SequenceOfType sq = (SequenceOfType)objNamedType.getType();
							fieldNM = objNamedType.getName();
							if(fieldNM.contains("-")){
								fieldNM = updateAndSetFieldInMap(objNamedType.getName().replace("-", "_"), sq.getElement().getType().getName(), true);
							}else{
								fieldNM = updateAndSetFieldInMap(objNamedType.getName(), sq.getElement().getType().getName(), true);	
							}
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;
							}
							
						}else{
							fieldNM = objNamedType.getName();
							if(fieldNM.contains("-")){
								fieldNM = updateAndSetFieldInMap(objNamedType.getName().replace("-", "_"), objNamedType.getType().getName(), false);
							}else{
								fieldNM = updateAndSetFieldInMap(objNamedType.getName(), objNamedType.getType().getName(), false);	
							}
							setLeafAttribute(dataType, fieldNM, objNamedType.getName());
							if(childAtt.isEmpty()){
								childAtt = fieldNM;
							}else{
								childAtt = childAtt+","+fieldNM;	
							}
						}
					}
				}
			}
			if(childAtt != null && childAtt.endsWith(",")){
				childAtt = childAtt.substring(0, childAtt.length() -1);
			}
		}
		return childAtt;
	}
	
	public static void setAttribute(Assignment assignment) throws Exception{
		ASN1ParserAttribute attribute = new ASN1ParserAttribute();
		String childAtt = "";
		String dataType = "";
		String sourceField = "";
		
		TypeAssignment objTypeAssignment = null;
		objTypeAssignment = (TypeAssignment) assignment;
		objTypeAssignment.getReference();
		Type objType = objTypeAssignment.getType();
		
		sourceField = getValueFromMap(assignment.getReference());
		if(sourceField == null || sourceField.isEmpty()){
			sourceField = assignment.getReference().toLowerCase();
			if(sourceField.contains("-")){
				sourceField = sourceField.replace("-", "_");
			}
		}
		
		if(objType.isChoiceType()){
			childAtt = getChildList(objType, assignment.getReference(), sourceField);
			dataType = recordMailAttBase + assignment.getReference();
		}else if(objType.isSequenceType()){
			childAtt = getChildList(objType, assignment.getReference(), sourceField);
			dataType = recordMailAttBase + assignment.getReference();
		}else if(objType.isSetType()){
			childAtt = getChildList(objType, assignment.getReference(), sourceField);
			dataType = recordMailAttBase + assignment.getReference();
		}else if(objType.isSetOfType()){
			childAtt = getChildList(objType, assignment.getReference(), sourceField);
			dataType = recordMailAttBase + assignment.getReference();
		}else if (objType.isSequenceOfType()) {
			dataType = recordMailAttBase + assignment.getReference();
		} else{
			dataType = getLeafDataType(objType);
			if(dataType == null){
				dataType = getDataTypeFromMapRecursively(assignment.getReference());
				if(dataType!=null &&!dataType.equals(null)) {//NOSONAR
					if(!dataType.contains(LEAF_NODE_DATA_TYPE_PREFIX)){
						childAtt = RESET_CHILD_ATTRIBUTES;	
					}
				}
			}
		}
		if(dataType != null && dataType.endsWith(LIST_STATIC_PREFIX)){
			String newSourceField = ELEMENTS_ARRAY+incAndGet();
			setElementArrayAttribute(newSourceField, childAtt, dataType.substring(0,dataType.lastIndexOf("List")), assignment.getReference());
			childAtt = newSourceField;
		}
		if(sourceField.contains(SEQUENCE_FIELD_SEPARATOR)){
			if(dataType != null && !dataType.contains(LEAF_NODE_DATA_TYPE_PREFIX)){
				String str1 = dataType.substring(0,dataType.lastIndexOf(".")+1);
				String str2 = dataType.substring(dataType.lastIndexOf(".")+1, dataType.length());
				
				String newSourceField = assignment.getReference()+"_"+ELEMENTS_ARRAY+incAndGet();
				setElementArrayAttribute(newSourceField, childAtt, dataType, assignment.getReference());
				dataType = str1 + SEQUENCE_OF_STATIC_PREFIX+str2;
				childAtt = newSourceField;
			}
			sourceField = sourceField.substring(0,sourceField.indexOf(SEQUENCE_FIELD_SEPARATOR));
		}
		if(dataType != null && dataType.contains(LEAF_NODE_DATA_TYPE_PREFIX) && sourceField != null){
			attribute.setUnifiedField(sourceField);
		}else{
			attribute.setUnifiedField("");
		}
		attribute.setSourceField(sourceField);
		if(sourceField != null && sourceField.contains(CHOICE_NO)){
			attribute.setUnifiedFieldHoldsChoiceId(UNIFIELD_FIELD_FOR_CHOICE_ID+"_"+sourceField.substring(0, sourceField.indexOf("_")));
		}else{
			attribute.setUnifiedFieldHoldsChoiceId("");
		}
		
		
		attribute.setChildAttributes(childAtt);
		attribute.setASN1DataType(dataType);
		masterAttributeMap.put(assignment.getReference(), attribute);
		attNameList.add(attribute.getSourceField());
		if(dataType != null && !dataType.contains(recordMailAtt)){
			masterAttList.add(attribute);	
		}
	}
	
	public static String getValueFromMap(String matchValue) throws Exception{//NOSONAR
		String val = null;
		for(String key : masterHashMap.keySet()){
			if(masterHashMap.get(key).equals(matchValue)){
				val = key;
				break;
			}
		}
		if(val == null){
			for(String key : masterHashMap.keySet()){
				if(masterHashMap.get(key).startsWith(matchValue) && 
					(masterHashMap.get(key).contains(SEQUENCE_OF_STATIC_PREFIX)|| 
					masterHashMap.get(key).contains(CHOICE_NO) ||
					masterHashMap.get(key).contains(ELEMENTS_ARRAY))){
					val = key;
					break;
				}
			}	
		}
		return val;
	}
	
	public static int incAndGet(){
		return cnt++;
	}
	
	public static String updateAndSetFieldInMap(String field, String value, boolean isSequenceOf){
		if(masterHashMap != null) {
			if(masterHashMap.get(field) != null){
				String val = masterHashMap.get(field);
				if(!val.equals(value)){
					field = field+"_"+incAndGet();
					if(isSequenceOf){
						masterHashMap.put(field+SEQUENCE_FIELD_SEPARATOR+field, value);
					}else{
						masterHashMap.put(field, value);	
					}
				}
			}else{
				if(isSequenceOf){
					masterHashMap.put(field+SEQUENCE_FIELD_SEPARATOR+field, value);
				}else{
					masterHashMap.put(field, value);	
				}
			}
		}
		return field;
	}
	
	public static String getDataTypeFromMapRecursively(String key) throws Exception{
		String dataType = valueAssignmentMasterHashMap.get(key);
		if(dataType != null ){
			if(dataType.contains(LEAF_NODE_DATA_TYPE_PREFIX) || dataType.contains(SEQUENCE_FIELD_SEPARATOR)){
				if(dataType.contains(SEQUENCE_FIELD_SEPARATOR)){
					return dataType.split(SEQUENCE_FIELD_SEPARATOR)[0];
				}else{
					return dataType;	
				}
				
			}else{
				return getDataTypeFromMapRecursively(dataType);
			}
			
		}else{
			return null;
		}
	}
	
	public static void initialzevalueAssignmentMap(Assignment assignment) throws Exception{
		
		TypeAssignment objTypeAssignment = null;
		objTypeAssignment = (TypeAssignment) assignment;
		objTypeAssignment.getReference();
		String dataType = "";
	
		Type objType = objTypeAssignment.getType();
		
		
		if(objType.isChoiceType()){
			dataType = recordMailAttBase + assignment.getReference()+SEQUENCE_FIELD_SEPARATOR+objType.getName();
		}else if(objType.isSequenceType()){
			dataType = recordMailAttBase + assignment.getReference()+SEQUENCE_FIELD_SEPARATOR+objType.getName();
		}else if(objType.isSetType()){
			dataType = recordMailAttBase + assignment.getReference()+SEQUENCE_FIELD_SEPARATOR+objType.getName();
		}else if(objType.isSetOfType()){
			dataType = recordMailAttBase + assignment.getReference()+SEQUENCE_FIELD_SEPARATOR+objType.getName();
		}else if (objType.isSequenceOfType()) {
			dataType = recordMailAttBase + assignment.getReference()+SEQUENCE_FIELD_SEPARATOR+objType.getName();
		} else{
			dataType = getLeafDataType(objType);
			if(dataType == null){
				dataType = objType.getName();
			}
		}
		ASN1ParserAttribute att = new ASN1ParserAttribute();
		att.setASN1DataType(dataType);
		valueAssignmentMasterHashMap.put(assignment.getReference(), dataType);
	}	
	
	public static String getLeafDataType(Type objType) throws Exception{//NOSONAR
		String dataType = null;
		if (objType.isOctetStringType()) {
			dataType = asnTypeMap.get("OCTET STRING");
		}
		if(objType instanceof IA5StringType){
			dataType = asnTypeMap.get("IA5String");
		} else if (objType.isBooleanType()) {
			dataType = asnTypeMap.get("BOOLEAN");
		} else if (objType.isBitStringType()) {
			dataType = asnTypeMap.get("BIT STRING");
		} else if (objType.isIntegerType()) {
			dataType = asnTypeMap.get("INTEGER");
		} else if (objType.isEnumeratedType()) {
			dataType = asnTypeMap.get("ENUMERATED");
		}else if(objType.getName().contains("ISDN-AddressString")){
			dataType = asnTypeMap.get("OCTET STRING");
		}else if(objType.getName().contains("AddressString")){
			dataType = asnTypeMap.get("OCTET STRING");
		}else if(objType.getName().contains("GraphicString")){
			dataType = asnTypeMap.get("OCTET STRING");
		}
		return dataType;
	}
	
	public static void setLeafAttribute(String dataType, String fieldNM, String reference) throws Exception{//NOSONAR
		if(dataType != null && !attNameList.contains(fieldNM)){
			ASN1ParserAttribute attribute = new ASN1ParserAttribute();
			attribute.setASN1DataType(dataType);
			attribute.setChildAttributes("");
			attribute.setRecordInitilializer("false");
			attribute.setSourceField(fieldNM);
			attribute.setUnifiedField(fieldNM);
			attribute.setUnifiedFieldHoldsChoiceId("");
			attNameList.add(fieldNM);
			masterAttributeMap.put(reference, attribute);
			masterAttList.add(attribute);
		}
	}
	
	public static void setElementArrayAttribute(String fieldNM, String childAttrs, String dataType, String reference) throws Exception{//NOSONAR
		if(dataType != null && !attNameList.contains(fieldNM)){
			ASN1ParserAttribute attribute = new ASN1ParserAttribute();
			attribute.setASN1DataType(dataType);
			attribute.setChildAttributes(childAttrs);
			attribute.setRecordInitilializer("false");
			attribute.setSourceField(fieldNM);
			attribute.setUnifiedField("");
			attribute.setUnifiedFieldHoldsChoiceId("");
			attNameList.add(fieldNM);
			masterAttributeMap.put(reference, attribute);
			masterAttList.add(attribute);
		}
	}
	
	public static String getTrimmedFieldName(String fieldName) throws Exception{//NOSONAR
		if(fieldName != null && !fieldName.isEmpty()){
			if(fieldName.contains(ELEMENTS_ARRAY) || fieldName.contains(CHOICE_NO)){
				if(fieldName.length() <= 8){
					return fieldName;
				}else{
					return fieldName.substring(0, 7);	
				}
			}else{
				if(fieldName.length() <= 25){
					return fieldName;
				}else{
					return fieldName.substring(0, 24);
				}
			}
		}
		return fieldName;
	}
	
	public static void printXML(){
		StringBuilder builder = new StringBuilder("");
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><asn1-parser-plugin>\n");
		builder.append("<instance id=\"44\">\n");
		//builder.append("<record-main-attribute>sample_ber.st_gmsc_test.CallEventRecord</record-main-attribute>\n");
		builder.append("<record-main-attribute>sample_ber.sm_sgsn_pgwcdr.GPRSRecord</record-main-attribute>\n");
		builder.append("<source-date-format>MM-dd-yyyy HH:mm:ss</source-date-format>\n");
		builder.append("<header-attribute-list/>\n");
		builder.append("<remove-additional-byte enabled=\"false\">\n");
		builder.append("<header-offset>0</header-offset>\n");
		builder.append("<record-offset>0</record-offset>\n");
		builder.append("</remove-additional-byte>\n");
		builder.append("<remove-variable-length-fillers enabled=\"false\">\n");
		builder.append("<record-start-ids>0,1,2,3,4,5,6,7,10,11,12,13,14,16,17,18,19,20,21,40,41,42,43,44,102,103</record-start-ids>\n");
		builder.append("</remove-variable-length-fillers>\n");
		builder.append("<skip-attribute-mapping-and-decode enabled=\"false\">\n");
		builder.append("<root-node-name>CallEventDetails</root-node-name>\n");
		builder.append("<decode-type>XML</decode-type>\n");
		builder.append("<decode-buffer-size>1024</decode-buffer-size>\n");
		builder.append("</skip-attribute-mapping-and-decode>\n");
		builder.append("<remove-additional-header-footer>false</remove-additional-header-footer>\n");
		builder.append("<attribute-list>\n");
		List<String> tempList = new ArrayList<String>();
		for(ASN1ParserAttribute attribute : masterAttList){
				
			if(!tempList.contains(attribute.getSourceField())){
				builder.append("<attribute>\n");
				builder.append("<source-field> "+attribute.getSourceField()+" </source-field>\n");
				builder.append("<unified-field>"+attribute.getUnifiedField()+"</unified-field>\n");
				builder.append("<asn1-data-type>"+attribute.getASN1DataType()+"</asn1-data-type>\n");
				builder.append("<source-field-format></source-field-format>\n<default-value></default-value>\n<trim-chars></trim-chars>\n");
				builder.append("<child-attributes>"+attribute.getChildAttributes()+"</child-attributes>\n");
				builder.append("<record-initializer>false</record-initializer>\n");	
				builder.append("<unified-field-for-choice-id>"+attribute.getUnifiedFieldHoldsChoiceId()+"</unified-field-for-choice-id>\n");
				builder.append("</attribute>\n");
			}
			tempList.add(attribute.getSourceField());
		}
		builder.append("</attribute-list>\n");
		builder.append("</instance>\n");
		builder.append("</asn1-parser-plugin>");
		try(FileWriter fw = new FileWriter("asn_plugin/asn1-parser-plugin.xml")){
			fw.write(builder.toString());
		}catch(Exception ex){//NOSONAR
			ex.printStackTrace();//NOSONAR
		}
	}
}