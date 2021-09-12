package py.com.sodep.mobileforms.license;

import java.util.Date;

public interface MFLicense {
	
	final String DATE_FORMAT = "yyyy-MM-dd";
	
	final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	Date getCreationDate();

	Long getValidDays();

}
