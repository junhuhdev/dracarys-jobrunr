package io.github.junhuhdev.dracarys.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;
import org.jobrunr.jobs.details.instructions.VisitTypeInstruction;

import java.lang.reflect.Array;

import static org.jobrunr.jobs.details.JobDetailsGeneratorUtils.toFQClassName;
import static org.jobrunr.utils.reflection.ReflectionUtils.toClass;

public class ANewArrayOperandInstruction extends VisitTypeInstruction {

    public ANewArrayOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        Integer arraySize = (Integer) jobDetailsBuilder.getStack().pollLast();
        final Object[] result = (Object[]) Array.newInstance(toClass(toFQClassName(type)), arraySize);
        for (int i = 0; i < arraySize; i++) {
            jobDetailsBuilder.pollFirstInstruction().invokeInstruction(); // not interested in this
            final Object arrayItem = jobDetailsBuilder.pollFirstInstruction().invokeInstruction();
            result[i] = arrayItem;
        }
        return result;
    }
}
