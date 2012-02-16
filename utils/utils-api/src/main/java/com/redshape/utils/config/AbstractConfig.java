package com.redshape.utils.config;

import com.redshape.utils.StringUtils;
import com.redshape.utils.config.sources.IConfigSource;

import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.utils.config
 * @date 10/20/11 1:13 PM
 */
public abstract class AbstractConfig implements IConfig {
	protected boolean nulled;
	protected String value;
	protected IConfig parent;
	protected String name;
	protected Map<String, String> attributes = new LinkedHashMap<String, String>();
	protected List<IConfig> childs = new ArrayList<IConfig>();
	protected IConfigSource source;

	protected AbstractConfig() {
		this(null, null, null);
	}

	protected AbstractConfig(IConfig parent, String name, String value) {
        this.parent = parent;
        this.name = name;
        this.value = value;
    }

    protected AbstractConfig(String name, String value) {
        this(null, name, value);
    }

    public AbstractConfig(IConfigSource source) throws ConfigException {
        this.source = source;
		this.init();
	}

	abstract protected void init() throws ConfigException;

	@Override
	public boolean isNull() {
		return this.nulled;
	}

	@Override
	public String path() throws ConfigException {
		Deque<String> deque = new LinkedBlockingDeque<String>();
		IConfig parent = this;
		while ( null != parent ) {
			deque.add( parent.name() );

			parent = parent.parent();
		}

		return StringUtils.join(deque, ".");
	}

	@Override
	public <T extends IConfig> List<T> childs() {
		return (List<T>) this.childs;
	}

	@Override
	public boolean hasChilds() {
		return !this.childs.isEmpty();
	}

	@Override
	public String[] list() {
		return this.list(null);
	}

	@Override
	public IConfig get(String name) throws ConfigException {
		if (this.isNull()) {
            return this.createNull();
        }

		IConfig result = this;
		String[] pathNodes = name.split("\\.");
		if ( pathNodes.length == 1 ) {
			return this._get(name);
		}

		for ( String pathNode : pathNodes ) {
			result = result.get(pathNode);
			if ( result.isNull() ) {
				break;
			}
		}

		return result;
	}

	private IConfig _get( String name ) {
		for ( IConfig config : this.childs() ) {
			if ( config.name().equals(name) ) {
				return config;
			}
		}

		return this.createNull();
	}

	@Override
	public String[] list(String name) {
		List<String> list = new ArrayList<String>();
		for ( IConfig node : this.childs() ) {
			if ( name == null || !node.name().equals(name) ) {
                continue;
            }

            list.add( node.value() );
		}

		return list.toArray( new String[ list.size() ] );
	}

	@Override
	public String name() {
		return this.name;
	}

	@Override
	public String[] names() {
		List<String> list = new ArrayList<String>();
		for ( IConfig node : this.childs() ) {
			list.add( node.name() );
		}

		return list.toArray( new String[ list.size() ] );
	}

	@Override
	public String attribute(String name) {
		return this.attributes.get(name);
	}

	@Override
	public String[] attributeNames() {
		return this.attributes.keySet().toArray( new String[this.attributes.size()] );
	}

	@Override
	public String value() {
		return this.value;
	}

	@Override
	public IConfig parent() throws ConfigException {
		return this.parent;
	}

	@Override
	public IConfig append(IConfig config) {
		config.parent(this);
		this.childs.add(config);
		return this;
	}

	@Override
	public IConfig parent(IConfig config) {
		this.parent = config;
		return this;
	}

	@Override
	public IConfig set(String value) throws ConfigException {
		this.value = value;
		return this;
	}

	@Override
	public IConfig attribute(String name, String value) {
		this.attributes.put(name, value);
		return this;
	}

	@Override
	public IConfig remove() throws ConfigException {
		this.parent.remove(this);
		this.nulled = true;
		return this;
	}

	@Override
	public IConfig remove(IConfig config) throws ConfigException {
		this.childs.remove(config);
		return this;
	}

	abstract protected IConfig createNull();
}
