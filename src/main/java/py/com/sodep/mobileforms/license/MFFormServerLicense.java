package py.com.sodep.mobileforms.license;

import java.util.Map;

public interface MFFormServerLicense extends MFLicense {
	
	Long getId();
	
	Long getServerId();

	Long getMaxApplications();

	Map<String, String> getProperties();

	MFApplicationLicense getDefaultApplicationLicense();

}