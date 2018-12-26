package com.bigfig.roma.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.bigfig.roma.entity.Attachment
import com.bigfig.roma.fragment.ViewMediaFragment

import java.util.Locale

class ImagePagerAdapter(
    fragmentManager: FragmentManager,
    private val attachments: List<Attachment>,
    private val initialPosition: Int
) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? {
        return if (position >= 0 && position < attachments.size) {
            ViewMediaFragment.newInstance(attachments[position], position == initialPosition)
        } else {
            null
        }
    }

    override fun getCount(): Int {
        return attachments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return String.format(Locale.getDefault(), "%d/%d", position + 1, attachments.size)
    }
}
