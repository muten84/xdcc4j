package it.luigibifulco.xdcc4j.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

public class OsUtils {

	public static String getUserHomeDir() {
		return SystemUtils.USER_HOME;
	}

	public static String getDownloadDir() {
		return getDownloadDir("", true);
	}

	public static String getPathDirSeparator() {
		if (SystemUtils.IS_OS_WINDOWS) {
			return "\\";
		} else
			return "/";
	}

	public static String getDownloadDir(String userDefinedDir,
			boolean useDownloadsPrefix) {
		String userHome = SystemUtils.USER_HOME;
		if (useDownloadsPrefix) {
			userHome = userHome + getPathDirSeparator() + "Downloads";
		}
		if (!StringUtils.isEmpty(userDefinedDir)) {
			userHome = userHome + getPathDirSeparator() + userDefinedDir;
		}
		return userHome;

	}

	public static void main(String[] args) {
		System.out.println(getUserHomeDir());
		System.out.println(getDownloadDir());
	}
}
