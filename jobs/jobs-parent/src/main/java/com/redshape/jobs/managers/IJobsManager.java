package com.redshape.jobs.managers;

import com.redshape.jobs.IJob;
import com.redshape.jobs.handlers.HandlingException;
import com.redshape.jobs.handlers.IJobHandler;
import com.redshape.jobs.result.IJobResult;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

public interface IJobsManager extends ApplicationContextAware {

    public Map<Class<? extends IJob>, IJobHandler<?, ?>> getHandlers();

	public <T extends IJob, R extends IJobResult> Future<R> execute(T job)
				throws HandlingException;
	
	public void cancel(UUID jobId) throws HandlingException;
	
}
