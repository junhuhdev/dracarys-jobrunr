package io.github.junhuhdev.dracarys.pipeline.cmd;

import io.github.junhuhdev.dracarys.pipeline.chain.Chain;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainContext;

/**
 * Default interface for all commands to implement.
 */
public interface Command {

    interface Request extends Command {
    }

    interface Handler extends Command {

        ChainContext execute(ChainContext ctx, Chain chain) throws Exception;
    }

    @FunctionalInterface
    interface Middleware {
        <R, C extends Command.Handler> ChainContext invoke(C command, Next<R> next) throws Exception;
//        ChainContext invoke(Handler command, Handler next);

        interface Next<T> {
            ChainContext invoke() throws Exception;
        }
    }
}
