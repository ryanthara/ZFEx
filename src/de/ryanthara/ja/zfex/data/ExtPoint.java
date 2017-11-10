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
package de.ryanthara.ja.zfex.data;

/**
 * The <tt>ExtPoint</tt> is used to hold the extended point information of the measured scan stations for writing
 * coordinate files and GeoJSON files.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class ExtPoint {

    private String number;
    private String x;
    private String y;
    private String z;

    /**
     * Constructs a new extended points by given string values for number, x-, y- and z coordinate.
     *
     * @param number the point number
     * @param x      the x coordinate
     * @param y      the y coordinate
     * @param z      the z coordinate
     */
    public ExtPoint(String number, String x, String y, String z) {
        this.number = number;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the point number string.
     *
     * @return point number
     */
    public String getNumber() {
        return number;
    }

    /**
     * Returns the x coordinate as string.
     *
     * @return x coordinate
     */
    public String getX() {
        return x;
    }

    /**
     * Returns the y coordinate as string.
     *
     * @return y coordinate
     */
    public String getY() {
        return y;
    }

    /**
     * Returns the z coordinate as string.
     *
     * @return z coordinate
     */
    public String getZ() {
        return z;
    }

    /**
     * Returns the point as string with four white space as delimiter.
     *
     * @return point as string
     */
    public String toString() {
        return getNumber() + "    " + getX() + "    " + getY() + "    " + getZ();
    }

} // end of ExtPoint
