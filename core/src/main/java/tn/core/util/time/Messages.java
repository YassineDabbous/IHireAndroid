package tn.core.util.time;

/**
 * Messages class
 */
public class Messages {
	private Messages() {
	}

	/**
	 * Get string for key
	 *
	 * @param key
	 * @return string
	 */
	public static String getString(String key) {
		String text = "AGO";
		switch (key){
			case "AGO": text = "منذ"; break;
			case "FROM_NOW": text = "الان"; break;
			case "SECONDS": text = "ثواني"; break;
			case "MINUTE": text = "دقيقة"; break;
			case "MINUTES": text = "دقائق"; break;
			case "HOUR": text = "ساعة"; break;
			case "HOURS": text = "ساعات"; break;
			case "DAY": text = "يوم"; break;
			case "DAYS": text = "ايام"; break;
			case "MONTH": text = "شهر"; break;
			case "MONTHS": text = "شهور"; break;
			case "YEAR": text = "سنة"; break;
			case "YEARS": text = "سنوات"; break;
			default: break;
		}
		return text;
	}
	/*
	* for English Lang
			case "AGO": text = "AGO"; break;
			case "FROM_NOW": text = "from now"; break;
			case "SECONDS": text = "SECONDS"; break;
			case "MINUTE": text = "MINUTE"; break;
			case "MINUTES": text = "MINUTES"; break;
			case "HOUR": text = "HOUR"; break;
			case "HOURS": text = "HOURS"; break;
			case "DAYS": text = "DAYS"; break;
			case "MONTH": text = "MONTH"; break;
			case "MONTHS": text = "MONTHS"; break;
			case "YEAR": text = "YEAR"; break;
			case "YEARS": text = "YEARS"; break;
	  */
}
