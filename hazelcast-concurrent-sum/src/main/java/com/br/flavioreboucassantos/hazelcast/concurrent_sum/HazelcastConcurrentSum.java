package com.br.flavioreboucassantos.hazelcast.concurrent_sum;

import java.util.concurrent.atomic.AtomicInteger;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastConcurrentSum {

	/*
	 * Change Here to Select Between runThreadSafeSumTask or runNonThreadSafeSumTask
	 */
	static public final boolean isThreadSafe = false;

	/*
	 * Configurations of Concurrent Sum
	 */
	static private final int numberOfAdders = 5;
	static private final int numberOfSums = 500;
	static private final long nsTimeBetweenSums = 1000000 * 1; // 1000000 is 1 ms & * 1000 is 1 sec

	static private final AtomicInteger countAddersConcluded = new AtomicInteger(0);
	static private final AtomicInteger countRollbacks = new AtomicInteger(0);

	static private final String mapName = "ConcurrentSum";
	static private final String keyName = "valor";

	static private void print(Object p) {
		System.out.println(p);
	}

	static private HazelcastInstance createHazelcastInstance() {
		final ClientConfig clientConfig = new ClientConfig();
		clientConfig.getNetworkConfig().addAddress("127.0.0.1:5701");
		clientConfig.setClusterName("dev");
//		clientConfig.getSecurityConfig().setCredentials(new UsernamePasswordCredentials("admin", "senha"));
		final HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
		return client;
	}

	static private boolean allAddersConcluded() {
		return (countAddersConcluded.get() < numberOfAdders) ? false : true;
	}

	static public void main(String[] args) throws InterruptedException {
		final HazelcastInstance hazelcastInstance = createHazelcastInstance();
		final IMap<Object, Integer> map = hazelcastInstance.getMap(mapName);
		map.destroy();
		map.put(keyName, 0);

		for (int i = 0; i < numberOfAdders; i++) {
			Thread thread = new Thread(new HazelcastRunnableConcurrentSum(createHazelcastInstance(), mapName, keyName, numberOfSums, nsTimeBetweenSums));
			thread.start();
		}

		final long msTimeOfStart = System.currentTimeMillis();

		while (!allAddersConcluded())
			Thread.yield();

		final long msTimeToFinish = System.currentTimeMillis() - msTimeOfStart;

		final int valorEsperado = numberOfAdders * numberOfSums;
		final int valorEncontrado = map.get(keyName);

		hazelcastInstance.shutdown();

		Thread.sleep(1000); // wait to finish hazelcast log
		print("Tempo gasto: " + msTimeToFinish + " milliseconds");
		print("Valor esperado: " + valorEsperado);
		print("Valor encontrado: " + valorEncontrado);
		print("Rollbacks realizados: " + countRollbacks.get());
	}

	static public void adderConcluded(final int _countRollbacks) {
		countRollbacks.addAndGet(_countRollbacks);
		countAddersConcluded.incrementAndGet();
	}
}
