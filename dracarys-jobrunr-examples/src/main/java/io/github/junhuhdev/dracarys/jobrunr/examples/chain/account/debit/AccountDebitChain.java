package io.github.junhuhdev.dracarys.jobrunr.examples.chain.account.debit;

import io.github.junhuhdev.dracarys.jobrunr.examples.component.account.Amount;
import io.github.junhuhdev.dracarys.pipeline.chain.ChainBase;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import io.github.junhuhdev.dracarys.pipeline.cmd.CommandRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountDebitChain extends ChainBase<AccountDebitChain.AccountDebitRequest> {

	@Override
	protected List<Class<? extends Command>> getCommands() {
		return List.of(
				AccountDebitValidationCmd.class,
				AccountDebitCmd.class);
	}

	@EqualsAndHashCode(callSuper = true)
	@AllArgsConstructor
	@Data
	public static class AccountDebitRequest extends CommandRequest {

		private String email;
		private Amount amount;

	}

}
