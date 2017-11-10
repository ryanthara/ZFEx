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
import de.ryanthara.ja.zfex.i18n.LangStrings;
import de.ryanthara.ja.zfex.i18n.ResourceBundleUtils;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * ExportFileWorker do all the file operations which has to be done to copy
 * and rename the 360° panoramic vies for every scan point.
 * <p>
 * The images are copied into a folder 'PNG' and renamed to the 'scan name'.png.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class ExportFileWorker {

    private final static Logger logger = Logger.getLogger(ExportFileWorker.class.getName());

    private File exportFolder;
    private File projectFile;
    private Path pngFolder;

    /**
     * Basic constructor without any functionality.
     *
     * @param projectFile  project file reference
     * @param exportFolder export folder reference
     */
    public ExportFileWorker(File projectFile, File exportFolder) {
        this.projectFile = projectFile;
        this.exportFolder = exportFolder;

        logger.setLevel(Main.loggingLevel);
        logger.addHandler(Main.fileHandler);
    }

    /**
     * Initialize the png export to the given export folder.
     */
    public void exportPNG() {
        if (createFolderPNG()) {
            File[] copyFiles = listPNGs();

            if (copyFiles != null) {
                copyInit(copyFiles);
            } else {
                Main.statusPane.setStatusIcon(Main.statusPane.STATUS_ERROR);
                Main.statusPane.setStatusText(ResourceBundleUtils.getLangString(LangStrings.statusCopyPNGError));
            }
        }
    }

    private void copyInit(File[] files) {
        ArrayList<String> duplicateFileNames = new ArrayList<>();
        ArrayList<File> notExistPath = new ArrayList<>();

        for (File file : files) {
            String fileName = file.getName().replaceAll(".pano360rf", "");

            Path src = Paths.get(file.getPath());
            Path dest = Paths.get(pngFolder.toString() + FileSystems.getDefault().getSeparator() + fileName);

            if (dest.toFile().exists()) {
                duplicateFileNames.add(fileName);
            } else {
                notExistPath.add(src.toFile());
            }
        }

        if (duplicateFileNames.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(ResourceBundleUtils.getLangString(LangStrings.copyPNGWarningTitle));
            alert.setHeaderText(ResourceBundleUtils.getLangString(LangStrings.copyPNGWarningHeaderText));
            alert.setContentText(ResourceBundleUtils.getLangString(LangStrings.copyPNGWarningText));

            ButtonType buttonOverwrite = new ButtonType(ResourceBundleUtils.getLangString(LangStrings.overwriteBtn));
            ButtonType buttonSkip = new ButtonType(ResourceBundleUtils.getLangString(LangStrings.skipBtn));
            ButtonType buttonTypeCancel = new ButtonType(ResourceBundleUtils.getLangString(LangStrings.cancelBtn),
                    ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonOverwrite, buttonSkip, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(null) == buttonOverwrite) {
                // ... user chose "One"
                copyPNGs(files);
            } else if (result.orElse(null) == buttonSkip) {
                // ... user chose "Two"
                if (notExistPath.toArray(new File[0]).length == 0) {
                    Main.statusPane.setStatusIcon(Main.statusPane.STATUS_OK);
                    Main.statusPane.setStatusText(ResourceBundleUtils.getLangString(LangStrings.statusExportPNGZero));
                } else {
                    copyPNGs(notExistPath.toArray(new File[0]));
                }
            } else if (result.orElse(null) == buttonTypeCancel) {
                Main.statusPane.setStatusIcon(Main.statusPane.STATUS_INFORMATION);
                Main.statusPane.setStatusText(ResourceBundleUtils.getLangString(LangStrings.statusExportPNGCancelled));
            }
        } else {
            copyPNGs(files);
        }
    }

    private void copyPNGSuccess(File[] copyFiles) {
        Main.statusPane.setStatusIcon(Main.statusPane.STATUS_OK);
        Main.statusPane.setStatusText(String.format(
                ResourceBundleUtils.getLangString(LangStrings.statusExportPNGSuccess), copyFiles.length, exportFolder.getName()));

        if (Main.isLoadedLaserRadarProject && Main.isWrittenStandPointsFile && Main.isWrittenViewerFile) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(ResourceBundleUtils.getLangString(LangStrings.workSuccessTitle));
            alert.setHeaderText(ResourceBundleUtils.getLangString(LangStrings.workSuccessHeaderText));
            alert.setContentText(ResourceBundleUtils.getLangString(LangStrings.workSuccessContentText));

            alert.showAndWait();
        }
    }

    /*
     * Creates the folder with java.nio functions
     */
    private void copyPNGs(File[] files) {
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                for (int i = 0; i < files.length; i++) {
                    final double step = i + 1.0;
                    final int counter = i + 1;

                    File file = files[i];
                    String fileName = file.getName().replaceAll(".pano360rf", "");

                    Path src = Paths.get(file.getPath());
                    Path dest = Paths.get(pngFolder.toString() + FileSystems.getDefault().getSeparator() + fileName);

                    try {
                        Files.copy(src, dest, REPLACE_EXISTING);

                        Main.statusPane.setProgress(step / files.length);
                    } catch (IOException e) {
                        logger.log(Level.SEVERE, "error while copying png files", e);
                        logger.log(Level.SEVERE, e.getMessage());

                        final String status = String.format(ResourceBundleUtils.getLangString(LangStrings.statusExportPNGFailed),
                                counter, exportFolder.getName());

                        Main.statusPane.setStatusIcon(Main.statusPane.STATUS_ERROR);
                        Main.statusPane.setStatusText(status);
                    }
                }

                return null;
            }
        };

        task.setOnSucceeded(e -> copyPNGSuccess(files));

        new Thread(task).start();
    }

    /*
     * Creates the folder with java.nio functions
     */
    private boolean createFolderPNG() {
        boolean success = false;

        pngFolder = Paths.get(exportFolder.getPath() + FileSystems.getDefault().getSeparator() + "PNG");

        if (Files.exists(pngFolder)) {
            success = true;

            Main.statusPane.setStatusIcon(Main.statusPane.STATUS_INFORMATION);
            Main.statusPane.setStatusText(String.format(
                    ResourceBundleUtils.getLangString(LangStrings.statusCreatePNGFolderExists), exportFolder.getName()));
        } else {
            try {
                Files.createDirectories(pngFolder);
                success = true;
            } catch (IOException e) {
                logger.log(Level.SEVERE, "png folder creation failed");
                logger.log(Level.SEVERE, e.getMessage());

                e.printStackTrace();
            }

            if (success) {
                final String status = String.format(ResourceBundleUtils.getLangString(LangStrings.statusCreatePNGFolderSuccess),
                        exportFolder.getName());

                Main.statusPane.setStatusIcon(Main.statusPane.STATUS_OK);
                Main.statusPane.setStatusText(status);
            } else {
                final String status = String.format(ResourceBundleUtils.getLangString(LangStrings.statusCreatePNGFolderFailed),
                        exportFolder.getName());

                Main.statusPane.setStatusIcon(Main.statusPane.STATUS_ERROR);
                Main.statusPane.setStatusText(status);
            }
        }

        return success;
    }

    /*
     * Lists only png files that are valid and contains the char sequence '.pano360rf.png'.
     * The indicator '.pano360rf.png' is given by ZF Laser Control and identify the needed panoramic files.
     */
    private File[] listPNGs() {
        FilenameFilter pngFilter = (dir, name) -> new File(dir, name).isFile() &&
                name.toLowerCase().contains(".pano360rf.png") &&
                name.toLowerCase().endsWith(".png");

        return new File(projectFile.getParent() + File.separator + ".temp").listFiles(pngFilter);
    }

} // end of ExportFileWorker
