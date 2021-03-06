package io.github.junhuhdev.dracarys.pipeline.middleware.log;

import io.github.junhuhdev.dracarys.jobrunr.jobs.context.JobRunrDashboardLogger;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import java.time.Duration;
import java.time.Instant;

import static com.machinezoo.noexception.Exceptions.sneak;

@RequiredArgsConstructor
@Order(1)
public class LogMiddleware implements Command.Middleware {

	private final CorrelationId correlationId;

	private <C extends Command.Handler> Logger logger(C command) {
		return new JobRunrDashboardLogger(LoggerFactory.getLogger(command.getClass()));
	}

	@SuppressWarnings("Unchecked")
	@Override
	public <C extends Command.Handler> ChainContext invoke(C command, ChainContext ctx, Next next) throws Exception {
		var log = logger(command);
		return correlationId.wrap(() -> {
			Instant start = Instant.now();
			log.info("---> Started cmd={}, id={}, input={}",
					command.getClass().getDeclaringClass().getSimpleName(),
					ctx.getFirst(Command.Request.class).getReferenceId(),
					ctx.getLast());
			ChainContext response = sneak().get(next::invoke);
			if (response.hasFault()) {
				log.error("<--- Failed cmd={}, id={} with error={} took {} ms",
						command.getClass().getDeclaringClass().getSimpleName(),
						ctx.getFirst(Command.Request.class).getReferenceId(),
						ctx.getLast(),
						Duration.between(start, Instant.now()).toMillis());
				return response;
			}
			log.info("<--- Completed cmd={}, id={} took {} ms",
					command.getClass().getDeclaringClass().getSimpleName(),
					ctx.getFirst(Command.Request.class).getReferenceId(),
					Duration.between(start, Instant.now()).toMillis());
			return response;
		});
	}

}
