package com.elitecore.sm.report.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;
//import org.springframework.web.servlet.view.document.AbstractExcelView;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.XMLParserAttribute;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsView;

/**
 * @author brijesh.soni
 */
public class ReportView extends AbstractXlsView {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void buildExcelDocument(Map model, Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		DateFormat dateFormatter = new SimpleDateFormat(BaseConstants.DELETE_EXPORT_DATE_TIME_FORMATTER);
		String fileName = "AttributeMapping" + "_" + dateFormatter.format(new Date()) + ".xls";
		response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
		String plugInType = (String) model.get("plugInType");
		String streamType = (String) model.get("streamType");
		List<ParserAttribute> parserAttributeList = null;
		List<ComposerAttribute> composerAttributeList = null;
		if(streamType.equals("UPSTREAM"))
			parserAttributeList = (List<ParserAttribute>) (Object) model.get("attributeList");
		else
			composerAttributeList = (List<ComposerAttribute>) (Object) model.get("attributeList");
		Sheet sheet = workbook.createSheet("Attribute Mapping");
		Row header = sheet.createRow(0);

		switch (plugInType) {
		case EngineConstants.ASCII_PARSING_PLUGIN:
			createXLSFileForASCIIParsingPlugin(sheet, header, parserAttributeList);
			break;

		case EngineConstants.FIXED_LENGTH_ASCII_PARSING_PLUGIN:
			createXLSFileForFixedLengthASCIIParsingPlugin(sheet, header, parserAttributeList);
			break;

		case EngineConstants.ASN1_PARSING_PLUGIN:
			createXLSFileForASN1ParsingPlugin(sheet, header, parserAttributeList);
			break;

		case EngineConstants.XML_PARSING_PLUGIN:
			createXLSFileForXMLParsingPlugin(sheet, header, parserAttributeList);
			break;
			
		case EngineConstants.FIXED_LENGTH_BINARY_PARSING_PLUGIN:
			createXLSFileForFixedLengthBinaryParsingPlugin(sheet, header, parserAttributeList);
			break;
		
		case EngineConstants.PDF_PARSING_PLUGIN:
			createXLSFileForPDFParsingPlugin(sheet, header, parserAttributeList);
			break;
			
		case EngineConstants.NATFLOW_PARSING_PLUGIN:
			createXLSFileForNatFlowParsingPlugin(sheet, header, parserAttributeList);
			break;
			
		case EngineConstants.ASCII_COMPOSER_PLUGIN:
			createXLSFileForAsciiComposerPlugin(sheet, header, composerAttributeList);
			break;
			
		case EngineConstants.ASN1_COMPOSER_PLUGIN:
			createXLSFileForAsn1ComposerPlugin(sheet, header, composerAttributeList);
			break;
			
		case EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN:
			createXLSFileForFixedLengthAsciiComposerPlugin(sheet, header, composerAttributeList);
			break;
			
		default:
			break;
		}

	}

