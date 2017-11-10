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
import de.ryanthara.ja.zfex.data.ExtPoint;
import de.ryanthara.ja.zfex.i18n.LangStrings;
import de.ryanthara.ja.zfex.i18n.ResourceBundleUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static de.ryanthara.ja.zfex.Main.statusPane;

/**
 * {@link ViewerFileWorker} does all the operations for the initial preparing of JSON files
 * for using in the scan data viewer.
 * <p>
 * The scan data viewer is not part of ZFEx.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class ViewerFileWorker {

    private File projectFile;
    private File viewerFolder;

    /**
     * Basic constructor with a reference to the viewer folder.
     *
     * @param projectFile  project file reference
     * @param viewerFolder viewer folder reference
     */
    public ViewerFileWorker(File projectFile, File viewerFolder) {
        this.projectFile = projectFile;
        this.viewerFolder = viewerFolder;
    }

    /**
     * Writes the needed 'JSON like' viewer file to the file system.
     * <p>
     * Due to some reasons there is no Java JSON (e.g. @see (javax.json.Json)) functionality used at the moment.
     *
     * @param extPoints array with the coordinates to write
     *
     * @return write success
     */
    public boolean exportViewerJSON(ExtPoint[] extPoints) {
        boolean success = true;

        final String fileName = viewerFolder.getPath() + FileSystems.getDefault().getSeparator() + getProjectName() + ".js";
        Path output = Paths.get(fileName);

        if (output.toFile().exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(ResourceBundleUtils.getLangString(LangStrings.viewerFileConfirmationTitle));
            alert.setHeaderText(ResourceBundleUtils.getLangString(LangStrings.viewerFileConfirmationHeaderText));
            alert.setContentText(ResourceBundleUtils.getLangString(LangStrings.viewerFileConfirmationText));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(null) == ButtonType.OK) {
                success = export(fileName, extPoints);
            } else if (result.orElse(null) == ButtonType.CANCEL) {
                Main.statusPane.setStatusText("");
            }
        } else {
            success = export(fileName, extPoints);
        }

        if (success) {
            Main.statusPane.setStatusIcon(statusPane.STATUS_OK);
            Main.statusPane.setStatusText(String.format(ResourceBundleUtils.getLangString(LangStrings.statusViewerExportSuccess), output.getFileName().toString()));
        }

        return success;
    }

    private boolean export(String fileName, ExtPoint[] extPoints) {
        boolean success;

        final String varId = getProjectName().toUpperCase();
        final String id = getProjectName();

        StringBuilder builder = new StringBuilder();

        builder.append("var " + varId + " = {" + System.lineSeparator());
        builder.append("    'type': 'FeatureCollection'," + System.lineSeparator());
        builder.append("    'id': '" + id + "'," + System.lineSeparator());
        builder.append("    'features': [" + System.lineSeparator());

        for (int i = 0; i < extPoints.length; i++) {
            ExtPoint point = extPoints[i];

            String s = "       ";

            if (i == 0) {
                s = s + " ";
            } else {
                s = s + ",";
            }
            s = s + "{ \"type\": \"Feature\", \"geometry\": { \"type\": \"Point\", \"coordinates\": [%s, %s] }, \"properties\": { \"ID\": %s, \"Typ\": \"Punkt\" } }" + System.lineSeparator();

            builder.append(String.format(s, point.getX(), point.getY(), point.getNumber()));
        }

        builder.append("    ]" + System.lineSeparator());
        builder.append("};");

        LineWriter lineWriter = new LineWriter(fileName);
        success = lineWriter.writeBuilder(builder);

        return success;
    }

    private String getProjectName() {
        return this.projectFile.getName().substring(0, this.projectFile.getName().lastIndexOf("."));
    }

} // end of ViewerFileWorker
