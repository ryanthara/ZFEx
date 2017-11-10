/*
 * License: MIT. Copyright (c) 2017 by Sebastian Aust (https://www.ryanthara.de/)
 *
 * This file is part of the package de.ryanthara.ja.zfex
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

import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * The <tt>PreferenceHandler</tt> is used to handle preferences and the listener controlled update of them.
 *
 * @author sebastian
 * @version 1
 * @since 1
 */
public class PreferenceHandler implements PreferenceChangeListener {

    /**
     * Member for the preference key for the RyCON position on the first monitor.
     */
    public final static String LAST_POS_PRIMARY_MONITOR = "param_pos_primary_monitor";
    private Preferences userPreferences;

    public PreferenceHandler() {
        userPreferences = Preferences.userRoot().node("/de/ryanthara/zfex");

        // add listener to the node and not to an instance of it!
        Preferences.userRoot().node("/de/ryanthara/zfex").addPreferenceChangeListener(this);
    }

    /**
     * Returns a system preference by given name.
     *
     * @param prefName name of the system preference to be read
     *
     * @return system preference as String
     */
    public String getUserPref(String prefName) {
        return userPreferences.get(prefName, "");
    }

    /**
     * This method gets called when a preference is added, removed or when
     * its value is changed.
     * <p>
     *
     * @param event A PreferenceChangeEvent object describing the event source
     *              and the preference that has changed.
     */
    @Override
    public void preferenceChange(PreferenceChangeEvent event) {
        // System.out.println("preference changed: " + event.getKey());
    }

    /**
     * Sets a defined system preference by name and value.
     *
     * @param prefName name of the system preference to be set
     * @param value    value to be set
     */
    public void setUserPref(String prefName, String value) {
        userPreferences.put(prefName, value);
    }

} // end of PreferenceHandler
