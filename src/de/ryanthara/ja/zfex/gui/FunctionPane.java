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

import de.ryanthara.ja.zfex.Main;
import de.ryanthara.ja.zfex.i18n.LangStrings;
import de.ryanthara.ja.zfex.i18n.ResourceBundleUtils;
import de.ryanthara.ja.zfex.io.ExportFileWorker;
import de.ryanthara.ja.zfex.io.ProjectFileWorker;
import de.ryanthara.ja.zfex.io.ViewerFileWorker;
import de.ryanthara.ja.zfex.tools.Checker;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <tt>FunctionPane</tt> is a special {@link GridPane} which contains the different functionality for ZFEx.
 * <p>
 * Therefore three rows of {@link Label}, {@link TextField} and {@link Button} are used.
 * All functions are implemented here as well.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
class FunctionPane extends GridPane {

    private final static Logger logger = Logger.getLogger(FunctionPane.class.getName());

    private Button runBtn;
    private TextField exportTextField;
    private TextField projectTextField;
    private TextField viewerTextField;

    private Stage primaryStage;

    FunctionPane(Stage primaryStage) {
        this.primaryStage = primaryStage;

        logger.setLevel(Main.loggingLevel);
        logger.addHandler(Main.fileHandler);

        createPane();
    }

    private Button createExportButton() {
        Button exportBtn = new Button(ResourceBundleUtils.getLangString(LangStrings.exportBtn));
        exportBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        exportBtn.setTooltip(new Tooltip(ResourceBundleUtils.getLangString(LangStrings.exportBtnToolTip)));
        exportBtn.setOnAction(event -> exportAction());

        return exportBtn;
    }

    private void createExportTextField() {
        exportTextField = new TextField();
        exportTextField.setPromptText(ResourceBundleUtils.getLangString(LangStrings.exportPromptText));

        exportTextField.setOnDragDetected(event -> {
            Dragboard db = exportTextField.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();

            List<File> files = new ArrayList<>();
            files.add(new File(exportTextField.getText()));

            content.putFiles(files);
            db.setContent(content);

            event.consume();
        });

        exportTextField.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && db.getFiles().get(0).isDirectory()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        // Dropping over project text field
        exportTextField.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.exists() && file.isDirectory()) {
                    success = true;
                    exportTextField.setText(file.getAbsolutePath());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Set text field value from the command line interface
        if (!Main.cliParameters.isEmpty() && !Main.cliParameters.get("export_folder").trim().equalsIgnoreCase("")) {
            exportTextField.setText(Main.cliParameters.get("export_folder").trim());
        }
    }

    private void createPane() {
        // General alignment and padding
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(15, 25, 10, 25));

        // Column constraints
        ColumnConstraints column1 = new ColumnConstraints(100);
        ColumnConstraints column2 = new ColumnConstraints(300, 1000, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(200);
        getColumnConstraints().addAll(column1, column2, column3);

        // Project selection area
        Label projectLabel = new Label(ResourceBundleUtils.getLangString(LangStrings.projectLabel));
        createProjectTextField();
        Button projectBtn = createProjectButton();

        // Export selection area
        Label exportLabel = new Label(ResourceBundleUtils.getLangString(LangStrings.exportLabel));
        createExportTextField();
        Button exportBtn = createExportButton();

        // Viewer selection area
        Label viewerLabel = new Label(ResourceBundleUtils.getLangString(LangStrings.viewerLabel));
        createViewerTextField();
        Button viewerBtn = createViewerButton();

        // Run button
        createRunButton();

        // Special button alignment to make all buttons the same width
        setFillWidth(projectBtn, true);
        setFillWidth(exportBtn, true);
        setFillWidth(viewerBtn, true);
        setFillWidth(runBtn, true);

        addRow(0, projectLabel, projectTextField, projectBtn);
        addRow(1, exportLabel, exportTextField, exportBtn);
        addRow(2, viewerLabel, viewerTextField, viewerBtn);

        add(runBtn, 2, 4);

        //Checks text fields and enable the run button if not empty and valid values
        BooleanBinding bb = new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return
                        !(Checker.checkTextField(projectTextField) && Checker.isFile(projectTextField.getText())) ||
                                !(Checker.checkTextField(exportTextField) && Checker.isDirectory(exportTextField.getText()));
                //!(Checker.checkTextField(viewerTextField) && Checker.isDirectory(viewerTextField.getText()));
            }

