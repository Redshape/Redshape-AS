package com.redshape.daemon.jobs.handlers;

import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import org.springframework.context.ApplicationContext;

public abstract class AbstractAwareJobHandler<T extends IJob, V extends IJobResult>
							extends AbstractJobHandler<T, V> {
	private ApplicationContext context;
	
	public AbstractAwareJobHandler( ApplicationContext context ) {
		this.context = context;
	}
	
	public void setContext( ApplicationContext context ) {
		this.context = context;
	}
	
	public ApplicationContext getContext() {
		return this.context;
	}
	
}
