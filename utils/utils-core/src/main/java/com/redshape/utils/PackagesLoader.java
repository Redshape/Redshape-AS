package com.redshape.utils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * @author nikelin
 *
 * x
 */
public class PackagesLoader implements IPackagesLoader {
	private static final Logger log = Logger.getLogger( PackagesLoader.class );

	@Autowired( required = true )
	private ResourcesLoader resourcesLoader;

	private List<String> classpath;

	public PackagesLoader() {
		this( new ArrayList<String>() );
	}

	public PackagesLoader(List<String> classpath) {
		this.classpath = classpath;

		this.init();
	}

	protected void init() {
		String systemClasspath = System.getProperty("java.class.path");

		this.classpath.addAll(
			Arrays.asList(
				systemClasspath.split(Pattern.quote(File.pathSeparator) )
			)
		);

		System.setProperty("java.class.path", SimpleStringUtils.join(this.classpath, File.separator ));
	}

	public List<String> getClasspath() {
		return classpath;
	}

	public void setClasspath(List<String> classpath) {
		this.classpath = classpath;
	}

	public void setResourcesLoader( ResourcesLoader loader ) {
		this.resourcesLoader = loader;
	}

	public ResourcesLoader getResourcesLoader() {
		return this.resourcesLoader;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T>[] getClasses(String pkgName) throws PackageLoaderException {
		return this.getClasses(pkgName, new InterfacesFilter<T>(new Class[]{ }));
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> Class<T>[] getClasses(String pkgName, IFilter<Class<T>> filter) throws PackageLoaderException {
		Set<Class<T>> classes = new HashSet<Class<T>>();
		for ( String path : this.getClasspath() ) {
			try {
				Class<T>[] collectionPart = this.getClasses( path, pkgName, filter );
				if ( collectionPart != null ) {
					classes.addAll( Arrays.asList( collectionPart ) );
				}
			} catch ( PackageLoaderException e ) {
				log.error(e.getMessage(), e);
				continue;
			} catch ( IOException e ) {
				log.error(e.getMessage(), e);
				continue;
			}
		}

		log.info( classes.size() + " classes loaded!" );

		return classes.toArray(new Class[classes.size()]);
	}

	protected <T> Class<T>[] getClasses( String path, String pkgName, IFilter<Class<T>> filter )
			throws PackageLoaderException, IOException {
		if ( path.endsWith(".jar") ) {
			return this.getClassesFromJar( path, pkgName, filter );
		} else {
			return this.getClassesFromIdle( path, pkgName, filter );
		}
	}

	private ClassLoader createClassLoader( URL path ) {
		return this.createClassLoader( Arrays.asList( new URL[] { path } ) );
	}

	private ClassLoader createClassLoader( List<URL> targetEntries ) {
		return new URLClassLoader( targetEntries.toArray( new URL[targetEntries.size()]), Thread.currentThread().getContextClassLoader() );
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<T>[] getClassesFromJar( String path,
												String pkgName,
												IFilter<Class<T>> filter )
			throws IOException, PackageLoaderException {
		try {
			List<URL> targetEntries = this.filterJarEntries(
				new JarFile( path ),
				this.convertToFolderName( pkgName ),
				path
			);

			List<Class<T>> result = new ArrayList<Class<T>>();
			ClassLoader loader = this.createClassLoader( targetEntries );
			for ( URL classUrl : targetEntries ) {
				String className = this.prepareClassName( classUrl );
				try {
					Class<T> clazz = (Class<T>) loader.loadClass(className);
					if ( filter == null || filter.filter(clazz) ) {
						result.add(clazz);
					}
				} catch ( ClassNotFoundException e ) {
					log.debug( String.format("Unable to loader class %s", className), e );
					continue;
				}
			}

			return result.toArray( new Class[result.size()] );
		} catch ( IOException e ) {
			throw e;
		}
	}

	protected String prepareClassName( URL url ) {
		return this.prepareClassName( url.getPath() );
	}

	protected String prepareClassName( String value ) {
		return 	value.substring(0, value.lastIndexOf("."))
					.replaceAll(".class", "")
					.replaceAll("\\\\", ".")
                    .replaceAll("\\/", ".");
	}

	private List<URL> filterJarEntries( JarFile file, String contextName, String itemName )
		throws MalformedURLException {
		Enumeration<JarEntry> entries = file.entries();
		List<URL> targetEntries = new ArrayList<URL>() ;
		while( entries.hasMoreElements() ) {
			JarEntry testing = entries.nextElement();

			if ( testing.getName().contains(contextName) &&
					!testing.isDirectory()
					&& testing.getName().endsWith(".class") ) {
				targetEntries.add( new URL("jar", itemName + "!/", testing.getName() ) );
			}
		}

		return targetEntries;
	}

	protected <T> Class<T>[] getClassesFromIdle( String basePath,
												 String pkgName,
												 IFilter<Class<T>> filter )
			throws PackageLoaderException {
		return this.getClassesFromIdle( basePath, basePath, pkgName, filter );
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<T>[] getClassesFromIdle( String basePath, String path, String pkgName, IFilter<Class<T>> filter ) throws PackageLoaderException {
		try {
			List<Class<T>> classes = new ArrayList<Class<T>>();
			String folderName = this.convertToFolderName( pkgName);

			File folder;
			try {
				folder = resourcesLoader.loadFile( path + File.separator + folderName );
				log.info("Trying to search in " + path + "/" + folderName );
				if ( folder == null || !folder.exists() || !folder.canRead() || ( !folder.isDirectory() && !folder.getPath().endsWith(".jar") ) ) {
					return null;
				}
			} catch ( FileNotFoundException e ) {
				folder = null;
			}

			if ( folder != null ) {
				if ( folder.getPath().endsWith(".jar") ) {
					classes.addAll( Arrays.<Class<T>>asList( this.getClassesFromJar( path, pkgName, filter ) ) );
				} else {
					classes.addAll( this.<T>getClassesFromIdleFolder( basePath, path + File.separator + pkgName.replace(".", "/"), pkgName, filter ) );
				}
			} else {
				File pathFile = this.getResourcesLoader().loadFile(path);
				for ( File file : pathFile.listFiles() ) {
					try {
						if ( file.getPath().endsWith(".jar") ) {
							log.info("Trying to load JAR: " + file.getPath() );
							classes.addAll( Arrays.<Class<T>>asList( this.getClassesFromJar( file.getAbsolutePath(), pkgName, filter ) ) );
						} else if ( file.isDirectory() && !file.getName().startsWith(".") ) {
							classes.addAll( this.<T>getClassesFromIdleFolder( file.getPath(), file.getPath() + File.separator + pkgName.replace(".", "/"), pkgName, filter ) );
						}
					} catch ( Throwable e ) {
						continue;
					}
				}
			}

			return classes.toArray( new Class[ classes.size() ] );
		} catch ( PackageLoaderException e ) {
			throw e;
		} catch ( Throwable e ) {
			throw new PackageLoaderException( e.getMessage(), e );
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> Collection<Class<T>> getClassesFromIdleFolder( String basePath, String folder, String pkgName, IFilter<Class<T>> filter )
			throws ClassNotFoundException, MalformedURLException {
		Collection<Class<T>> classes = new HashSet<Class<T>>();
		File folderFile = new File( folder );

		File[] clsEntries = folderFile.listFiles();
		for ( File clsFile : clsEntries ) {
			if ( clsFile.isDirectory() ) {
				classes.addAll(
					this.<T>getClassesFromIdleFolder(
						basePath,
						clsFile.getAbsolutePath(),
						pkgName + "." + clsFile.getName(),
						filter
					)
				);
			}

			String clazzName = this.prepareClassName(pkgName + File.separator + clsFile.getName());
			try {
				Class<T> clazz = (Class<T>) Class.forName(clazzName);
				if ( filter != null && !filter.filter(clazz) ) {
					continue;
				}

				classes.add( clazz );
			} catch ( Throwable e ) {
				log.debug( String.format( "Unable to load class %s", clazzName ));
				continue;
			}
		}

		return classes;
	}

	private String convertToFolderName( String packageName ) {
		return StringUtils.preparePathByPackage(packageName);
	}

}


