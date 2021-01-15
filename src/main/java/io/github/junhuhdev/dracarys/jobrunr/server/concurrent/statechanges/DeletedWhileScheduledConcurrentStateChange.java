package io.github.junhuhdev.dracarys.jobrunr.server.concurrent.statechanges;

import static org.jobrunr.jobs.states.StateName.DELETED;
import static org.jobrunr.jobs.states.StateName.SCHEDULED;

public class DeletedWhileScheduledConcurrentStateChange extends AbstractAllowedConcurrentStateChange {

    public DeletedWhileScheduledConcurrentStateChange() {
        super(SCHEDULED, DELETED);
    }

}