	private void createXLSFileForASN1ParsingPlugin(Sheet sheet, Row header, List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Source Field");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("ASN1 Data Type");
		header.createCell(cellCounter++).setCellValue("Child Attributes");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Source Field Data Format");
		header.createCell(cellCounter++).setCellValue("Is Record Initializer");
		header.createCell(cellCounter++).setCellValue("ChoiceId Holder Unified field");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ASN1ParserAttribute attribute = (ASN1ParserAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getASN1DataType());
					row.createCell(rowCellCounter++).setCellValue(attribute.getChildAttributes());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					if(attribute.getSrcDataFormat() == null){
						row.createCell(rowCellCounter++).setCellValue("");
					}else{
						row.createCell(rowCellCounter++).setCellValue(String.valueOf(attribute.getSrcDataFormat()));						
					}
					row.createCell(rowCellCounter++).setCellValue(attribute.isRecordInitilializer());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedFieldHoldsChoiceId());
				}
			}
		}

	}

	private void createXLSFileForFixedLengthASCIIParsingPlugin(Sheet sheet, Row header,
			List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Source Field Format");
		header.createCell(cellCounter++).setCellValue("Length");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Prefix");
		header.createCell(cellCounter++).setCellValue("Postfix");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Start Length");
		header.createCell(cellCounter++).setCellValue("End Length");
		header.createCell(cellCounter++).setCellValue("Rigth Delimiter");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				FixedLengthASCIIParserAttribute attribute = (FixedLengthASCIIParserAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceFieldFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPrefix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPostfix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getStartLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getEndLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getRightDelimiter());
				}
			}
		}

	}

	private void createXLSFileForASCIIParsingPlugin(Sheet sheet, Row header, List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Source Field");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Source Field Format");
		header.createCell(cellCounter++).setCellValue("Date Format");
		header.createCell(cellCounter++).setCellValue("IP-Port Unified field (port)");
		header.createCell(cellCounter++).setCellValue("IP-Port Separator");
		
		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ParserAttribute attribute = attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceFieldFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDateFormat());
					try{
						AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute) attribute;
						row.createCell(rowCellCounter++).setCellValue(asciiParserAttribute.getPortUnifiedField());
						row.createCell(rowCellCounter++).setCellValue(asciiParserAttribute.getIpPortSeperator());
					} catch(ClassCastException castException) {}//NOSONAR
				}
			}
		}
	}

	private void createXLSFileForXMLParsingPlugin(Sheet sheet, Row header, List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Source Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Source Field Format");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ParserAttribute attribute = null;	
				try{
					attribute = (XMLParserAttribute) attributeList.get(i);
				} catch(Exception e) {//NOSONAR
					attribute = (ParserAttribute) attributeList.get(i);
				}
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceFieldFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
				}
			}
		}
	}
	
	private void createXLSFileForFixedLengthBinaryParsingPlugin(Sheet sheet, Row header,
			List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Source Field Format");
		header.createCell(cellCounter++).setCellValue("Length");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Prefix");
		header.createCell(cellCounter++).setCellValue("Postfix");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Start Length");
		header.createCell(cellCounter++).setCellValue("End Length");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				FixedLengthBinaryParserAttribute attribute = (FixedLengthBinaryParserAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceFieldFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPrefix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPostfix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getStartLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getEndLength());
				}
			}
		}
	}
	
	private void createXLSFileForPDFParsingPlugin(Sheet sheet, Row header, List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Location");
		header.createCell(cellCounter++).setCellValue("Column Start Location");
		header.createCell(cellCounter++).setCellValue("Column Identifier");
		header.createCell(cellCounter++).setCellValue("Reference Row");
		header.createCell(cellCounter++).setCellValue("Column Starts With");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				PDFParserAttribute attribute = (PDFParserAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getLocation());
					row.createCell(rowCellCounter++).setCellValue(attribute.getColumnStartLocation());
					row.createCell(rowCellCounter++).setCellValue(attribute.getColumnIdentifier());
					row.createCell(rowCellCounter++).setCellValue(attribute.getReferenceRow());
					row.createCell(rowCellCounter++).setCellValue(attribute.getColumnStartsWith());
				}
			}
		}
	}
	
	private void createXLSFileForNatFlowParsingPlugin(Sheet sheet, Row header, List<ParserAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Source Field");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Trim Char");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Source Field Format");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ParserAttribute attribute = (ParserAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefaultValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimChars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSourceFieldFormat());
				}
			}
		}
	}
	
	private void createXLSFileForAsciiComposerPlugin(Sheet sheet, Row header, List<ComposerAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Seq.No");
		header.createCell(cellCounter++).setCellValue("Destination Field Name");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Data Type");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Trim Position");
		header.createCell(cellCounter++).setCellValue("Date Format");
		header.createCell(cellCounter++).setCellValue("Replace Condition List");
		header.createCell(cellCounter++).setCellValue("Enable Padding");
		header.createCell(cellCounter++).setCellValue("Padding Length");
		header.createCell(cellCounter++).setCellValue("Type");
		header.createCell(cellCounter++).setCellValue("Padding Character");
		header.createCell(cellCounter++).setCellValue("Prefix");
		header.createCell(cellCounter++).setCellValue("Suffix");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ASCIIComposerAttr attribute = (ASCIIComposerAttr) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getSequenceNumber());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDestinationField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefualtValue());
					if(attribute.getDataType() == null)
						row.createCell(rowCellCounter++).setCellValue("");
					else
						row.createCell(rowCellCounter++).setCellValue(attribute.getDataType().toString());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimchars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimPosition());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDateFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getReplaceConditionList());
					row.createCell(rowCellCounter++).setCellValue(String.valueOf(attribute.isPaddingEnable()));
					row.createCell(rowCellCounter++).setCellValue(attribute.getLength());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPaddingType().toString());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPaddingChar());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPrefix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSuffix());
				}
			}
		}
	}
	
	private void createXLSFileForAsn1ComposerPlugin(Sheet sheet, Row header, List<ComposerAttribute> attributeList) {


		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Destination Field Name");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("ASN1 Data type");
		header.createCell(cellCounter++).setCellValue("Destination Field Format");
		header.createCell(cellCounter++).setCellValue("Argument Data Type");
		header.createCell(cellCounter++).setCellValue("Choice Id");
		header.createCell(cellCounter++).setCellValue("Child Attributes");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				ASN1ComposerAttribute attribute = (ASN1ComposerAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getDestinationField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefualtValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimchars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getasn1DataType());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDestFieldDataFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getArgumentDataType());
					row.createCell(rowCellCounter++).setCellValue(attribute.getChoiceId());
					row.createCell(rowCellCounter++).setCellValue(attribute.getChildAttributes());
				}
			}
		}
	
	}
	
	private void createXLSFileForFixedLengthAsciiComposerPlugin(Sheet sheet, Row header, List<ComposerAttribute> attributeList) {

		int cellCounter = 0;
		header.createCell(cellCounter++).setCellValue("Seq.No");
		header.createCell(cellCounter++).setCellValue("Unified Field");
		header.createCell(cellCounter++).setCellValue("Description");
		header.createCell(cellCounter++).setCellValue("Default Value");
		header.createCell(cellCounter++).setCellValue("Trim Character");
		header.createCell(cellCounter++).setCellValue("Type");
		header.createCell(cellCounter++).setCellValue("Padding Character");
		header.createCell(cellCounter++).setCellValue("Prefix");
		header.createCell(cellCounter++).setCellValue("Suffix");
		header.createCell(cellCounter++).setCellValue("Data type");
		header.createCell(cellCounter++).setCellValue("Date format");
		header.createCell(cellCounter++).setCellValue("Length");

		int rowNum = 1;
		if (!CollectionUtils.isEmpty(attributeList)) {
			int length = attributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				FixedLengthASCIIComposerAttribute attribute = (FixedLengthASCIIComposerAttribute) attributeList.get(i);
				if (attribute != null && !attribute.getStatus().equals(StateEnum.DELETED)) {
					int rowCellCounter = 0;
					Row row = sheet.createRow(rowNum++);
					row.createCell(rowCellCounter++).setCellValue(attribute.getSequenceNumber());
					row.createCell(rowCellCounter++).setCellValue(attribute.getUnifiedField());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDescription());
					row.createCell(rowCellCounter++).setCellValue(attribute.getDefualtValue());
					row.createCell(rowCellCounter++).setCellValue(attribute.getTrimchars());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPaddingType().toString());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPaddingChar());
					row.createCell(rowCellCounter++).setCellValue(attribute.getPrefix());
					row.createCell(rowCellCounter++).setCellValue(attribute.getSuffix());
					if(attribute.getDataType() == null)
						row.createCell(rowCellCounter++).setCellValue("");
					else
						row.createCell(rowCellCounter++).setCellValue(attribute.getDataType().toString());
					row.createCell(rowCellCounter++).setCellValue(attribute.getFixedLengthDateFormat());
					row.createCell(rowCellCounter++).setCellValue(attribute.getFixedLength());
				}
			}
		}
	}
}