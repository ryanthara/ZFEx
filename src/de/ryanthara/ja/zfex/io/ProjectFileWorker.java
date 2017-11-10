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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

import static de.ryanthara.ja.zfex.Main.statusPane;

/**
 * ProjectFileWorker do all the operations on the ZF Laser Radar project file (*.zfprj).
 * <p>
 * First the xml based file is read, then parsed into a DOM and afterwards extract all the needed information
 * for preparing the coordinate export and the file copying of the 360° panoramic scan views.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class ProjectFileWorker {

    private ExtPoint[] extPoints = new ExtPoint[0];
    private File projectFile;

    /**
     * Default constructor without any functionality.
     *
     * @param projectFile the project file to walk through
     */
    public ProjectFileWorker(File projectFile) {
        this.projectFile = projectFile;
    }

    /**
     * Returns the array of {@link ExtPoint}
     *
     * @return array of extended points
     */
    public ExtPoint[] getExtPoints() {
        return extPoints;
    }

    public boolean processProjectFile() throws IOException, SAXException, ParserConfigurationException {
        boolean success = false;

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(projectFile);

        //Optional, but recommended
        //Read this: http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("Viewpoint");

        extPoints = new ExtPoint[nodeList.getLength()];

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                //String name = element.getAttribute("name");
                String register = element.getElementsByTagName("RegisterMatrix").item(0).getTextContent();
                String scans = element.getElementsByTagName("Scans").item(0).getTextContent().trim();

                String[] coordinates = register.split("[\\s]+");
                String x = coordinates[3];
                String y = coordinates[7];
                String z = coordinates[11];

                // Remove '.zfs' file ending string
                String scan = scans.substring(0, scans.lastIndexOf('.'));

                extPoints[i] = new ExtPoint(scan, x, y, z);
            }
        }

        if (nodeList.getLength() == extPoints.length) {
            success = true;

            statusPane.setStatusIcon(statusPane.STATUS_OK);
            statusPane.setStatusText(String.format(
                    ResourceBundleUtils.getLangString(LangStrings.statusLaserRadarProjectReadSuccess), projectFile.getName()));
        } else {
            statusPane.setStatusIcon(statusPane.STATUS_ERROR);
            statusPane.setStatusText(String.format(
                    ResourceBundleUtils.getLangString(LangStrings.statusLaserRadarProjectReadFailed), projectFile.getName()));
        }

        return success;
    }

    /**
     * Writes the coordinate file into a text file.
     *
     * @param fileName file name of the to be written file
     *
     * @return success of file writing
     */
    public boolean writeCoordinateFile(String fileName) {
        boolean success = false;

        ArrayList<String> lines = new ArrayList<>();

        for (ExtPoint extPoint : extPoints) {
            lines.add(extPoint.toString());
        }

        Path targetFile = Paths.get(fileName);

        if (targetFile.toFile().exists()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(ResourceBundleUtils.getLangString(LangStrings.coordinateFileConfirmationTitle));
            alert.setHeaderText(ResourceBundleUtils.getLangString(LangStrings.coordinateFileConfirmationHeaderText));
            alert.setContentText(ResourceBundleUtils.getLangString(LangStrings.coordinateFileConfirmationText));

            Optional<ButtonType> result = alert.showAndWait();

            if (result.orElse(null) == ButtonType.OK) {
                success = write(fileName, lines);
            } else if (result.orElse(null) == ButtonType.CANCEL) {
                Main.statusPane.setStatusIcon(statusPane.STATUS_OK);
                Main.statusPane.setStatusText("");
            }
        } else {
            success = write(fileName, lines);
        }

        return success;
    }

    private boolean write(String fileName, ArrayList<String> lines) {
        boolean success;

        LineWriter lineWriter = new LineWriter(fileName);
        success = lineWriter.writeList(lines);

        if (success) {
            statusPane.setStatusIcon(statusPane.STATUS_OK);
            statusPane.setStatusText(String.format(
                    ResourceBundleUtils.getLangString(LangStrings.statusLaserRadarProjectExportCoordinatesSuccess),
                    new File(fileName).getName()));
        } else {
            statusPane.setStatusIcon(statusPane.STATUS_ERROR);
            statusPane.setStatusText(String.format(
                    ResourceBundleUtils.getLangString(LangStrings.statusLaserRadarProjectExportCoordinatesFailed),
                    new File(fileName).getName()));
        }

        return success;
    }

} // end of ProjectFileWorker
