package com.redshape.servlet.routes.route;

import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.routes.IRoute;
import com.redshape.utils.config.ConfigException;
import com.redshape.utils.config.IConfig;
import com.redshape.utils.range.IRange;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexp implements IRoute {
	private static final Logger log = Logger.getLogger( Regexp.class );

    @Autowired( required = true )
    private IConfig config;

	private Pattern pattern;
	private IRange<Integer> controllerGroup;
	private IRange<Integer> actionGroup;
	private List<Integer> allowedGroupsCount = new ArrayList<Integer>();

	public Regexp( String expression, IRange<Integer> controllerGroup,
									  IRange<Integer> actionGroup ) {
		if ( expression == null || expression.isEmpty() ) {
			throw new IllegalArgumentException("<null> expression");
		}
		
		if ( controllerGroup == null ) {
			throw new IllegalArgumentException("<null> controller group");
		}
		
		if ( actionGroup == null ) {
			throw new IllegalArgumentException("<null> action group");
		}

		if ( controllerGroup.isIntersects( actionGroup ) ) {
			throw new IllegalArgumentException("Controller groups indexes must " +
					"not intersects with action groups");
		}
		
		this.pattern = Pattern.compile(expression);
		this.controllerGroup = controllerGroup;
		this.actionGroup = actionGroup;
	}

	public List<Integer> getAllowedGroupsCount() {
		return allowedGroupsCount;
	}

	public void setAllowedGroupsCount(List<Integer> allowedGroupsCount) {
		this.allowedGroupsCount = allowedGroupsCount;
	}

	@Override
	public boolean isApplicable(IHttpRequest request) {
		Matcher matcher = this.pattern.matcher(
				request.getRequestURI().startsWith(request.getContextPath()) ?
						request.getRequestURI().substring(request.getContextPath().length())
						: request.getRequestURI());
		if ( !matcher.find() ) {
			return false;
		}

		return this.getAllowedGroupsCount().isEmpty()
				|| this.getAllowedGroupsCount().contains(matcher.groupCount() );
	}

	@Override
	public void apply(IHttpRequest request) {
        String contextPath = this.getContextPath(request);

		String sourcePath = request.getServletPath().startsWith( contextPath )?
						request.getRequestURI().substring( contextPath.length() )
						: request.getRequestURI();
        if ( sourcePath.contains(";") ) {
            sourcePath = sourcePath.substring( 0, sourcePath.indexOf(";") );
        }

		Matcher matcher = this.pattern.matcher( sourcePath );
		if ( matcher.find() ) {
			log.debug( "Groups founded:" + matcher.groupCount() );

			StringBuilder actionPath = new StringBuilder();
			StringBuilder controllerPath = new StringBuilder();
			for ( int i = 1; ; i++ ) {
				boolean lastNode =
							(i == matcher.groupCount() );

				int start = matcher.start(i);
				int end = matcher.end(i);
				if ( start == -1 ) {
					continue;
				}

				String nodeValue = sourcePath.substring(
								start, end );
				if ( this.actionGroup.isEmpty() ||
						( this.controllerGroup.inRange(i) &&
							/** controller nodes count must not be
							 *  higher than total (groups count - 1)**/
							( !lastNode ) ) ) {
					if ( i != 1 ) {
						controllerPath.append("/");
					}
					controllerPath.append( nodeValue );
				} else if ( this.actionGroup.inRange(i) || lastNode ) {
					actionPath.append( nodeValue );
					break;
				}

				if ( lastNode ) {
					break;
				}
			}

			request.setAction( actionPath.toString().replaceAll("/", "") );
			request.setController( this.normalize( this.removeContextPart( request, controllerPath.toString() ) ) );
		}
	}

    protected String getContextPath( IHttpRequest request ) {
        try {
            String contextPath = request.getContextPath();
            if ( !contextPath.isEmpty() ) {
                return contextPath;
            }

            IConfig contextPathNode = this.config.get("web.servletPath");
            if ( !contextPathNode.isNull() ) {
                contextPath = contextPathNode.value();
            }

            return contextPath;
        } catch ( ConfigException e ) {
            return "";
        }
    }

	protected String removeContextPart( IHttpRequest request, String sourcePath ) {
		while ( sourcePath.startsWith( request.getServletPath() ) ) {
			sourcePath = sourcePath.substring( request.getServletPath().length() );
		}

		return sourcePath;
	}

	protected String normalize( String value ) {
		while ( value.startsWith("/") ) {
			value = value.substring(1);
		}

		while ( value.endsWith("/") ) {
			value = value.substring( value.length(), value.length() - 1 );
		}

		while ( value.contains("//") ) {
			// Remove double slashes
			value = value.replace("//", "/");
		}

		return value;
	}
	
}
