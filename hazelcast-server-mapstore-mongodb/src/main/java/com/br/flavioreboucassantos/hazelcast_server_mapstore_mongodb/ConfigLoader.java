package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
	static private Properties properties = new Properties();

	static {
		// Carrega o arquivo application.properties do classpath
		try (InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("application.properties")) {
			if (input == null)
				System.out.println("Desculpe, arquivo application.properties não encontrado.");

			properties.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	static public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
