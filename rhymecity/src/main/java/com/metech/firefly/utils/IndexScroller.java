/*
 * Copyright 2011 woozzu
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.metech.firefly.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.SectionIndexer;

public class IndexScroller
{
	private static final int STATE_HIDDEN = 0;
	private static final int STATE_SHOWING = 1;
	private static final int STATE_SHOWN = 2;
	private static final int STATE_HIDING = 3;
	private float mIndexbarWidth;
	private float mIndexbarMargin;
	private float mPreviewPadding;
	private float mDensity;
	private float mScaledDensity;
	private float mAlphaRate;
	private int mState = STATE_HIDDEN;
	private int mListViewWidth;
	private int mListViewHeight;
	private int mCurrentSection = -1;
	private boolean mIsIndexing = false;
	private ListView mListView = null;
	private SectionIndexer mIndexer = null;
	private String[] mSections = null;
	private RectF mIndexbarRect;
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);

			switch (mState)
			{
				case STATE_SHOWING:
					// Fade in effect
					mAlphaRate += (1 - mAlphaRate) * 0.2;
					if (mAlphaRate > 0.9)
					{
						mAlphaRate = 1;
						setState(STATE_SHOWN);
					}

					mListView.invalidate();
					fade(10);
					break;

			}
		}

	};

	public IndexScroller(Context context, ListView lv)
	{
		mDensity = context.getResources().getDisplayMetrics().density;
		mScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
		mListView = lv;
		setAdapter(mListView.getAdapter());

		mIndexbarWidth = 25 * mDensity;
		mIndexbarMargin = 10 * mDensity;
		mPreviewPadding = 5 * mDensity;
	}

	public void draw(Canvas canvas)
	{
		// mAlphaRate determines the rate of opacity
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.parseColor("#f7f7f7"));
		indexbarPaint.setAlpha((int) (64 * mAlphaRate));
		indexbarPaint.setAntiAlias(true);

		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);

		if (mSections != null && mSections.length > 0)
		{
			// Preview is shown when mCurrentSection is set
			if (mCurrentSection >= 0)
			{
				Paint previewPaint = new Paint();
				previewPaint.setColor(Color.parseColor("#CFCFCF"));
				previewPaint.setAlpha(96);
				previewPaint.setAntiAlias(true);
				previewPaint.setShadowLayer(3, 0, 0, Color.argb(64, 0, 0, 0));

				Paint previewTextPaint = new Paint();
				previewTextPaint.setColor(Color.BLACK);
				previewTextPaint.setAntiAlias(true);


				previewTextPaint.setTextSize(45 * mScaledDensity);

				float previewTextWidth =
						previewTextPaint.measureText(mSections[mCurrentSection]);
				float previewSize = 2 * mPreviewPadding +
						previewTextPaint.descent() - previewTextPaint.ascent();
				RectF previewRect = new RectF((mListViewWidth - previewSize) / 2,
				                              (mListViewHeight - previewSize) / 2, (mListViewWidth -
						previewSize) / 2 + previewSize, (mListViewHeight - previewSize) /
						                              2 + previewSize);

				canvas.drawRoundRect(previewRect, 5 * mDensity, 5 * mDensity,
				                     previewPaint);
				canvas.drawText(mSections[mCurrentSection], previewRect.left +
						                (previewSize - previewTextWidth) / 2 - 1, previewRect.top +
						                mPreviewPadding - previewTextPaint.ascent() + 1,
				                previewTextPaint);
			}

			Paint indexPaint = new Paint();
			indexPaint.setColor(Color.BLACK);
			indexPaint.setAlpha((int) (255 * mAlphaRate));
			indexPaint.setAntiAlias(true);
			indexPaint.setTextSize(12 * mScaledDensity);

			float sectionHeight = (mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length;
			float paddingTop = (sectionHeight - (indexPaint.descent() - indexPaint.ascent())) / 2;

			int indexMaxSize = (int) Math.floor(sectionHeight);
			if (indexMaxSize != 0)
			{
				int delta = mSections.length / indexMaxSize + 1;

				if (delta < 1)
				{
					delta = 1;
				}

				if (delta >= 1)
				{
					for (int i = 0; i < mSections.length; i = i + delta)
					{
						float paddingLeft = (mIndexbarWidth - indexPaint.measureText(mSections[i])) / 2;

						if (i == 0)
						{
							canvas.drawText(mSections[0], mIndexbarRect.left + paddingLeft, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
						}
						else if (i >= mSections.length)
						{
							canvas.drawText(mSections[mSections.length], mIndexbarRect.left + paddingLeft, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
						}
						else
						{
							canvas.drawText(mSections[i], mIndexbarRect.left + paddingLeft, mIndexbarRect.top + mIndexbarMargin + sectionHeight * i + paddingTop - indexPaint.ascent(), indexPaint);
						}
					}
				}
			}
		}
	}

	public void remove(Canvas canvas)
	{
		Paint indexbarPaint = new Paint();
		indexbarPaint.setColor(Color.parseColor("#f7f7f7"));
		indexbarPaint.setAlpha((int) (200 * mAlphaRate));
		indexbarPaint.setAntiAlias(true);

		canvas.drawRoundRect(mIndexbarRect, 5 * mDensity, 5 * mDensity, indexbarPaint);
	}

	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				// If down event occurs inside index bar region, start indexing
				if (mState != STATE_HIDDEN && contains(ev.getX(), ev.getY()))
				{
					setState(STATE_SHOWN);

					// It demonstrates that the motion event started from index
					// bar
					mIsIndexing = true;
					// Determine which section the point is in, and move the
					// list to
					// that section
					mCurrentSection = getSectionByPoint(ev.getY());
					mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));

					return true;
				}
				break;

			case MotionEvent.ACTION_MOVE:
				if (mIsIndexing)
				{
					// If this event moves inside index bar
					if (contains(ev.getX(), ev.getY()))
					{
						// Determine which section the point is in, and move the
						// list to that section
						mCurrentSection = getSectionByPoint(ev.getY());
						mListView.setSelection(mIndexer.getPositionForSection(mCurrentSection));
					}
					return true;
				}
				break;

			case MotionEvent.ACTION_UP:
				if (mIsIndexing)
				{
					mIsIndexing = false;
					mCurrentSection = -1;
				}

				break;
		}

		return false;
	}

	public void hide()
	{
		if (mState == STATE_SHOWN)
		{
			setState(STATE_HIDING);
		}
	}

	public void directHide()
	{
		mHandler.removeMessages(0);
		fade(30);
		mListView.invalidate();

	}

	public void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		mListViewWidth = w;
		mListViewHeight = h;
		mIndexbarRect = new RectF(w - mIndexbarMargin - mIndexbarWidth, mIndexbarMargin, w - mIndexbarMargin, h - mIndexbarMargin);
	}

	public void show()
	{
		if (mState == STATE_HIDDEN)
		{
			setState(STATE_SHOWING);
		}
		else if (mState == STATE_HIDING)
		{
			setState(STATE_HIDING);
		}
	}

	public void setAdapter(Adapter adapter)
	{
		if (adapter instanceof SectionIndexer)
		{
			mIndexer = (SectionIndexer) adapter;
			mSections = (String[]) mIndexer.getSections();
		}
	}

	private void setState(int state)
	{
		if (state < STATE_HIDDEN || state > STATE_HIDING)
		{
			return;
		}
		mState = state;
		switch (mState)
		{
			case STATE_HIDDEN:
				// Cancel any fade effect
				mHandler.removeMessages(0);
				Log.e("STATE_HIDDEN", "true");
				break;

			case STATE_SHOWING:
				// Start to fade in
				mAlphaRate = 0;
				fade(0);
				break;

			case STATE_SHOWN:
				// Cancel any fade effect
				mHandler.removeMessages(0);
				break;
		}
	}

	private boolean contains(float x, float y)
	{
		// Determine if the point is in index bar region, which includes the
		// right margin of the bar
		return (x >= mIndexbarRect.left && y >= mIndexbarRect.top && y <= mIndexbarRect.top + mIndexbarRect.height());
	}

	private int getSectionByPoint(float y)
	{
		if (mSections == null || mSections.length == 0)
		{
			return 0;
		}
		if (y < mIndexbarRect.top + mIndexbarMargin)
		{
			return 0;
		}
		if (y >= mIndexbarRect.top + mIndexbarRect.height() - mIndexbarMargin)
		{
			return mSections.length - 1;
		}
		return (int) ((y - mIndexbarRect.top - mIndexbarMargin) / ((mIndexbarRect.height() - 2 * mIndexbarMargin) / mSections.length));
	}

	private void fade(long delay)
	{
		mHandler.removeMessages(0);
		mHandler.sendEmptyMessageAtTime(0, SystemClock.uptimeMillis() + delay);
	}
}
