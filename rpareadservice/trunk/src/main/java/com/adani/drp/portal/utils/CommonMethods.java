package com.adani.drp.portal.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.adani.drp.portal.configurations.PortalProperties;
import com.adani.drp.portal.models.responses.survey.ApiResponse;
import com.adani.drp.portal.services.UnitService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonMethods {

	private CommonMethods() {
	}

	public static Timestamp cnvStrToTimestamp(String strTimeStmp) { // convert String to Timestamp
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
		Timestamp timestamp = null;
		Date parsedDate;
		try {
			parsedDate = (Date) dateFormat.parse(strTimeStmp);
			timestamp = new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	public static Date cnvStrToDate(String dateString) {

		String dateFormatPattern = "yyyy-MM-dd";
		Date sqlDate = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatPattern);

		try {
			java.util.Date utilDate = dateFormat.parse(dateString);
			sqlDate = new Date(utilDate.getTime());
		} catch (ParseException e) {
			log.error("Error parsing the date: " + e.getMessage());
		}
		return sqlDate;
	}

	public static boolean isValidMobileNumber(String mobileNumber) {

//		String regex = "^[6-9]\\d{9}$";
		String regex = "^(\\+91)?[6-9]\\d{9}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(mobileNumber);

		return matcher.matches();
	}

	public static String cnvDateToPattern(String format, Date date) {

		try {
			String formattedDate = "";
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			if (date != null) {
				// Format the java.sql.Date to a string
				formattedDate = dateFormat.format(date);
			}
			return formattedDate;
		} catch (Exception e) {
			return null;
		}
	}

	public static String cnvTimeStampToPattern(String format, Timestamp timeStamp) {
		try {
			String formattedDate = "";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
			if (timeStamp != null) {
				formattedDate = timeStamp.toLocalDateTime().format(formatter);
			}
			return formattedDate;
		} catch (Exception e) {
			return null;
		}
	}

	public static String formatDate(java.util.Date date) {
		if (date == null) {
			return null;
		}

		// Customize the date format as per your requirement
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(date);
	}

	public static String formatDateOnly(java.util.Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

		return sdf.format(date);
	}

	public static String formatDateOnlyWithSlashes(java.util.Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		return sdf.format(date);
	}

	public static String formatDateOnlyWithDashes(java.util.Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = sdf.format(date);

		// Extract the date part and reformat it to dd-MM-yyyy
		String datePart = formattedDate.substring(0, 10); // yyyy-MM-dd
		String[] dateComponents = datePart.split("-");
		String reformattedDate = dateComponents[2] + "-" + dateComponents[1] + "-" + dateComponents[0]; // dd-MM-yyyy

		return reformattedDate;
	}

	public static String formatDateString(String timestampString) {
		if (timestampString == null || timestampString.isEmpty()) {
			return "";
		}

		try {
			long timestamp = Long.parseLong(timestampString);

			// Convert the timestamp to Instant
			Instant instant = Instant.ofEpochMilli(timestamp);

			// Convert Instant to LocalDateTime in UTC timezone
			LocalDateTime dateTime = LocalDateTime.ofInstant(instant, null);

			// Define the desired date-time format
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

			// Format LocalDateTime to a string using the defined format
			String formattedDateTime = dateTime.format(formatter);
			return formattedDateTime;

		} catch (Exception e) {
			// Handle parsing exception
			log.error("error parsing date timestamp");
			return "null";
		}
	}

	public static String formatDateWithoutTime(java.util.Date dateWithTime) {
		if (dateWithTime == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(dateWithTime);
	}

	public static String formatDateWithTime(java.util.Date dateWithTime) {
		LocalDateTime localDateTime = dateWithTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalTime time = localDateTime.toLocalTime();
		return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
	}

	public static String HTMLTagsRemover(String untrustedHTML) {
		untrustedHTML = untrustedHTML.replaceAll("<[^>]*>", "");
		return untrustedHTML;
	}

	public static String maskAadhar(String aadharNumber) {
		// Check if the Aadhar number is valid
		if (aadharNumber == null || aadharNumber.length() != 12) {
			return "";
		}

		// Mask all digits except the last four
		StringBuilder maskedAadhar = new StringBuilder();
		for (int i = 0; i < aadharNumber.length() - 4; i++) {
			maskedAadhar.append('X');
		}

		// Append last four digits
		maskedAadhar.append(aadharNumber.substring(aadharNumber.length() - 4));

		return maskedAadhar.toString();
	}

	public static String marathiNumerics(String data) {

		if (data == null || data.isEmpty()) {
			return "";
		}
//		else if (data.isEmpty()) {
//			data = "0";
//		}
		data = data.replace("0", "०").replace("1", "१").replace("2", "२").replace("3", "३").replace("4", "४")
				.replace("5", "५").replace("6", "६").replace("7", "७").replace("8", "८").replace("9", "९");

		return data;
	}

	private static final String[] array_two = { "^", "*", "Þ", "ß", "¼", "½", "¿", "À", "¾", "A", "\\", "&", "&", "Œ",
			"]", "-", "~ ", "å", "ƒ", "„", "…", "†", "‡", "ˆ", "‰", "Š", "‹", "Û", "$", "(", "&",

			// "¶","d","[k","x","T","t","M+","<+","Q",";","j","u",
			"¶+", "d+", "[k+", "x+", "T+", "t+", "M+", "<+", "Q+", ";+", "j+", "u+", "Ù", "Ùk", "ä", "–", "—",

			"Üo", "à", "á", "â", "ã", "ºz", "º", "í", "{", "{k", "«", "=", "K", "Nî", "Vî", "Bî", "Mî", "<î", "|", "}",
			"J", "Vª", "Mª", "<ªª", "Nª", "Ø", "Ý", "æ", "ç", "xz", "#", ":", "z",

			"vks", "vkS", "vk", "v", "bZ", "b", "m", "Å", ",s", ",", "_",

			"D", "d", "ô", "[", "[k", "X", "x", "?", "?k", "³", "pkS", "P", "p", "N", "T", "t", "÷", ">", "¥",

			"ê", "ë", "V", "B", "ì", "ï", "M", "<", ".", ".k", "R", "r", "F", "Fk", ")", "n", "è", "èk", "U", "u",

			"I", "i", "¶", "Q", "C", "c", "H", "Hk", "E", "e", "¸", ";", "j", "Y", "y", "G", "O", "o", "'", "'k", "\"",
			"\"k", "L", "l", "g",

			"v‚", "‚", "ks", "kS", "k", "h", "q", "w", "`", "s", "S", "a", "¡", "%", "W", "·", "~ ", "~", "+", "@" };

	private static final String[] array_one = { // ignore all nuktas except in ड़ and ढ़
			"‘", "’", "“", "”", "(", ")", "{", "}", "=", "।", "?", "-", "µ", "॰", ",", ".", "् ", "०", "१", "२", "३",
			"४", "५", "६", "७", "८", "९", "x", "+", ";", "_",

			"फ़्", "क़", "ख़", "ग़", "ज़्", "ज़", "ड़", "ढ़", "फ़", "य़", "ऱ", "ऩ", // one-byte nukta varNas
			"त्त्", "त्त", "क्त", "दृ", "कृ",

			"श्व", "ह्न", "ह्य", "हृ", "ह्म", "ह्र", "ह्", "द्द", "क्ष्", "क्ष", "त्र्", "त्र", "ज्ञ", "छ्य", "ट्य",
			"ठ्य", "ड्य", "ढ्य", "द्य", "द्व", "श्र", "ट्र", "ड्र", "ढ्र", "छ्र", "क्र", "फ्र", "द्र", "प्र", "ग्र",
			"रु", "रू", "्र",

			"ओ", "औ", "आ", "अ", "ई", "इ", "उ", "ऊ", "ऐ", "ए", "ऋ",

			"क्", "क", "क्क", "ख्", "ख", "ग्", "ग", "घ्", "घ", "ङ", "चै", "च्", "च", "छ", "ज्", "ज", "झ्", "झ", "ञ",

			"ट्ट", "ट्ठ", "ट", "ठ", "ड्ड", "ड्ढ", "ड", "ढ", "ण्", "ण", "त्", "त", "थ्", "थ", "द्ध", "द", "ध्", "ध",
			"न्", "न",

			"प्", "प", "फ्", "फ", "ब्", "ब", "भ्", "भ", "म्", "म", "य्", "य", "र", "ल्", "ल", "ळ", "व्", "व", "श्", "श",
			"ष्", "ष", "स्", "स", "ह",

			"ऑ", "ॉ", "ो", "ौ", "ा", "ी", "ु", "ू", "ृ", "े", "ै", "ं", "ँ", "ः", "ॅ", "ऽ", "् ", "्", "़", "/" };

	private static final void doConvert(StringBuffer strUnicode, String modifiedsubstring) {

		String modified_substring = modifiedsubstring;
		// if string to be converted is non-blank then no need of any processing.
		if (!modified_substring.isEmpty()) {
			// first replace the two-byte nukta_varNa with corresponding one-byte nukta
			// varNas.
			modified_substring = modified_substring.replace("त्र्य", "«य");
			modified_substring = modified_substring.replace("श्र्य", "Ü‍‍zय");

			modified_substring = modified_substring.replace("क़", "क़");
			modified_substring = modified_substring.replace("ख़‌", "ख़");
			modified_substring = modified_substring.replace("ग़", "ग़");
			modified_substring = modified_substring.replace("ज़", "ज़");
			modified_substring = modified_substring.replace("ड़", "ड़");
			modified_substring = modified_substring.replace("ढ़", "ढ़");
			modified_substring = modified_substring.replace("ऩ", "ऩ");
			modified_substring = modified_substring.replace("फ़", "फ़");
			modified_substring = modified_substring.replace("य़", "य़");
			modified_substring = modified_substring.replace("ऱ", "ऱ");

			// code for replacing "ि" (chhotee ee kii maatraa) with "f" and correcting its
			// position too.
			int position_of_f = modified_substring.indexOf("ि");
			while (position_of_f != -1) {
				char character_left_to_f = modified_substring.charAt(position_of_f - 1);
				modified_substring = modified_substring.replace(character_left_to_f + "ि", "f" + character_left_to_f);

				position_of_f = position_of_f - 1;

				while ((position_of_f != 0) && (modified_substring.charAt(position_of_f - 1) == '्')) {
					String string_to_be_replaced = modified_substring.charAt(position_of_f - 2) + "्";
					modified_substring = modified_substring.replace(string_to_be_replaced + "f",
							"f" + string_to_be_replaced);

					position_of_f = position_of_f - 2;
				}
				position_of_f = modified_substring.indexOf("ि", position_of_f + 1); // search for f ahead of the current
																					// position.
			}

			// Eliminating "र्" and putting Z at proper position for this.
			String set_of_matras = "ािीुूृेैोौं:ँॅ";

			modified_substring += "  "; // add two spaces after the string to avoid UNDEFINED char in the following
										// code.

			int position_of_half_R = modified_substring.indexOf("र्");
			while (position_of_half_R > 0) {
				// "र्" is two bytes long
				int probable_position_of_Z = position_of_half_R + 2;
				char character_at_probable_position_of_Z = modified_substring.charAt(probable_position_of_Z);

				// trying to find non-maatra position right to probable_position_of_Z.
				while (set_of_matras.contains(String.valueOf(character_at_probable_position_of_Z))) {
					probable_position_of_Z = probable_position_of_Z + 1;
					character_at_probable_position_of_Z = modified_substring.charAt(probable_position_of_Z);
				}

				// check if the next character is a halant
				int right_to_position_of_Z = probable_position_of_Z + 1;
				if (right_to_position_of_Z > 0) {
					char character_right_to_position_of_Z = modified_substring.charAt(right_to_position_of_Z);
					while (character_right_to_position_of_Z == '्') {
						// halant found, move to next character
						probable_position_of_Z = right_to_position_of_Z + 1;
						character_at_probable_position_of_Z = modified_substring.charAt(probable_position_of_Z);
						right_to_position_of_Z = probable_position_of_Z + 1;
						character_right_to_position_of_Z = modified_substring.charAt(right_to_position_of_Z);
					}

//	                System.out.println("modified_substring :"+modified_substring);
//	                System.out.println("position_of_half_R :"+position_of_half_R);
//	                System.out.println("probable_position_of_Z :"+probable_position_of_Z);
//	                modified_substring.length();
//	                System.out.println("modified_substring.length() :"+modified_substring.length());
//	                String string_to_be_replaced = modified_substring.substring(position_of_half_R + 2, (probable_position_of_Z - position_of_half_R) - 1);
//	                String string_to_be_replaced = modified_substring.substring(position_of_half_R + 2, (probable_position_of_Z + position_of_half_R));
//	                String string_to_be_replaced = modified_substring.substring ( probable_position_of_Z, probable_position_of_Z +1) ;
					String string_to_be_replaced = modified_substring.substring(position_of_half_R + 2,
							(position_of_half_R + 2) + ((probable_position_of_Z - position_of_half_R) - 1));
					modified_substring = modified_substring.replace("र्" + string_to_be_replaced,
							string_to_be_replaced + "Z");
					position_of_half_R = modified_substring.indexOf("र्");
				}
			}

			modified_substring = modified_substring.substring(0, modified_substring.length() - 2);

			// substitute array_two elements in place of corresponding array_one elements
			for (int input_symbol_idx = 0; input_symbol_idx < array_one.length; input_symbol_idx++) {
				int idx = 0; // index of the symbol being searched for replacement
				while (idx != -1) {
					modified_substring = modified_substring.replace(array_one[input_symbol_idx],
							array_two[input_symbol_idx]);
					idx = modified_substring.indexOf(array_one[input_symbol_idx]);
				}
			}
		}

		modified_substring = modified_substring.replace("Zksa", "ksZa");
		modified_substring = modified_substring.replace("~ Z", "Z~");
		modified_substring = modified_substring.replace("Zk", "kZ");
		modified_substring = modified_substring.replace("Zh", "Ê");

		strUnicode.append(modified_substring);
	}

	public static String convertToKrutidev(String krutiString) {
		String strKtd = krutiString;
		StringBuffer sbUnicode = new StringBuffer();

		int text_size = strKtd.length();
		int sthiti1 = 0, sthiti2 = 0, chale_chalo = 1;
		int max_text_size = 6000;

		while (chale_chalo == 1) { // chale_chalo == 1
			sthiti1 = sthiti2; // sthiti1 == 0,sthiti2 == 0

			if (sthiti2 < (text_size - max_text_size)) {
				sthiti2 += max_text_size;
				while (strKtd.charAt(sthiti2) != ' ') {
					sthiti2--;
				}
			} else {
				sthiti2 = text_size; // sthiti2 == 9 text_size==9 chale_chalo ==0
				chale_chalo = 0;
			}

			String modified_substring = strKtd.substring(sthiti1, sthiti2);

			doConvert(sbUnicode, modified_substring);
		}

		return sbUnicode.toString();
	}

	public static String extractDate(String dateTimeString) {
		if (dateTimeString == null || dateTimeString.isEmpty()) {
			return "";
		}

		String regexPattern = "(\\d{4}-\\d{2}-\\d{2})";
		Pattern pattern = Pattern.compile(regexPattern);
		Matcher matcher = pattern.matcher(dateTimeString);

		if (matcher.find()) {
			String date = matcher.group(1);
			return rearrangeDate(date);
		} else {
			return "";
		}
	}

	private static String rearrangeDate(String date) {
		String[] parts = date.split("-");
		if (parts.length == 3) {
			return parts[2] + "-" + parts[1] + "-" + parts[0];
		} else {
			return "";
		}
	}

	public static String generateGlobalId() {
		UUID uuid = UUID.randomUUID();
		return "{" + uuid.toString().toUpperCase() + "}";
	}

	public static boolean verifyAuthentication(Map<String, String> headers, PortalProperties portalProperties) {
		if (headers == null || headers.isEmpty() || headers.get("access-token") == null
				|| headers.get("access-token").isEmpty()) {
			return false;
		}

		String accessToken = headers.get("access-token").contains("@")
				? headers.get("access-token").substring(0, headers.get("access-token").indexOf('@'))
				: headers.get("access-token");

		String inHouseAuthHeader = (headers.get("inhouse-autho") != null && !headers.get("inhouse-autho").isBlank())
				? headers.get("inhouse-autho").trim()
				: "";

		boolean isAuthorised;

		if (inHouseAuthHeader.equalsIgnoreCase("false")) {
			log.info("Calling odoo re-authentication service");
			ApiResponse authResp = UnitService.callExternalService(accessToken, portalProperties);

			if (authResp.getStatus().getStatus() == 1 && authResp.getData() != null) {
				log.info("Odoo authentication successful: " + authResp.getData());
				isAuthorised = true;
			} else {
				log.error("Odoo authentication failed: " + authResp.getData());
				isAuthorised = false;
			}
		} else {
			log.info("Calling in-house authentication");
			isAuthorised = accessToken.equals(portalProperties.getExpectedAccessToken());
		}

		return isAuthorised;
	}

	public static java.util.Date parseDate(String dateString) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			return formatter.parse(dateString);

		} catch (Exception e) {
			log.info("unable to parse date {} to sql date", dateString);
			return null;
		}
	}
	
	public static java.util.Date parseDateTime(String dateString) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return formatter.parse(dateString);

		} catch (Exception e) {
			log.info("unable to parse date {} to sql date", dateString);
			return null;
		}
	}

}
