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
package de.ryanthara.ja.zfex.gui;

import com.apple.eawt.Application;
import de.ryanthara.ja.zfex.Main;
import de.ryanthara.ja.zfex.data.PreferenceHandler;
import de.ryanthara.ja.zfex.i18n.LangStrings;
import de.ryanthara.ja.zfex.i18n.ResourceBundleUtils;
import de.ryanthara.ja.zfex.tools.PrimaryStagePositioner;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <tt>MainApplication</tt> is the main application of <tt>ZFEx</tt>.
 * <p>
 * It initialize the main window of <tt>ZFEx</tt> and setup the background functionality. This is done
 * by an extension of the {@link Main} class.
 *
 * @author sebastian
 * @version 1
 * @see Main
 * @since 1
 */
public class MainApplication extends Main {

    private final static Logger logger = Logger.getLogger(MainApplication.class.getName());

    private Scene scene;

    private static void initPreferences() {
        Main.pref = new PreferenceHandler();
    }

    /**
     * Main application startup.
     *
     * @param args command line arguments
     */
    public static void main(String... args) {
        launch(args);
    }

    private static void saveFramePosition(Scene scene) {
        // Runs only on one monitor systems
        // Detect the active monitor on which ZFEx is visible
        Main.pref.setUserPref(PreferenceHandler.LAST_POS_PRIMARY_MONITOR,
                Double.toString(scene.getWindow().getX()).concat(",").concat(Double.toString(scene.getWindow().getY())));
    }

    @Override
    public void init() {
        initLogging();

        // Load parameters from the command line interface
        Parameters parameters = getParameters();
        Main.cliParameters = parameters.getNamed();

        initPreferences();
    }

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set. The primary stage will be embedded in
     *                     the browser if the application was launched as an applet.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages and will not be embedded in the browser.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        final BorderPane borderPane = new BorderPane();

        borderPane.setTop(createInformationPane());
        borderPane.setCenter(createCenterPane(primaryStage));
        borderPane.setBottom(createStatusPane());

        scene = new Scene(borderPane, 800, 465);

        // TODO: 25.05.17 Replace this 'hack' when JDK 9 is available
        setApplicationIcon(primaryStage);

        primaryStage.setTitle(ResourceBundleUtils.getLangString(LangStrings.title));
        primaryStage.setScene(scene);

        // Position the primary stage (centered or last position)
        PrimaryStagePositioner.positStage(primaryStage);

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        saveFramePosition(scene);
    }

    private Pane createCenterPane(Stage primaryStage) {
        final GridPane gridPane = new GridPane();

        gridPane.add(new FunctionPane(primaryStage), 0, 0);

        return gridPane;
    }

    private Pane createInformationPane() {
        return new InformationPane(getHostServices());
    }

    private Pane createStatusPane() {
        final GridPane gridPane = new GridPane();

        Main.statusPane = new StatusPane();

        gridPane.add(Main.statusPane, 0, 0);

        return gridPane;
    }

    private void initLogging() {
        Main.loggingLevel = Level.FINEST;

        logger.setLevel(Main.loggingLevel);

        try {
            FileHandler fh = new FileHandler("ZFEx_logfile%g.xml", 1024 * 1024, 10, true);
            Main.fileHandler = fh;

            logger.addHandler(fh);
            logger.log(Level.INFO, "logging with level '" + Main.loggingLevel.getName() + "' for ZFEx enabled successful");
        } catch (IOException e) {
            System.err.println("Can not access file 'ZFEx_logfile[n].xml' " + e.getMessage());
        }
    }

    /*
     * Replace this 'hack' when JDK 9 is available with a solution for showing application icons for different os.
     */
    private void setApplicationIcon(Stage primaryStage) {
        // Bad part of code till some of the next javafx development cycles passed to get rid of this code
        if (System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
            Application application = Application.getApplication();
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(Images.iconZFEx.getImagePath()));

            application.setDockIconImage(image);
        } else {
            javafx.scene.image.Image image = new javafx.scene.image.Image(getClass().getResourceAsStream(Images.iconZFEx.getImagePath()));
            primaryStage.getIcons().add(image);
        }

        logger.log(Level.WARNING, "WARNING");
        logger.log(Level.CONFIG, "CONFIG");
        logger.log(Level.FINER, "FINER");
    }

} // end of MainApplication
