package py.com.sodep.mobileforms.license.info;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.NetworkInterface;

import py.com.sodep.mobileforms.license.crypto.CryptoUtils;

public class FormServerInfo {

	public static String getMacAddress() {
		try {

			if (System.getProperty("os.name").startsWith("Windows")) {
				// includes: Windows 2000, Windows 95, Windows 98, Windows NT,
				// Windows Vista, Windows XP
			} else {
				String[] cmd = { "/bin/sh", "-c",
						"route -n | awk '{print $1  \" \" $8}' | grep 0.0.0.0 | awk '{print $2}'" };
				Process process = Runtime.getRuntime().exec(cmd);
				InputStream in = process.getInputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String interfaceName = reader.readLine();
				NetworkInterface interface1 = NetworkInterface.getByName(interfaceName);
				if (interface1 != null) {
					byte[] hardwareAddress = interface1.getHardwareAddress();
					String hexString = CryptoUtils.toHexString(hardwareAddress);
					return hexString;
				}
			}

		} catch (Exception e) {

		}

		return "N/A";
	}

	public static String getHddSerial() {
		return "N/A";
	}

	public static void main(String[] args) {
		String macAddress = getMacAddress();
		System.out.println(macAddress);
	}
}
