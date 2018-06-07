/*
 * License: MIT. Copyright (c) 2017 by Sebastian Aust (https://www.ryanthara.de/)
 *
 * This file is part of the package de.ryanthara.ja.zfex.gui
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
package de.ryanthara.ja.zfex.io;

import de.ryanthara.ja.zfex.Main;
import de.ryanthara.ja.zfex.tools.Checker;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Writes a string array line by line into a text file.
 * <p>
 * This is used for writing the coordinate file.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
class LineWriter {

    private final static Logger logger = Logger.getLogger(LineWriter.class.getName());

    private final String fileName;

    /**
     * Constructs a new instance of this class with the filename of the file to be written.
     *
     * @param fileName file name of the file to be written
     */
    LineWriter(String fileName) {
        this.fileName = fileName;

        logger.setLevel(Main.loggingLevel);
        logger.addHandler(Main.fileHandler);
    }

    /**
     * Writes a {@link StringBuilder} object that contains lines of strings into a file.
     *
     * @param builder Lines of Strings to be written
     *
     * @return success of file writing
     */
    boolean writeBuilder(final StringBuilder builder) {
        boolean success = false;

        final Path path = Paths.get(fileName);

        if (Checker.isDirectory(path.toString())) {
            try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
                writer.write(builder.toString());
                success = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "can not write string builder to file: " + fileName, e);
                logger.log(Level.SEVERE, e.getMessage());
            }
        }

        return success;
    }

    /**
     * Writes an {@link java.util.ArrayList} of string lines into a file.
     *
     * @param lines Lines of Strings to be written
     *
     * @return success of file writing
     */
    boolean writeList(final List<String> lines) {
        boolean success = false;

        final Path path = Paths.get(fileName);

        try {
            Files.write(path, lines);
            success = true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "can not write list to file: " + fileName, e);
            logger.log(Level.SEVERE, e.getMessage());
        }

        return success;
    }

} // end of LineWriter
