package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

public class MapFnWebhookCallback implements FunctionEx<String, String> {

	/*
	 * mapFn:
	 * A mapping stage which applies the given function to each input item independently and emits the function's result as the output item.
	 * If the result is null , it emits nothing.
	 * Therefore, this stage can be used to implement filtering semantics as well.
	 */

	static private final ILogger LOG = Logger.getLogger(MapFnWebhookCallback.class);

	@Override
	public String applyEx(final String input) throws Exception {
		LOG.info("\nMapFn::applyEx::input=" + input);

		return "(...mapFn...)";
	}

}
