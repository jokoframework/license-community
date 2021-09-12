package py.com.sodep.mobileforms.license;


public interface MFApplicationLicense extends MFLicense {
	
	Long getApplicationId();

	Long getMaxDevices();

	Long getMaxUsers();

	/**
	 * Returns the email address of the owner
	 * 
	 * @return
	 */
	String getOwner();

}