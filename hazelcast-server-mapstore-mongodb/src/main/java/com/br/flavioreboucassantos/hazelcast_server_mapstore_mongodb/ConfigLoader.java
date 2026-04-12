package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public final class ConfigLoader {
	static private final Properties properties = new Properties();

	static {
		// Carrega o arquivo application.properties do classpath
		try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (inputStream == null)
				System.out.println("Desculpe, arquivo application.properties não encontrado.\n");

			properties.load(inputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// Carrega o arquivo hazelcast.license-key do classpath
		try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("hazelcast.license-key")) {
			if (inputStream == null)
				System.out.println("Desculpe, arquivo hazelcast.license-key não encontrado.\n");

			String licenseKey = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
			properties.setProperty("hazelcast.license-key", licenseKey);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static public final String getProperty(String key) {
		return properties.getProperty(key);
	}
}
