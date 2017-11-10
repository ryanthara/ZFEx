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

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * This is a special {@link GridPane} which contains the status of ZFEx.
 * <p>
 * It contains a three coloured signal on the left side, the status message area in the center
 * and a {@link ProgressIndicator} on the right side.
 */
public class StatusPane extends GridPane {

    public final int STATUS_OK = 0;
    public final int STATUS_INFORMATION = 5;
    public final int STATUS_ERROR = 10;
    private ProgressIndicator progressIndicator;
    private Label imageLabel;
    private Label textLabel;
    private String statusText;
    private Image imgERROR;
    private Image imgINFO;
    private Image imgOK;

    /**
     * Default constructor with initialisation functionality.
     */
    StatusPane() {
        this.statusText = "";
        loadImages();
        createPane();
    }

    /**
     * Sets the progress of the progress indicator for the file copy operation.
     * <p>
     * After the start of ZFEx the progress indicator is hidden, so it has to be set visible here.
     *
     * @param progress the progress value
     */
    public void setProgress(double progress) {
        if (!progressIndicator.isVisible()) {
            progressIndicator.setVisible(true);
        }

        progressIndicator.setProgress(progress);
    }

    /**
     * Sets the status icon.
     *
     * @param status status icon to be set
     */
    public void setStatusIcon(int status) {
        switch (status) {
            case 0:
                imageLabel.setGraphic(new ImageView(imgOK));
                break;
            case 5:
                imageLabel.setGraphic(new ImageView(imgINFO));
                break;
            case 10:
                imageLabel.setGraphic(new ImageView(imgERROR));
                break;
            default:
                break;
        }
    }

    /**
     * Sets the status text with the given string.
     *
     * @param statusText status text to be set
     */
    public void setStatusText(String statusText) {
        this.statusText = statusText;
        textLabel.setText(statusText);
    }

    private void createPane() {
        // General alignment and padding
        setAlignment(Pos.CENTER);
        setHgap(10);
        setVgap(10);
        setPadding(new Insets(15, 25, 10, 25));

        // Column constraints
        ColumnConstraints column1 = new ColumnConstraints(50);
        ColumnConstraints column2 = new ColumnConstraints(300, 1000, Double.MAX_VALUE);
        column2.setHgrow(Priority.ALWAYS);
        ColumnConstraints column3 = new ColumnConstraints(50);
        getColumnConstraints().addAll(column1, column2, column3);

        // Icon area (left)
        imageLabel = new Label();
        imageLabel.setGraphic(new ImageView(imgOK));

        // Status text (middle)
        textLabel = new Label();
        textLabel.setText(statusText);
        textLabel.setAlignment(Pos.BOTTOM_LEFT);

        // Progress indicator (right)
        progressIndicator = new ProgressIndicator(0);
        progressIndicator.setVisible(false);

        addRow(0, imageLabel, textLabel, progressIndicator);
    }

    private void loadImages() {
        imgERROR = new Image(getClass().getResourceAsStream(Images.statusError.getImagePath()));
        imgINFO = new Image(getClass().getResourceAsStream(Images.statusInfo.getImagePath()));
        imgOK = new Image(getClass().getResourceAsStream(Images.statusOK.getImagePath()));
    }

} // end of StatusPane
