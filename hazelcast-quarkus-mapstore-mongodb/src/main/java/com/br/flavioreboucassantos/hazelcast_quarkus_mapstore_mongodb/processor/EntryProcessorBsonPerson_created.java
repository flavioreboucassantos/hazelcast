package com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.processor;

import java.util.Map.Entry;

import com.br.flavioreboucassantos.hazelcast_quarkus_mapstore_mongodb.bson.BsonPerson;
import com.hazelcast.map.EntryProcessor;

public class EntryProcessorBsonPerson_created implements EntryProcessor<Long, BsonPerson, Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 491173554436706337L;

	/*
	 * Vantagens e Detalhes:
	 * Atomicidade: Não é necessário usar locks explicitamente. O processo é atômico por chave.
	 * Performance: Evita serialização/deserialização se o dado estiver no formato OBJECT.
	 * Melhor uso: Ideal para atualizações de contadores, processamento em lote e quando o valor de retorno não é crucial para evitar tráfego.
	 * Cuidado: O EntryProcessor não é thread-safe. Evite estados mutáveis dentro da classe do processador.
	 */

	/*
	 * Quando utilizar EntryProcessor:
	 * Atualizações de Alta Concorrência: Quando múltiplos clientes tentam atualizar a mesma chave simultaneamente e você precisa evitar condições de corrida (race conditions) sem travar o mapa.
	 * Melhoria de Performance: Evita o padrão "get-modify-put", que exige duas viagens de rede (network hops). O EntryProcessor envia apenas a lógica para o membro, reduzindo a latência.
	 * Operações Atômicas: Garante que a operação de leitura e modificação no par chave-valor seja tratada como uma única unidade de trabalho atômica no thread de partição.
	 * Processamento em Lote (Batch): Para aplicar a mesma lógica de negócio a várias entradas (keys) de uma vez.
	 */

	/*
	 * Exemplos Comuns de Uso:
	 * Incrementar um contador (ex: count++).
	 * Adicionar um item a uma lista dentro de um objeto armazenado.
	 * Atualizar um campo específico de um objeto complexo sem precisar trafegar o objeto inteiro pela rede.
	 * O código do EntryProcessor roda localmente no membro que detém o dado, tornando a operação eficiente e thread-safe.
	 */

	@Override
	public Object process(final Entry<Long, BsonPerson> entry) {
		entry.setValue(new BsonPerson(entry.getValue(), System.currentTimeMillis()));
		return true; // A result that will be returned from the method taking the EntryProcessor, such as IMap.executeOnKey()
	}

}
