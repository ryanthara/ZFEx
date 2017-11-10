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
package de.ryanthara.ja.zfex.tools;

import javafx.scene.control.TextField;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Checker has basic functions for different kind of simple tests and checks.
 * <p>
 * With this class and its static methods it is possible to check the content of text fields,
 * the existence of {@link java.io.File} and little things more.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class Checker {

    /**
     * Checks a {@link TextField} for not contains a null string and not being empty.
     *
     * @param textField the text field to be checked
     *
     * @return true for not empty text fields
     */
    public static boolean checkTextField(TextField textField) {
        return textField != null && !textField.getText().trim().isEmpty();
    }

    /**
     * Checks a String for being a valid directory.
     *
     * @param file the file to be checked
     *
     * @return true if is valid directory
     */
    public static boolean isDirectory(String file) {
        Path path = Paths.get(file);

        return Files.exists(path) && Files.isDirectory(path);
    }

    /**
     * Checks a String for being a valid file.
     *
     * @param file the file to be checked
     *
     * @return true if is valid file
     */
    public static boolean isFile(String file) {
        Path path = Paths.get(file);

        return Files.exists(path) && Files.isRegularFile(path);
    }


} // end of Checker
