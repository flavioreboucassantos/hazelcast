package com.br.flavioreboucassantos.hazelcast.concurrent_sum;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastRunnableConcurrentSum implements Runnable {

	private final int numberOfSums;
	private final long nsTimeBetweenSums;

	final HazelcastInstance hazelcastInstance;
	final IMap<Object, Integer> map;
	final String keyName;

	/*
	 * Non Thread Safe Sum Task
	 */
	private void nonThreadSafeSumTask() throws Exception {
		Integer integer = map.get(keyName);
		integer++;
		map.put(keyName, integer);
	}

	private void runNonThreadSafeSumTask() {
		try {
			// sum every nsTimeBetweenSums
			int numberOfSum = 0;
			while (numberOfSum++ < numberOfSums) {
				nonThreadSafeSumTask();
				final long timeNextRun = System.nanoTime() + nsTimeBetweenSums;
//				System.out.println(numberOfSum + " >>> " + timeNextRun);
				while (System.nanoTime() < timeNextRun)
					Thread.yield();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HazelcastConcurrentSum.adderConcluded(0);
		}
	}

	/*
	 * Thread Safe Sum Task
	 */
	private boolean threadSafeSumTask() throws Exception {
		map.lock(keyName);

		try {
			Integer integer = map.get(keyName);
			integer++;
			map.put(keyName, integer);
			return true;

		} catch (Exception e) {

			throw e;
		} finally {

			map.unlock(keyName);
		}
	}

	private void runThreadSafeSumTask() {
		int numberOfSum = 0;
		int countRollbacks = 0;
		try {
			// sum every nsTimeBetweenSums
			while (numberOfSum++ < numberOfSums) {
				threadSafeSumTask();
				final long timeNextRun = System.nanoTime() + nsTimeBetweenSums;
//				System.out.println(numberOfSum + " >>> " + timeNextRun);
				while (System.nanoTime() < timeNextRun)
					Thread.yield();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			HazelcastConcurrentSum.adderConcluded(countRollbacks);
		}
	}

	public HazelcastRunnableConcurrentSum(final HazelcastInstance hazelcastInstance, final String mapName, final String keyName, final int numberOfSums,
			final long nsTimeBetweenSums) {
		this.numberOfSums = numberOfSums;
		this.nsTimeBetweenSums = nsTimeBetweenSums;

		this.hazelcastInstance = hazelcastInstance;
		map = hazelcastInstance.getMap(mapName);
		this.keyName = keyName;
	}

	@Override
	public void run() {
		try {

			if (HazelcastConcurrentSum.isThreadSafe)
				runThreadSafeSumTask();
			else
				runNonThreadSafeSumTask();

		} finally {
			hazelcastInstance.shutdown();
		}
	}

}
