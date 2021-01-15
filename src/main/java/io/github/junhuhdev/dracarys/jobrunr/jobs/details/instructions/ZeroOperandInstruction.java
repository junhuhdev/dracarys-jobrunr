package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;
import org.jobrunr.jobs.details.instructions.AbstractJVMInstruction;

public abstract class ZeroOperandInstruction extends AbstractJVMInstruction {

    public ZeroOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load() {
        jobDetailsBuilder.pushInstructionOnStack(this);
    }
}
