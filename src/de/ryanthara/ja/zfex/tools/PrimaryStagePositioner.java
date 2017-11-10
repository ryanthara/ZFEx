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

import de.ryanthara.ja.zfex.Main;
import de.ryanthara.ja.zfex.data.PreferenceHandler;
import javafx.stage.Stage;

/**
 * {@link PrimaryStagePositioner} implements a simple functionality to center ZFEx on the screen.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class PrimaryStagePositioner {

    /**
     * Calculates the Stage position on the screen or load it from the settings file if present.
     *
     * @param primaryStage parent Stage
     */
    public static void positStage(Stage primaryStage) {
        String s = Main.pref.getUserPref(PreferenceHandler.LAST_POS_PRIMARY_MONITOR);

        double x = 0d, y = 0d;

        if (s.isEmpty()) {
            primaryStage.centerOnScreen();
        } else {
            String[] coords = s.split(",");

            x = Double.parseDouble(coords[0]);
            y = Double.parseDouble(coords[1]);
        }

        primaryStage.setX(x);
        primaryStage.setY(y);
    }

} // end of PrimaryStagePositioner
