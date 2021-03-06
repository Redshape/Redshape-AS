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

package com.redshape.utils.system.console;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * User: serge
 * Date: 7/27/12
 * Time: 6:00 PM
 */
public class ConsoleTest {

    public static final String USER_HOME_DIR = System.getProperty("user.home");
    public static final String SUBDIR_1_PATH = USER_HOME_DIR + File.separator + "subdir.1";
    public static final String SUBDIR_2_PATH = SUBDIR_1_PATH + File.separator + "subdir.2";

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Before
    public void setUp() {
        File subdir = new File(SUBDIR_2_PATH);
        if ( subdir.exists() )
            subdir.delete();

        subdir = new File(SUBDIR_1_PATH);
        if ( subdir.exists() )
            subdir.delete();
    }

    @Test
    public void testCheckExists() throws IOException {
        IConsole console = new Console();
        assertTrue(console.checkExists(USER_HOME_DIR));
        assertFalse(console.checkExists(SUBDIR_1_PATH));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testMkdir() throws IOException {
        IConsole console = new Console();

        assertFalse(new File(SUBDIR_2_PATH).exists());

        console.mkdir(SUBDIR_2_PATH);

        assertTrue(new File(SUBDIR_2_PATH).exists());

        new File(SUBDIR_2_PATH).delete();
        new File(SUBDIR_1_PATH).delete();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    public void testDeleteFile() throws IOException {
        new File(SUBDIR_1_PATH).mkdir();
        new File(SUBDIR_2_PATH).mkdir();

        IConsole console = new Console();

        assertTrue( new File(SUBDIR_2_PATH).exists() );

        console.deleteFile(SUBDIR_1_PATH);

        assertFalse( new File(SUBDIR_1_PATH).exists() );
    }
}
