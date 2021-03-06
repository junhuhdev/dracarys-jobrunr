package io.github.junhuhdev.dracarys.jobrunr.jobs.filters;

import io.github.junhuhdev.dracarys.jobrunr.jobs.Job;
import io.github.junhuhdev.dracarys.jobrunr.jobs.states.JobState;

/**
 * A filter that is triggered each time that the state of a Job is changed.
 * This filter will be called before that the job has been saved to a {@link io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider}.
 * Altering the job will change the lifecycle of the job - an example of this is the {@link RetryFilter} which updates jobs that are failed to scheduled again.
 */
public interface ElectStateFilter extends JobFilter {

    void onStateElection(Job job, JobState newState);

}
