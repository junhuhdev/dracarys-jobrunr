package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.utils.streams.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Stream;

public class JobPerformingFilters extends AbstractJobFilters {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobPerformingFilters.class);

    private Job job;

    public JobPerformingFilters(Job job, JobDefaultFilters jobFilters) {
        super(job, jobFilters);
        this.job = job;
    }

    public void runOnStateElectionFilter() {
        electStateFilters().forEach(catchThrowable(electStateFilter -> electStateFilter.onStateElection(job, job.getJobState())));
    }

    public void runOnStateAppliedFilters() {
        applyStateFilters().forEach(catchThrowable(applyStateFilter -> applyStateFilter.onStateApplied(job, job.getJobState(-2), job.getJobState(-1))));
    }

    public void runOnJobProcessingFilters() {
        jobServerFilters().forEach(catchThrowable(jobServerFilter -> jobServerFilter.onProcessing(job)));
    }

    public void runOnJobProcessedFilters() {
        jobServerFilters().forEach(catchThrowable(jobServerFilter -> jobServerFilter.onProcessed(job)));
    }

    private Stream<ElectStateFilter> electStateFilters() {
        return electStateFilters(jobFilters);
    }

    private Stream<ApplyStateFilter> applyStateFilters() {
        return applyStateFilters(jobFilters);
    }

    private Stream<JobServerFilter> jobServerFilters() {
        return jobServerFilters(jobFilters);
    }

    private static Stream<ElectStateFilter> electStateFilters(List<JobFilter> jobFilters) {
        return StreamUtils.ofType(jobFilters, ElectStateFilter.class);
    }

    private static Stream<ApplyStateFilter> applyStateFilters(List<JobFilter> jobFilters) {
        return StreamUtils.ofType(jobFilters, ApplyStateFilter.class);
    }

    private static Stream<JobServerFilter> jobServerFilters(List<JobFilter> jobFilters) {
        return StreamUtils.ofType(jobFilters, JobServerFilter.class);
    }

    @Override
    Logger getLogger() {
        return LOGGER;
    }
}
