package com.redshape.daemon.jobs.handlers;

import com.redshape.daemon.jobs.IJob;
import com.redshape.daemon.jobs.result.IJobResult;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.UUID;

public abstract class AbstractJobHandler<T extends IJob, V extends IJobResult>
                                                implements IJobHandler<T, V>,
                                                           ApplicationContextAware {
    private ApplicationContext context;

    protected ApplicationContext getContext() {
        return this.context;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    abstract protected V createJobResult( UUID jobId );
	
}
