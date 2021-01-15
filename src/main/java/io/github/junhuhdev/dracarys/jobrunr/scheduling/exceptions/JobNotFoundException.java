package io.github.junhuhdev.dracarys.jobrunr.scheduling.exceptions;


import io.github.junhuhdev.dracarys.jobrunr.jobs.JobDetails;
import io.github.junhuhdev.dracarys.jobrunr.jobs.JobParameter;

import java.util.stream.Stream;

public class JobNotFoundException extends RuntimeException {

    public JobNotFoundException(JobDetails jobDetails) {
        this(jobDetails.getClassName(), jobDetails.getMethodName(), jobDetails.getJobParameters().stream().map(JobParameter::getClassName).toArray(String[]::new));
    }

    public JobNotFoundException(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        this(clazz.getName(), methodName, Stream.of(parameterTypes).map(Class::getName).toArray(String[]::new));
    }

    public JobNotFoundException(String className, String methodName, String[] parameterTypes) {
        super(className + "." + methodName + "(" + String.join(",", parameterTypes) + ")");
    }

    public JobNotFoundException(String message) {
        super(message);
    }
}
