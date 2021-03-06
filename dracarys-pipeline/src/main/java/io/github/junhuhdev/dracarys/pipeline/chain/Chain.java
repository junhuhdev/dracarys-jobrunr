package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.common.StreamSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;

import java.util.ListIterator;

public class Chain {

	private static final Logger log = LoggerFactory.getLogger(Chain.class);
	private final ListIterator<Command.Handler> commands;
	private final StreamSupplier<Command.Middleware> middlewares;

	public Chain(ListIterator<Command.Handler> commands, ObjectProvider<Command.Middleware> middlewares) {
		this.commands = commands;
		this.middlewares = middlewares::orderedStream;
	}

	/** Recusively calls itself until all cmds of chain have executed */
	public ChainContext proceed(ChainContext ctx) throws Exception {
		if (!commands.hasNext()) {
			return completeChain();
		}
		Command.Handler command = commands.next();
		Command.Middleware.Next handleCommand = new HandleCommand<>(command, ctx, this);
		try {
			return middlewares
					.supplyEx()
					.foldRight(handleCommand, (step, next) -> () -> step.invoke(command, ctx, next))
					.invoke();
		} finally {
			commands.previous();
		}
	}

	private ChainContext completeChain() {
		throw new IllegalStateException("No command configured to handle end of chain gracefully.");
	}

	public static class HandleCommand<HANDLER extends Command.Handler> implements Command.Middleware.Next {

		private final HANDLER command;
		private final ChainContext ctx;
		private final Chain chain;

		public HandleCommand(HANDLER command, ChainContext ctx, Chain chain) {
			this.command = command;
			this.ctx = ctx;
			this.chain = chain;
		}

		public ChainContext ctx() {
			return ctx;
		}

		@Override
		public ChainContext invoke() throws Exception {
			return command.execute(ctx, chain);
		}

	}

}
