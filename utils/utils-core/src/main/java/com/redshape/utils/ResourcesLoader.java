/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.redshape.utils;

import org.apache.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jan 22, 2010
 * Time: 10:23:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class ResourcesLoader implements IResourcesLoader {
    private final static Logger log = Logger.getLogger( ResourcesLoader.class );
    private String rootDirectory;
    private Collection<String> searchPath = new HashSet<String>();
    final static Pattern NON_PRINTABLE = Pattern
            .compile("[^\t\n\r\u0020-\u007E\u0085\u00A0-\uD7FF\uE000-\uFFFC]");

    public ResourcesLoader() {
        this(null);
    }

    public ResourcesLoader( String rootDirectory ) {
        this( rootDirectory, new HashSet<String>() );
    }

    public ResourcesLoader( String rootDirectory, Collection<String> searchPath ) {
        if ( rootDirectory != null ) {
            this.rootDirectory = StringUtils.escapePath(rootDirectory);
        }

        this.setSearchPath( System.getProperty("java.class.path").split(File.pathSeparator) );
        this.setSearchPath( searchPath );
    }

    public void setSearchPath( String[] paths ) {
        for ( String path : paths ) {
            this.addSearchPath(path);
        }
    }

    public void setSearchPath( Collection<String> paths ) {
        this.setSearchPath( paths.toArray(new String[paths.size()]) );
    }

    public Collection<String> getSearchPath() {
        return this.searchPath;
    }

    public File loadFile( String path ) throws IOException {
        return this.loadFile( path, true );
    }

    public void setRootDirectory( String rootDirectory ) {
        this.rootDirectory = rootDirectory;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
    }

    @Override
    public File loadFile(URI uri) throws IOException {
        return new File(uri);
    }

    public File loadFile( String path, boolean searchPath ) throws IOException {
        File file = new File( StringUtils.escapePath(path) );
        if ( file.exists() ) {
            return file;
        }

        file = new File( (rootDirectory == null ? "." : this.rootDirectory) + File.separator + StringUtils.escapePath(path) );
        if ( searchPath && !file.exists() ) {
            file = this.find(path);
        }

        return file;
    }

    public String loadData( File file ) throws IOException {
        return this.loadData(file, false);
    }

    public String loadData( String path ) throws IOException {
        return this.loadData( path, false );
    }

    public String loadData( String path, boolean escapeNonpritable ) throws IOException {
        return this.loadData( this.loadFile(path), escapeNonpritable );
    }

    public String loadData( File file, boolean escapeNonprintable ) throws IOException {
        FileInputStream stream = new FileInputStream(file);
        BufferedReader reader = new BufferedReader( new InputStreamReader(stream) );

        String result = new String();

        String line;
        while( null != ( line = reader.readLine() ) ) {
            result = result.concat(line) + "\n";
        }

        if ( escapeNonprintable ) {
            Matcher matcher = NON_PRINTABLE.matcher(result);
            if ( matcher.find() ) {
                result = matcher.replaceAll("\0");
            }
        }

        return result;
    }

    public InputStream loadResource( String path ) throws IOException {
        try {
            InputStream stream = this.getClass().getResourceAsStream( StringUtils.escapePath(path) );
            if ( stream == null ) {
                stream = new FileInputStream(this.loadFile(path));
            }

            return stream;
        } catch ( IOException e ) {
            return this.getClass().getClassLoader().getResourceAsStream( StringUtils.escapePath(path) );
        }
    }

    /**
     * @TODO загрузка из JAR-classpath элемента
     * @param path
     * @return
     * @throws FileNotFoundException
     */
    protected File find( String path ) throws FileNotFoundException {
        File candidateFile = null;

        path = StringUtils.escapePath(path);

        for( String pathPart : this.getSearchPath() ) {
            candidateFile = new File( pathPart + File.separator + path );
            if ( !candidateFile.exists() || !candidateFile.canRead() ) {
                candidateFile = null;
            } else {
                log.info("Found: " + path + "; on " + pathPart );
                break;
            }
        }

        return candidateFile;
    }

    public String[] getList( String path ) throws IOException {
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> entries = jarFile.entries();
        List<String> targetEntries = new ArrayList<String>() ;
        while( entries.hasMoreElements() ) {
            JarEntry testing = entries.nextElement();

            if ( testing.getName().endsWith(".class") ) {
                targetEntries.add( testing.getName() );
            }
        }

        return targetEntries.toArray( new String[targetEntries.size()] );
    }

    @Override
    public void addSearchPath(String searchPath) {
        this.searchPath.add( StringUtils.escapePath(searchPath) );
    }
}
