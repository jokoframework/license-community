package py.com.sodep.mobileforms.license.json.notification;

public class MFFormServerNotification {

	public static final int NOTIFICATION_STARTUP = 1;

	private Long serverId;

	private Long serverLicenseId;

	private String hddSerial;

	private String macAddress;

	private Integer notificationType;

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
	}

	public String getHddSerial() {
		return hddSerial;
	}

	public void setHddSerial(String hddSerial) {
		this.hddSerial = hddSerial;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public Integer getNotificationType() {
		return notificationType;
	}

	public void setNotificationType(Integer notificationType) {
		this.notificationType = notificationType;
	}

	public Long getServerLicenseId() {
		return serverLicenseId;
	}

	public void setServerLicenseId(Long serverLicenseId) {
		this.serverLicenseId = serverLicenseId;
	}

}
