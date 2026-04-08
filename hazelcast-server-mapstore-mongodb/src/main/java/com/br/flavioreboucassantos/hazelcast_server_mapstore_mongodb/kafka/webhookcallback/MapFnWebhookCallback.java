package com.br.flavioreboucassantos.hazelcast_server_mapstore_mongodb.kafka.webhookcallback;

import com.hazelcast.function.FunctionEx;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

/**
 * mapFn:<br>
 * A mapping stage which applies the given function to each input item independently and emits the function's result as the output item.<br>
 * If the result is null , it emits nothing.<br>
 * Therefore, this stage can be used to implement filtering semantics as well.
 */
public class MapFnWebhookCallback implements FunctionEx<String, String> {

	static private final long serialVersionUID = -655739658896381549L;

	static private final ILogger LOG = Logger.getLogger(MapFnWebhookCallback.class);

	@Override
	public String applyEx(final String input) throws Exception {
		LOG.info("\nMapFn::applyEx::input=" + input);

		return "(...mapFn...)";
	}

}
