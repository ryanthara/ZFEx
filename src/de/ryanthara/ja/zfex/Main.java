/*
 * License: MIT. Copyright (c) 2017 by Sebastian Aust (https://www.ryanthara.de/)
 *
 * This file is part of the package de.ryanthara.ja.zfex
 *
 * Copyright (c) 2017 ryanthara
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.ryanthara.ja.zfex;

import de.ryanthara.ja.zfex.data.PreferenceHandler;
import de.ryanthara.ja.zfex.gui.StatusPane;
import javafx.application.Application;

import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;

/**
 * This implements values, constants and objects for the complete ZFEx application as an abstract class.
 * <p>
 * The main idea to do this was influenced by the code base of JOSM, which is the most popular
 * java written editor for OpenStreetMap data.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public abstract class Main extends Application {

    /**
     * The reference to the global FileHandler for logging into a single file.
     */
    public static FileHandler fileHandler;
    /**
     * Contains the URL of the ZFEx website.
     */
    public static final String ZFEx_WEBSITE = "https://code.ryanthara.de/ZFEx";
    /**
     * The reference for read success of the laser radar project.
     */
    public static boolean isLoadedLaserRadarProject = false;
    /**
     * The reference for write success of the stand points coordinate file.
     */
    public static boolean isWrittenStandPointsFile = false;
    /**
     * The reference for write success of the viewer file.
     */
    public static boolean isWrittenViewerFile = false;
    /**
     * The reference to the logging level for ZFEx.
     */
    public static Level loggingLevel;
    /**
     * Store command line interface parameters in a map as string key/value pair.
     */
    public static Map<String, String> cliParameters;
    /**
     * The reference to the global application preferences handler.
     */
    public static PreferenceHandler pref;
    /**
     * The reference to the global application status bar.
     */
    public static StatusPane statusPane;

} // end of Main
