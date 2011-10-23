package com.redshape.utils.system;

/**
 * @author nikelin
 * @date 23:07
 */

import com.redshape.utils.system.console.IConsole;
import com.redshape.utils.system.scripts.IScriptExecutor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemFacade implements ISystemFacade {
	private static Logger log = Logger.getLogger( SystemFacade.class );

	private static final Pattern UUID_PATTERN = Pattern.compile("UUID: (.*?)\\n");

	private IConsole console;
    private boolean scanned;
    private boolean refreshDevices;

    public SystemFacade( IConsole console ){
    	this.console = console;
    }

    /* (non-Javadoc)
	 * @see com.api.deployer.system.ISystemFacade#getConsole()
	 */
    public IConsole getConsole() {
    	return this.console;
    }

    @Override
	public void init(){

    }

    @Override
    public UUID detectStationId() throws IOException {
    	IScriptExecutor executor = this.getConsole().createExecutor("dmidecode");
    	executor.addUnnamedParameter("--type")
    			.addUnnamedParameter(1);

    	String output = executor.execute();

    	Matcher matcher = UUID_PATTERN.matcher(output);
    	if ( !matcher.find() ) {
    		throw new IOException("Cannot detect UUID from dmidetect output");
    	}

    	return UUID.fromString(output.substring(matcher.start(1), matcher.end(1)));
    }

    @Override
    /**
     * @about if we running under the root privileges id -u will return "0" value
     */
    public boolean isUnderRoot() throws IOException {
    	String result = this.getConsole().createExecutor("id").addUnnamedParameter("-u").execute();
    	log.info("isUnderRoot result: " + result);
    	return result.startsWith("0");
    }

    @Override
	public void reboot() throws IOException {
    	this.reboot(0);
    }

    @Override
	public void shutdown() throws IOException {
    	this.shutdown(0);
    }

    @Override
    public Integer getStationArch() throws IOException {
        return Integer.valueOf( System.getProperty("sun.arch.data.mode") );
    }

	@Override
	public void reboot(Integer delay) throws IOException {
		this.getConsole().createExecutor("reboot").addUnnamedParameter(delay).execute();
	}

	@Override
	public void shutdown(Integer delay) throws IOException {
		this.getConsole().createExecutor("shutdown").addUnnamedParameter(delay).execute();
	}

}