package com.mfcdev.roma.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.mfcdev.roma.fragment.ViewMediaFragment

class AvatarImagePagerAdapter(fragmentManager: FragmentManager, private val avatarUrl: String) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return if (position == 0) {
            ViewMediaFragment.newAvatarInstance(avatarUrl)
        } else {
            null
        }
    }

    override fun getCount() = 1

}
