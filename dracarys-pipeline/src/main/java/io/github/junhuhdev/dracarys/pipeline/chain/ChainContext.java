package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;

import java.util.List;
import java.util.Optional;

public class ChainContext {

	private final CommandContext commandContext;

	public ChainContext(Command.Request command) {
		this.commandContext = new CommandContext(command);
	}

	public String getId() {
		return this.commandContext.getId();
	}

	public List<Command> getAll() {
		return this.commandContext.getRequests();
	}

	public <CMD extends Command> CMD getFirst(Class<CMD> type) {
		return this.commandContext.getFirst(type);
	}

	public Command getLast() {
		return this.commandContext.getLast();
	}

	public <CMD extends Command> Optional<CMD> onFirst(Class<CMD> type) {
		return this.commandContext.onFirst(type);
	}

	/**
	 * Command can store command
	 * @param command
	 */
	public void store(Command command) {
		this.commandContext.store(command);
	}

}
