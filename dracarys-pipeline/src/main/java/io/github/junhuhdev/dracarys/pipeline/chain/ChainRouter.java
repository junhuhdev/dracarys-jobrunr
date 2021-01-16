package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChainRouter<R extends Command.Request> implements Chainable<R> {

	private final List<ChainBase<R>> chains;


	@Override
	public <C extends Command> ChainContext<C> dispatch(R event) throws Exception {
		var chainMatches = chains.stream()
				.filter(chain -> chain.matches(event))
				.collect(toList());
		ChainContext<C> ctx = null;
		log.info("Routing to chains {}", chainMatches.stream().map(r -> r.getClass().getSimpleName()).collect(toList()));
		if (chainMatches.size() > 1) {
			throw new IllegalStateException(String.format("Found more than one chain to route type=%s", event.getClass().getSimpleName()));
		}
		if (isEmpty(chainMatches)) {
			throw new IllegalStateException("No chain to process workflow=" + event.getClass().getSimpleName());
		}
		for (var chain : chainMatches) {
			log.info("Processing chain={} ...", chain.getClass().getSimpleName());
			ctx = chain.dispatch(event);
		}
		return ctx;
	}

}
