package io.github.junhuhdev.dracarys.pipeline.chain;

import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandContext;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandStatus;
import io.github.junhuhdev.dracarys.pipeline.cmd.FaultCmd;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public class ChainContext {

	private final CommandContext commandContext;

	public ChainContext(Command command) {
		this.commandContext = new CommandContext(command);
	}

	public ChainContext(List<Command> commands, CommandStatus status) {
		this.commandContext = new CommandContext(commands, status);
	}

	public CommandContext getCmdCtx() {
		return commandContext;
	}

	public CommandStatus getStatus() {
		return commandContext.getStatus();
	}

	public String getId() {
		return this.commandContext.getId();
	}

	public List<Command> getAll() {
		return this.commandContext.getCommands();
	}

	public <CMD extends Command> CMD getFirst(Class<CMD> type) {
		return this.commandContext.getFirst(type);
	}

	public Command getLast() {
		return this.commandContext.getLast();
	}

	public boolean hasFault() {
		var cmd = this.commandContext.getLast();
		return cmd instanceof FaultCmd;
	}

	public <CMD extends Command> Optional<CMD> onFirst(Class<CMD> type) {
		return this.commandContext.onFirst(type);
	}

	/**
	 * Command can store command
	 * @param command
	 */
	public void store(Command command) {
		requireNonNull(command, "Command cannot be null");
		this.commandContext.store(command);
		var onNextState = command.nextState();
		onNextState.ifPresent(commandContext::triggerStateTransition);
	}

}
