package cn.homjie.boot.configure;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

public class DateFormatter implements Formatter<Date> {

	private static final Logger log = LoggerFactory.getLogger(DateFormatter.class);

	private static final String[] parsePatterns = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };

	private static final DateFormatter formatter = new DateFormatter();

	private DateFormatter() {
	}

	public static DateFormatter instance() {
		return formatter;
	}

	@Override
	public String print(Date object, Locale locale) {
		String text = DateFormatUtils.format(object, "yyyy-MM-dd HH:mm:ss");
		log.info("to print {}", text);
		return text;
	}

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		log.info("to parse {}", text);
		return DateUtils.parseDate(text, parsePatterns);
	}

}
