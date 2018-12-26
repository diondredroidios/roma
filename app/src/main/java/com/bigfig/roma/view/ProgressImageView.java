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

package com.bigfig.roma.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bigfig.roma.R;
import at.connyduck.sparkbutton.helpers.Utils;

public final class ProgressImageView extends AppCompatImageView {

    private int progress = -1;
    private final RectF progressRect = new RectF();
    private final RectF biggerRect = new RectF();
    private final Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint clearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint markBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Drawable captionDrawable;

    public ProgressImageView(Context context) {
        super(context);
        init();
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        circlePaint.setStrokeWidth(Utils.dpToPx(getContext(), 4));
        circlePaint.setStyle(Paint.Style.STROKE);

        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        markBgPaint.setStyle(Paint.Style.FILL);
        markBgPaint.setColor(ContextCompat.getColor(getContext(),
                R.color.description_marker_unselected));
        captionDrawable = AppCompatResources.getDrawable(getContext(), R.drawable.spellcheck);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (progress != -1) {
            setColorFilter(Color.rgb(123, 123, 123), PorterDuff.Mode.MULTIPLY);
        } else {
            clearColorFilter();
        }
        invalidate();
    }

    public void setChecked(boolean checked) {
        this.markBgPaint.setColor(ContextCompat.getColor(getContext(),
                checked ? R.color.colorPrimary : R.color.description_marker_unselected));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = (progress / 100f) * 360 - 90;
        float halfWidth = canvas.getWidth() / 2;
        float halfHeight = canvas.getHeight() / 2;
        progressRect.set(halfWidth * 0.75f, halfHeight * 0.75f, halfWidth * 1.25f, halfHeight * 1.25f);
        biggerRect.set(progressRect);
        int margin = 8;
        biggerRect.set(progressRect.left - margin, progressRect.top - margin, progressRect.right + margin, progressRect.bottom + margin);
        canvas.saveLayer(biggerRect, null, Canvas.ALL_SAVE_FLAG);
        if (progress != -1) {
            canvas.drawOval(progressRect, circlePaint);
            canvas.drawArc(biggerRect, angle, 360 - angle - 90, true, clearPaint);
        }
        canvas.restore();

        int circleRadius = Utils.dpToPx(getContext(), 14);
        int circleMargin = Utils.dpToPx(getContext(), 14);

        int circleY = canvas.getHeight() - circleMargin - circleRadius / 2;
        int circleX = canvas.getWidth() - circleMargin - circleRadius / 2;

        canvas.drawCircle(circleX, circleY, circleRadius, markBgPaint);

        captionDrawable.setBounds(canvas.getWidth() - circleMargin - circleRadius,
                canvas.getHeight() - circleMargin - circleRadius,
                canvas.getWidth() - circleMargin,
                canvas.getHeight() - circleMargin);
        DrawableCompat.setTint(captionDrawable, Color.WHITE);
        captionDrawable.draw(canvas);
    }
}