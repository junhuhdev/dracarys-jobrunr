package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

public class IConst0OperandInstruction extends ZeroOperandInstruction {

    public IConst0OperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        return 0;
    }

}
