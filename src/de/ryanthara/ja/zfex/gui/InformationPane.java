/*
 * License: MIT. Copyright (c) 2017 by Sebastian Aust (https://www.ryanthara.de/)
 *
 * This file is part of the package de.ryanthara.ja.zfex.i18n
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
import javafx.application.HostServices;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * This is a special {@link HBox} which contains the information label with a short description for ZFEx.
 * <p>
 * All functions are implemented here as well.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
class InformationPane extends HBox {

    private HostServices hostServices;

    InformationPane(HostServices hostServices) {
        this.hostServices = hostServices;
        createPane();
    }

    private void createPane() {
        // General alignment and padding
        setPadding(new Insets(25, 25, 10, 25));
        setSpacing(8);

        // Left side positioned image
        Image image = new Image(getClass().getResourceAsStream(Images.iconInfo.getImagePath()));

        Hyperlink link = new Hyperlink();
        link.setBorder(Border.EMPTY);
        link.setGraphic(new ImageView(image));
        link.setOnAction(e -> hostServices.showDocument(Main.ZFEx_WEBSITE));

        // Information text on the right side (rude implementation with different Labels
        // in a VBox, one Label for every text)
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        Label info1 = new Label();
        info1.setText(ResourceBundleUtils.getLangString(LangStrings.informationText1));
        info1.setTextAlignment(TextAlignment.LEFT);
        info1.setWrapText(true);

        Label info2 = new Label();
        info2.setText(ResourceBundleUtils.getLangString(LangStrings.informationText2));
        info2.setTextAlignment(TextAlignment.LEFT);
        info2.setWrapText(true);

        Label info3 = new Label();
        info3.setText(ResourceBundleUtils.getLangString(LangStrings.informationText3));
        info3.setTextAlignment(TextAlignment.LEFT);
        info3.setWrapText(true);

        Label info4 = new Label();
        info4.setText(ResourceBundleUtils.getLangString(LangStrings.informationText4));
        info4.setTextAlignment(TextAlignment.LEFT);
        info4.setWrapText(true);

        vBox.getChildren().addAll(info1, info2, info3, info4);

        getChildren().addAll(link, vBox);
    }

} // end of InformationPane
