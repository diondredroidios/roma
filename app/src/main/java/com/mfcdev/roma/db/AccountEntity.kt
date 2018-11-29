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

package com.mfcdev.roma.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters

import com.mfcdev.roma.entity.Emoji
import com.mfcdev.roma.entity.Status

@Entity(indices = [Index(value = ["domain", "accountId"],
        unique = true)])
@TypeConverters(Converters::class)
data class AccountEntity(@field:PrimaryKey(autoGenerate = true) var id: Long,
                         val domain: String,
                         var accessToken: String,
                         var isActive: Boolean,
                         var accountId: String = "",
                         var username: String = "",
                         var displayName: String = "",
                         var profilePictureUrl: String = "",
                         var notificationsEnabled: Boolean = true,
                         var notificationsMentioned: Boolean = true,
                         var notificationsFollowed: Boolean = true,
                         var notificationsReblogged: Boolean = true,
                         var notificationsFavorited: Boolean = true,
                         var notificationSound: Boolean = true,
                         var notificationVibration: Boolean = true,
                         var notificationLight: Boolean = true,
                         var defaultPostPrivacy: Status.Visibility = Status.Visibility.PUBLIC,
                         var defaultMediaSensitivity: Boolean = false,
                         var alwaysShowSensitiveMedia: Boolean = false,
                         var mediaPreviewEnabled: Boolean = true,
                         var lastNotificationId: String = "0",
                         var activeNotifications: String = "[]",
                         var emojis: List<Emoji> = emptyList()) {

    val identifier: String
        get() = "$domain:$accountId"

    val fullName: String
        get() = "@$username@$domain"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountEntity

        if (id == other.id) return true
        if (domain == other.domain && accountId == other.accountId) return true

        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + domain.hashCode()
        result = 31 * result + accountId.hashCode()
        return result
    }
}
