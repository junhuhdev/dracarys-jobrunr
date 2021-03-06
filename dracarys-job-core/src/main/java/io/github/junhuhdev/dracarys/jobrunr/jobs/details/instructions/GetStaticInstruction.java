package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsFinderContext;

import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.getObjectViaStaticField;
import static io.github.junhuhdev.dracarys.jobrunr.jobs.details.JobDetailsGeneratorUtils.toFQClassName;

public class GetStaticInstruction extends VisitFieldInstruction {

    public GetStaticInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        // TODO: how to know if we should invoke it or create JobDetails with static field?
        String className = toFQClassName(owner);
        String methodName = name;

        if (className.equals(System.class.getName())) {
            jobDetailsBuilder.setClassName(className);
            jobDetailsBuilder.setStaticFieldName(name);
        }
        return getObjectViaStaticField(className, methodName);
    }
}
