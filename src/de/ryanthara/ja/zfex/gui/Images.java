/*
 * License: GPL. Copyright 2016- (C) by Sebastian Aust (https://www.ryanthara.de/)
 *
 * This file is part of the package de.ryanthara.ja.zfex.gui
 *
 * This package is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This package is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this package. If not, see <http://www.gnu.org/licenses/>.
 */
package de.ryanthara.ja.zfex.gui;

/**
 * The <tt>Images</tt> enumeration holds the paths to all used icons for <tt>ZFEx</tt>.
 * <p>
 * This enumeration is used for encapsulating the data.
 *
 * @author sebastian
 * @version 1
 * @since 2.0
 */
public enum Images {

    iconInfo("/de/ryanthara/ja/zfex/icons/information.png"),
    iconZFEx("/de/ryanthara/ja/zfex/icons/zfex-icon.png"),
    statusError("/de/ryanthara/ja/zfex/icons/status_error.png"),
    statusInfo("/de/ryanthara/ja/zfex/icons/status_information.png"),
    statusOK("/de/ryanthara/ja/zfex/icons/status_ok.png");

    private String imagePath;

    Images(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    @Override
    public String toString() {
        return imagePath;
    }

} // end of Images
