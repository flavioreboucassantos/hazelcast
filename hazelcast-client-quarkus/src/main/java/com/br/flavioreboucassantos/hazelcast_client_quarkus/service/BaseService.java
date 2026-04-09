package com.br.flavioreboucassantos.hazelcast_client_quarkus.service;

import java.util.Random;

public class BaseService {

	static public String gerarStringAleatoria(final int tamanho) {
		final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		final int length = caracteres.length();
		final Random random = new Random();
		final StringBuilder sb = new StringBuilder(tamanho);

		for (int i = 0; i < tamanho; i++) {
			sb.append(caracteres.charAt(random.nextInt(length)));
		}

		return sb.toString();
	}

}
