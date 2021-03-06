/* Copyright 2017 Andrew Dawson
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

package com.bigfig.roma.entity

import com.google.gson.annotations.SerializedName

data class Notification(
        val type: Type,
        val id: String,
        val account: Account,
        val status: Status?) {

    enum class Type {
        @SerializedName("mention")
        MENTION,
        @SerializedName("reblog")
        REBLOG,
        @SerializedName("favourite")
        FAVOURITE,
        @SerializedName("follow")
        FOLLOW
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Notification) {
            return false
        }
        val notification = other as Notification?
        return notification?.id == this.id
    }
}
