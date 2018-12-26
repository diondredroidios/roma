/* Copyright 2018 charlag
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


package com.bigfig.roma.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import com.bigfig.roma.RomaApplication
import com.bigfig.roma.appstore.EventHub
import com.bigfig.roma.appstore.EventHubImpl
import com.bigfig.roma.db.AccountManager
import com.bigfig.roma.db.AppDatabase
import com.bigfig.roma.network.MastodonApi
import com.bigfig.roma.network.TimelineCases
import com.bigfig.roma.network.TimelineCasesImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by charlag on 3/21/18.
 */

@Module
class AppModule {

    @Provides
    fun providesApplication(app: RomaApplication): Application = app

    @Provides
    fun providesContext(app: Application): Context = app

    @Provides
    fun providesSharedPreferences(app: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(app)
    }

    @Provides
    fun providesBroadcastManager(app: Application): LocalBroadcastManager {
        return LocalBroadcastManager.getInstance(app)
    }

    @Provides
    fun providesTimelineUseCases(api: MastodonApi,
                                 eventHub: EventHub): TimelineCases {
        return TimelineCasesImpl(api, eventHub)
    }

    @Provides
    @Singleton
    fun providesAccountManager(app: RomaApplication): AccountManager {
        return app.serviceLocator.get(AccountManager::class.java)
    }

    @Provides
    @Singleton
    fun providesEventHub(): EventHub = EventHubImpl

    @Provides
    @Singleton
    fun providesDatabase(app: RomaApplication): AppDatabase {
        return app.serviceLocator.get(AppDatabase::class.java)
    }
}