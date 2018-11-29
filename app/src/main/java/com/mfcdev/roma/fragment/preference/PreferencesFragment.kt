/* Copyright 2018 Conny Duck
 *
 * This file is a part of Roma.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * Roma is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with Roma; if not,
 * see <http://www.gnu.org/licenses>. */

package com.mfcdev.roma.fragment.preference

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.mfcdev.roma.PreferencesActivity
import com.mfcdev.roma.R
import com.mfcdev.roma.util.ThemeUtils
import com.mfcdev.roma.util.getNonNullString
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable

class PreferencesFragment : PreferenceFragmentCompat() {

    private val iconSize by lazy {resources.getDimensionPixelSize(R.dimen.preference_icon_size)}

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.preferences)

        val themePreference = findPreference("appTheme")
        themePreference.icon = IconicsDrawable(context, GoogleMaterial.Icon.gmd_palette).sizePx(iconSize).color(ThemeUtils.getColor(context, R.attr.toolbar_icon_tint))

        val emojiPreference = findPreference("emojiCompat")
        emojiPreference.icon = IconicsDrawable(context, GoogleMaterial.Icon.gmd_sentiment_satisfied).sizePx(iconSize).color(ThemeUtils.getColor(context, R.attr.toolbar_icon_tint))

        val textSizePreference = findPreference("statusTextSize")
        textSizePreference.icon = IconicsDrawable(context, GoogleMaterial.Icon.gmd_format_size).sizePx(iconSize).color(ThemeUtils.getColor(context, R.attr.toolbar_icon_tint))

        val timelineFilterPreferences = findPreference("timelineFilterPreferences")
        timelineFilterPreferences.setOnPreferenceClickListener {
            activity?.let { activity ->
                val intent = PreferencesActivity.newIntent(activity, PreferencesActivity.TAB_FILTER_PREFERENCES)
                activity.startActivity(intent)
                activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
            true
        }

        val httpProxyPreferences = findPreference("httpProxyPreferences")
        httpProxyPreferences.setOnPreferenceClickListener {
            activity?.let { activity ->
                val intent = PreferencesActivity.newIntent(activity, PreferencesActivity.PROXY_PREFERENCES)
                activity.startActivity(intent)
                activity.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()
        updateHttpProxySummary()
    }

    private fun updateHttpProxySummary() {

        val httpProxyPref = findPreference("httpProxyPreferences")

        val sharedPreferences = preferenceManager.sharedPreferences

            val httpProxyEnabled = sharedPreferences.getBoolean("httpProxyEnabled", false)

            val httpServer = sharedPreferences.getNonNullString("httpProxyServer", "")

            try {
                val httpPort = sharedPreferences.getNonNullString("httpProxyPort", "-1").toInt()

                if (httpProxyEnabled && httpServer.isNotBlank() && httpPort > 0 && httpPort < 65535) {
                    httpProxyPref.summary = "$httpServer:$httpPort"
                    return
                }
            } catch (e: NumberFormatException) {
                // user has entered wrong port, fall back to empty summary
            }

            httpProxyPref.summary = ""

    }

    companion object {
        fun newInstance(): PreferencesFragment {
            return PreferencesFragment()
        }
    }
}