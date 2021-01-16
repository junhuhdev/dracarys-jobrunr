package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.common.FirstGenericArgOf;
import org.springframework.beans.factory.ListableBeanFactory;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * One chain per event to process
 */
public abstract class ChainBase<R extends Command.Request, C extends Command> implements Chainable<R, C> {

	@Resource
	private ListableBeanFactory beanFactory;

	protected abstract List<Class<? extends Command>> getCommands();

	public boolean matches(R request) {
		Class handlerType = getClass();
		Class commandType = request.getClass();
		return new FirstGenericArgOf(handlerType).isAssignableFrom(commandType);
	}

	private ListIterator<Command.Handler> createCommands() {
		List<Command.Handler> commands = new ArrayList<>();
		addCommands(commands, this.getCommands());
		return commands.listIterator();
	}

	private void addCommands(List<Command.Handler> commands, List<Class<? extends Command>> listOfCmds) {
		for (var cmd : listOfCmds) {
			Command.Handler bean = (Command.Handler) beanFactory.getBean(cmd);
			commands.add(bean);
		}
	}

	@Override
	public ChainContext<C> dispatch(R event) throws Exception {
		ListIterator<Command.Handler> commands = this.createCommands();
		Chain chain = new Chain(commands);
		return chain.proceed(new ChainContext(event));
	}

}
