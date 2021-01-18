package io.github.junhuhdev.dracarys.jobrunr.examples.infra.config;

import com.google.gson.GsonBuilder;
import io.github.junhuhdev.dracarys.jobrunr.configuration.JobRunr;
import io.github.junhuhdev.dracarys.jobrunr.configuration.JobRunrConfiguration;
import io.github.junhuhdev.dracarys.jobrunr.dashboard.JobRunrDashboardWebServer;
import io.github.junhuhdev.dracarys.jobrunr.jobs.mappers.JobMapper;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.BackgroundJob;
import io.github.junhuhdev.dracarys.jobrunr.scheduling.JobScheduler;
import io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServer;
import io.github.junhuhdev.dracarys.jobrunr.server.JobActivator;
import io.github.junhuhdev.dracarys.jobrunr.storage.StorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.storage.sql.common.DefaultSqlStorageProvider;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.JsonMapper;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.gson.GsonJsonMapper;
import io.github.junhuhdev.dracarys.jobrunr.utils.mapper.gson.RuntimeClassNameTypeAdapterFactory;
import io.github.junhuhdev.dracarys.pipeline.cmd.Command;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import static io.github.junhuhdev.dracarys.jobrunr.server.BackgroundJobServerConfiguration.usingStandardBackgroundJobServerConfiguration;

@Configuration
@Slf4j
public class DracarysJobRunrConfiguration {

	@Bean
	public JobActivator jobActivator(ApplicationContext applicationContext) {
		return applicationContext::getBean;
	}

	@Bean
	public StorageProvider storageProvider(DataSource dataSource, JobMapper jobMapper) {
		var storageProvider = new DefaultSqlStorageProvider(dataSource, DefaultSqlStorageProvider.DatabaseOptions.SKIP_CREATE);
		storageProvider.setJobMapper(jobMapper);
		return storageProvider;
	}

	@Bean
	public JobMapper jobMapper(JsonMapper jsonMapper) {
		return new JobMapper(jsonMapper);
	}

	@Bean
	public JsonMapper jsonMapper() {
		GsonBuilder gsonBuilder = new GsonBuilder()
				.registerTypeAdapterFactory(RuntimeClassNameTypeAdapterFactory.of(Command.class));
		return new GsonJsonMapper(gsonBuilder);
	}

	@Bean
	public BackgroundJobServer backgroundJobServer(StorageProvider storageProvider, JobActivator jobActivator) {
		var config = usingStandardBackgroundJobServerConfiguration().andWorkerCount(5);
		final BackgroundJobServer backgroundJobServer = new BackgroundJobServer(storageProvider, jobActivator, config);
		backgroundJobServer.start();
		return backgroundJobServer;
	}

	@Bean
	public JobRunrDashboardWebServer dashboardWebServer(StorageProvider storageProvider, JsonMapper jsonMapper, Environment env) {
		int port = Integer.parseInt(env.getProperty("server.port")) + 1;
		log.info("Running dashboard on port {}", port);
		final JobRunrDashboardWebServer jobRunrDashboardWebServer = new JobRunrDashboardWebServer(storageProvider, jsonMapper, port);
		jobRunrDashboardWebServer.start();
		return jobRunrDashboardWebServer;
	}

	@Bean
	public JobScheduler initJobRunr(StorageProvider storageProvider, JobActivator jobActivator) {
		JobScheduler jobScheduler = new JobScheduler(storageProvider);
		BackgroundJob.setJobScheduler(jobScheduler);
		Runtime.getRuntime().addShutdownHook(new Thread(JobRunr::destroy, "extShutdownHook"));
		return jobScheduler;
	}

	@PreDestroy
	public void stop() {
		log.info("Shutting down Dracarys JobRunr...");
		JobRunr.destroy();
	}

}