            {
                super.bind(projectTextField.textProperty(), exportTextField.textProperty());
                //super.bind(projectTextField.textProperty(), exportTextField.textProperty(), viewerTextField.textProperty());
            }
        };

        runBtn.disableProperty().bind(bb);

        // Add keyboard shortcuts for the buttons
        this.setOnKeyPressed(ke -> {
            if (ke.isShortcutDown() && ke.getCode() == KeyCode.DIGIT1) {
                projectAction();
            } else if (ke.isShortcutDown() && ke.getCode() == KeyCode.DIGIT2) {
                exportAction();
            } else if (ke.isShortcutDown() && ke.getCode() == KeyCode.DIGIT3) {
                viewerAction();
            } else if (ke.isShortcutDown() && ke.getCode() == KeyCode.E) {
                if (!runBtn.disabledProperty().get()) {
                    runAction();
                }
            }
        });

        // Set focus to the project text field for direct shortcut access
        Platform.runLater(() -> projectBtn.requestFocus());

        logger.log(Level.FINE, "FunctionPane created successful");
    }

    private Button createProjectButton() {
        Button projectBtn = new Button(ResourceBundleUtils.getLangString(LangStrings.projectBtn));
        projectBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        projectBtn.setTooltip(new Tooltip(ResourceBundleUtils.getLangString(LangStrings.projectBtnToolTip)));
        projectBtn.setOnAction(event -> projectAction());

        return projectBtn;
    }

    private void createProjectTextField() {
        projectTextField = new TextField();
        projectTextField.setPromptText(ResourceBundleUtils.getLangString(LangStrings.projectPromptText));

        projectTextField.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && db.getFiles().get(0).isFile()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        // Dropping over project text field
        projectTextField.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.exists() && file.isFile()) {
                    success = true;
                    projectTextField.setText(file.getAbsolutePath());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Set text field value from the command line interface
        if (!Main.cliParameters.isEmpty() && !Main.cliParameters.get("project").trim().equalsIgnoreCase("")) {
            projectTextField.setText(Main.cliParameters.get("project").trim());
        }
    }

    private void createRunButton() {
        runBtn = new Button(ResourceBundleUtils.getLangString(LangStrings.runBtn));
        runBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        runBtn.setTooltip(new Tooltip(ResourceBundleUtils.getLangString(LangStrings.runBtnToolTip)));
        runBtn.setOnAction(event -> runAction());
        runBtn.setDisable(true);
    }

    private Button createViewerButton() {
        Button viewerBtn = new Button(ResourceBundleUtils.getLangString(LangStrings.viewerBtn));
        viewerBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        viewerBtn.setTooltip(new Tooltip(ResourceBundleUtils.getLangString(LangStrings.viewerBtnToolTip)));
        viewerBtn.setOnAction(event -> viewerAction());

        return viewerBtn;
    }

    private void createViewerTextField() {
        viewerTextField = new TextField();
        viewerTextField.setPromptText(ResourceBundleUtils.getLangString(LangStrings.viewerPromptText));

        viewerTextField.setOnDragDetected(event -> {
            Dragboard db = viewerTextField.startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();

            List<File> files = new ArrayList<>();
            files.add(new File(viewerTextField.getText()));

            content.putFiles(files);
            db.setContent(content);

            event.consume();
        });

        viewerTextField.setOnDragOver(event -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && db.getFiles().get(0).isDirectory()) {
                event.acceptTransferModes(TransferMode.COPY);
            } else {
                event.consume();
            }
        });

        // Dropping over viewer text field
        viewerTextField.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasFiles()) {
                File file = db.getFiles().get(0);
                if (file.exists() && file.isDirectory()) {
                    success = true;
                    viewerTextField.setText(file.getAbsolutePath());
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        // Set text field value from the command line interface
        if (!Main.cliParameters.isEmpty() && !Main.cliParameters.get("viewer_folder").trim().equalsIgnoreCase("")) {
            viewerTextField.setText(Main.cliParameters.get("viewer_folder").trim());
        }
    }

    private void exportAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ResourceBundleUtils.getLangString(LangStrings.exportFileChooserTitle));

        if (!Main.pref.getUserPref("lastUsedExportFolder").trim().equalsIgnoreCase("")) {
            File last = new File(Main.pref.getUserPref("lastUsedExportFolder"));
            if (last.isDirectory()) {
                directoryChooser.setInitialDirectory(last);
            }
        }

        // Show open directory dialog
        File exportFolder = directoryChooser.showDialog(primaryStage);

        if (exportFolder != null) {
            exportTextField.setText(exportFolder.getPath());
            Main.pref.setUserPref("lastUsedExportFolder", exportFolder.getPath());
        }
    }

    private void projectAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(ResourceBundleUtils.getLangString(LangStrings.projectFileChooserTitle));

        if (!Main.pref.getUserPref("lastUsedProjectFile").trim().equalsIgnoreCase("")) {
            File last = new File(Main.pref.getUserPref("lastUsedProjectFile"));

            if (last.isFile()) {
                fileChooser.setInitialDirectory(last);
            }
        }

        // Set extension filter
        FileChooser.ExtensionFilter extensionFilter =
                new FileChooser.ExtensionFilter(ResourceBundleUtils.getLangString(LangStrings.filterExtensionDescriptionZF), "*.zfprj");
        fileChooser.getExtensionFilters().add(extensionFilter);

        // Show open file dialog
        File projectFile = fileChooser.showOpenDialog(primaryStage);

        if (projectFile != null) {
            projectTextField.setText(projectFile.getPath());
            Main.pref.setUserPref("lastUsedProjectFile", projectFile.getParent());
        }
    }

    private void runAction() {
        // Text field check is done by the Boolean binding which activates the run button!
        File projectFile = new File(projectTextField.getText());
        File exportFolder = new File(exportTextField.getText());
        File viewerFolder = new File(viewerTextField.getText());

        // Process project file
        ProjectFileWorker projectFileWorker = new ProjectFileWorker(projectFile);

        try {
            Main.isLoadedLaserRadarProject = projectFileWorker.processProjectFile();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            logger.log(Level.SEVERE, "can't load laser radar project", e);
            logger.log(Level.SEVERE, e.getMessage());
        }

        // Write coordinate file
        if (Main.isLoadedLaserRadarProject) {
            final String fileName = new File(projectTextField.getText()).getName();

            Main.isWrittenStandPointsFile = projectFileWorker.writeCoordinateFile(
                    exportFolder.getPath() + FileSystems.getDefault().getSeparator() + fileName + "_register.txt");
        }

        // Copy images
        if (Main.isWrittenStandPointsFile) {
            ExportFileWorker exportFileWorker = new ExportFileWorker(projectFile, exportFolder);
            exportFileWorker.exportPNG();
        }

        // Prepare viewer folder
        if (Main.isWrittenStandPointsFile && !viewerTextField.getText().trim().equalsIgnoreCase("")) {
            ViewerFileWorker viewerFileWorker = new ViewerFileWorker(projectFile, viewerFolder);
            Main.isWrittenViewerFile = viewerFileWorker.exportViewerJSON(projectFileWorker.getExtPoints());
        }
    }

    private void viewerAction() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(ResourceBundleUtils.getLangString(LangStrings.viewerFileChooserTitle));

        if (!Main.pref.getUserPref("lastUsedViewerFolder").trim().equalsIgnoreCase("")) {
            File last = new File(Main.pref.getUserPref("lastUsedViewerFolder"));
            if (last.isDirectory()) {
                directoryChooser.setInitialDirectory(last);
            }
        }

        // Show open directory dialog
        File viewerFolder = directoryChooser.showDialog(primaryStage);

        if (viewerFolder != null) {
            viewerTextField.setText(viewerFolder.getPath());
            Main.pref.setUserPref("lastUsedViewerFolder", viewerFolder.getPath());
        }
    }

} // end of FunctionPane
