package com.redshape.servlet.routes.route;

import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.routes.IRoute;
import com.redshape.utils.range.IRange;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regexp implements IRoute {
	private static final Logger log = Logger.getLogger( Regexp.class );

	private Pattern pattern;
	private IRange<Integer> controllerGroup;
	private IRange<Integer> actionGroup;
	
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

	@Override
	public boolean isApplicatable(IHttpRequest request) {
		log.info("URI requested:" + request.getRequestURI() ); 
		log.info("URI pattern:" + this.pattern.pattern() );
		return this.pattern.matcher( request.getRequestURI() ).find();
	}

	@Override
	public void applicate(IHttpRequest request) {
		String sourcePath = request.getRequestURI();
		Matcher matcher = this.pattern.matcher( sourcePath );
		
		if ( matcher.find() ) {
			log.info( "Groups founded:" + matcher.groupCount() );

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
								matcher.start(i), matcher.end(i) );
				if ( this.controllerGroup.inRange(i) &&
							/** controller nodes count must not be
							 *  higher than total (groups count - 1)**/
							!lastNode ) {
					if ( i != 1 ) {
						controllerPath.append("/");
					}
					controllerPath.append( nodeValue );
				} else if ( this.actionGroup.inRange(i) || lastNode ) {
					actionPath.append( nodeValue );
					break;
				} else {
					break;
				}
			}

			request.setAction( actionPath.toString().replaceAll("/", "") );
			request.setController( this.normalize( controllerPath.toString() ) );
		} else {
			throw new IllegalArgumentException();
		}
	}

	protected String normalize( String value ) {
		if ( value.startsWith("/") ) {
			value = value.substring(1);
		}

		if ( value.endsWith("/") ) {
			value = value.substring( value.length(), value.length() - 1 );
		}

		return value;
	}
	
}
