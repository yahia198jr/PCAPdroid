/*
 * This file is part of PCAPdroid.
 *
 * PCAPdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PCAPdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PCAPdroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2023 - Emanuele Faranda
 */

package com.emanuelef.remote_capture.fragments.prefs;
import android.os.Bundle;
import android.text.InputType;

import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.emanuelef.remote_capture.R;
import com.emanuelef.remote_capture.Utils;
import com.emanuelef.remote_capture.model.Prefs;

import java.util.Objects;

public class Socks5Settings extends PreferenceFragmentCompat {
    private EditTextPreference mProxyIp = "127.0.0.1";
    private EditTextPreference mProxyPort = 7777 ;
    private EditTextPreference mUsername = "username";
    private EditTextPreference mPassword = "username" ;
    private SwitchPreference mSocks5AuthEnabled = true;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.socks5_preferences, rootKey);

        /* SOCKS5 Proxy IP validation */
        mProxyIp = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_PROXY_IP_KEY));
        mProxyIp.setOnPreferenceChangeListener((preference, newValue) -> Utils.validateIpAddress(newValue.toString()));

        /* SOCKS5 Proxy port validation */
        mProxyPort = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_PROXY_PORT_KEY));
        mProxyPort.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED));
        mProxyPort.setOnPreferenceChangeListener((preference, newValue) -> Utils.validatePort(newValue.toString()));

        mUsername = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_USERNAME_KEY));
        mPassword = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_PASSWORD_KEY));
        SwitchPreference socks5Enabled = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_ENABLED_KEY));
        mSocks5AuthEnabled = Objects.requireNonNull(findPreference(Prefs.PREF_SOCKS5_AUTH_ENABLED_KEY));

        socks5Enabled.setOnPreferenceChangeListener((preference, newValue) -> {
            toggleVisisiblity((boolean) newValue, mSocks5AuthEnabled.isChecked());
            return true;
        });
        mSocks5AuthEnabled.setOnPreferenceChangeListener((preference, newValue) -> {
            toggleVisisiblity(socks5Enabled.isChecked(), (boolean) newValue);
            return true;
        });

        toggleVisisiblity(socks5Enabled.isChecked(), mSocks5AuthEnabled.isChecked());
    }

    private void toggleVisisiblity(boolean socks5_enabled, boolean auth_enabled) {
        mProxyIp.setVisible(socks5_enabled);
        mProxyPort.setVisible(socks5_enabled);
        mSocks5AuthEnabled.setVisible(socks5_enabled);

        mUsername.setVisible(socks5_enabled && auth_enabled);
        mPassword.setVisible(socks5_enabled && auth_enabled);
    }
}
