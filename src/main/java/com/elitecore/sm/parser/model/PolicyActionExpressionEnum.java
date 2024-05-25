package com.elitecore.sm.parser.model;

// TODO: Auto-generated Javadoc
/**
 * The Enum PolicyActionExpression. This enumeration is used to store function name and length for policy action
 * @author Elitecore
 */
public enum PolicyActionExpressionEnum {


	/** The abs. */
	ABS("abs(",1),
	
	/** The add. */
	ADD("add(",3),
	
	/** The floor. */
	FLOOR("floor(",1),
	
	/** The getdatedifference. */
	GETDATEDIFFERENCE("getDateDifference(",1),
	
	/** The man. */
	MAN("max(",3),
	
	/** The min. */
	MIN("min(",3),
	
	/** The random. */
	RANDOM("random(",1),
	
	/** The round. */
	ROUND("round(",1),
	
	/** The concat. */
	CONCAT("concat(",2),
	
	/** The copy. */
	COPY("copy(",1),
	
	/** The dateformat. */
	DATEFORMAT("dateFormat(",2),
	
	/** The extract. */
	EXTRACT("extract(",3),
	
	/** The format date to long. */
	formatDateToLong("formatDateToLong(",2),
	
	/** The hextodec. */
	HEXTODEC("hexToDec(",1),
	
	/** The hextostring. */
	HEXTOSTRING("hexToString(",1),
	
	/** The istimebetween. */
	ISTIMEBETWEEN("isTimeBetween(",2),
	
	/** The trim. */
	TRIM("trim(",1),
	
	/** The righttrim. */
	RIGHTTRIM("rightTrim(",2),
	
	/** The lefttrim. */
	LEFTTRIM("leftTrim(",2),
	
	/** The length. */
	LENGTH("length(",1),
	
	/** The touppercase. */
	TOUPPERCASE("toUpperCase(",1),
	
	/** The tolowercase. */
	TOLOWERCASE("toLowerCase(",1),
	
	/** The netmatch. */
	NETMATCH("netmatch(",2),
	
	/** The removefiller. */
	REMOVEFILLER("removeFiller(",1),
	
	/** The replace. */
	REPLACE("replace(",3),
	
	/** The replaceall. */
	REPLACEALL("replaceAll(",3),
	
	/** The replacefirst. */
	REPLACEFIRST("replaceFirst(",3),
	
	/** The substring. */
	SUBSTRING("subString(",3),
	
	/** The strip. */
	STRIP("strip(",3),
	
	/** The swapnibble. */
	SWAPNIBBLE("swapNibble(",1),
	
	/** The formatedateaccordingtogmt. */
	FORMATEDATEACCORDINGTOGMT("formateDateAccordingToGMT(",3),
	
	/** The merge. */
	MERGE("merge(",3),
	
	/** The substringwithchars. */
	SUBSTRINGWITHCHARS("subStringWithChars(",3),
	
	/** The lpad. */
	LPAD("lpad(",3),
	
	/** The rpad. */
	RPAD("rpad(",3),
	
	/** The longToDate. */
	LONGTODATE("longToDate(",2),
	
	SIN("sin(", 1),
	COS("cos(", 1),
	TAN("tan(", 1),
	ASIN("asin(", 1),
	ACOS("acos(", 1),
	ATAN("atan(", 1),
	TORADIANS("toRadians(", 1),
	TODEGREES("toDegrees(", 1),
	IEEEREMAINDER("IEEEremainder(", 1),
	ATAN2("atan2(", 1),
	SINH("sinh(", 1),
	COSH("cosh(", 1),
	TANH("tanh(", 1),
	HYPOT("hypot(", 1),
	EXPM1("expm1(", 1),
	lOG1P("log1p(", 1),
	ULP("ulp(", 1),
	SIGNUM("signum(", 1),
	NEXTAFTER("nextAfter(", 1),
	NEXTUP("nextUp(", 1),
	NEXTDOWN("nextDown(", 1),
	GETEXPONENT("getExponent(", 1),
	POW("pow(", 1),
	SUBTRACTEXACT("subtractExact(", 1),
	MULTIPLYEXACT("multiplyExact(", 1),
	INCREMENTEXACT("incrementExact(", 1),
	DECREMENTEXACT("decrementExact(", 1),
	NEGATEEXACT("negateExact(", 1),
	TOINTEXACT("toIntExact(", 1),
	FLOORDIV("floorDiv(", 1),
	FLOORMOD("floorMod(", 1),
	CBRT("cbrt(", 1),
	EXP("exp(", 1),
	LOG("log(", 1),
	LOG10("log10(", 1),
	SQRT("sqrt(", 1),
	RINT("rint(", 1),
	COPYSIGN("copySign(", 1),
	
	CODE_POINT_AT("codePointAt(", 2),
	CODE_POINT_BEORE("codePointBefore(", 2),
	CODE_POINT_COUNT("codePointCount(", 3),
	OFFSET_BY_CODE_POINT("offsetByCodePoints(", 3),
	SUB_SEQUENCE("subSequence(", 3),
	INDEX_OF("indexOf(", 2),
	LAST_INDEX_OF("lastIndexOf(", 2),	
	CHAR_AT("charAt(", 2),
	COMPARE_TO("compareTo(", 2),
	VALUE_OF("valueOf(", 2),
	
	;

	/** The function name. */
	private String functionName;
	
	/** The function lenght. */
	private int functionLenght;

	/**
	 * Instantiates a new policy action expression enum.
	 *
	 * @param functionName the function name
	 * @param functionLenght the function lenght
	 */
	private PolicyActionExpressionEnum(String functionName, int functionLenght) {
		this.functionName = functionName;
		this.functionLenght = functionLenght;
	}

	/**
	 * Gets the function name.
	 *
	 * @return the function name
	 */
	public String getFunctionName() {
		return functionName;
	}

	/**
	 * Gets the function lenght.
	 *
	 * @return the function lenght
	 */
	public int getFunctionLenght() {
		return functionLenght;
	}
}
