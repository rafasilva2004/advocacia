package br.com.escritorio.advocacia.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropertiesUtil {
	private static ResourceBundle BUNDLE_CONFIG;
	private static ResourceBundle BUNDLE_MESSAGES;

	/**
	 * Inicializa os bundles
	 * @param strBundleConfig o caminho para o bundle do arquivo de configuracao
	 * @param strBundleMessages o caminho para o bundle do arquivo de mensagens
	 */
	public static void init(String strBundleConfig, String strBundleMessages) {
		try {
			BUNDLE_CONFIG  = ResourceBundle.getBundle(strBundleConfig);
			BUNDLE_MESSAGES  = ResourceBundle.getBundle(strBundleMessages);
		} catch (MissingResourceException e) {
			System.out.println("Erro ao iniciar PropertiesUtil: "+e.getMessage());
			e.printStackTrace();
		}
	}

	public static String getConfig(String key) {
		String valor = null;
		try {
			valor =  BUNDLE_CONFIG.getString(key);
		} catch (MissingResourceException e) {
			valor = '!' + key + '!';
		}
		return valor;
	}

	public static String getConfig(String key, Object... params) {
		String valor = null;
		try {
			valor = MessageFormat.format(BUNDLE_CONFIG.getString(key), params);
		} catch (MissingResourceException e) {
			valor = '!' + key + '!';
		}
		return valor;
	}
	
	public static String getMessage(String key) {
		String valor = null;
		try {
			valor = BUNDLE_MESSAGES.getString(key);
		} catch (MissingResourceException e) {
			valor = '!' + key + '!';
		}
		return valor;
	}

	public static String getMessage(String key, Object... params) {
		String valor = null;
		try {
			valor = MessageFormat.format(BUNDLE_MESSAGES.getString(key), params);
		} catch (MissingResourceException e) {
			valor = '!' + key + '!';
		}
		return valor;
	}
}
