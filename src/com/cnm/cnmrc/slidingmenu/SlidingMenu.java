/*
 * Copyright (C) 2012 0xlab - http://0xlab.org/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authored by Julian Chu <walkingice AT 0xlab.org>
 */

package com.cnm.cnmrc.slidingmenu;

// update the package name to match your app
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

import com.cnm.cnmrc.R;

public class SlidingMenu extends ViewGroup {

    private final static int DURATION = 200;

    protected boolean mPlaceLeft = false;
    protected boolean mOpened;
    protected View mSidebar;
    protected View mContent;
    protected int mSidebarWidth = 145; /* assign default value. It will be overwrite
                                          in onMeasure by Layout xml resource. */

    protected Animation mAnimation;
    protected OpenListener    mOpenListener;
    protected CloseListener   mCloseListener;
    protected Listener mListener;

    protected boolean mPressed = false;

    public SlidingMenu(Context context) {
        this(context, null);
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        mSidebar = findViewById(R.id.menu);
        mContent = findViewById(R.id.content);

        if (mSidebar == null) {
            throw new NullPointerException("no view id = animation_sidebar");
        }

        if (mContent == null) {
            throw new NullPointerException("no view id = animation_content");
        }

        mOpenListener = new OpenListener(mSidebar, mContent);
        mCloseListener = new CloseListener(mSidebar, mContent);
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        /* the title bar assign top padding, drop it */
        int sidebarLeft = l;
        if (!mPlaceLeft) {
            sidebarLeft = r - mSidebarWidth;
        }
        mSidebar.layout(sidebarLeft,
                0,
                sidebarLeft + mSidebarWidth,
                0 + mSidebar.getMeasuredHeight());

        if (mOpened) {
            if (mPlaceLeft) {
                mContent.layout(l + mSidebarWidth, 0, r + mSidebarWidth, b);
            } else  {
                mContent.layout(l - mSidebarWidth, 0, r - mSidebarWidth, b);
            }
        } else {
            mContent.layout(l, 0, r, b);	// 기본화면
            //mContent.layout(l - mSidebarWidth, 0, r - mSidebarWidth, b);
        }
    }

    @Override
    public void onMeasure(int w, int h) {
        super.onMeasure(w, h);
        super.measureChildren(w, h);
        mSidebarWidth = mSidebar.getMeasuredWidth();
    }

    @Override
    protected void measureChild(View child, int parentWSpec, int parentHSpec) {
        /* the max width of Sidebar is 90% of Parent */
        if (child == mSidebar) {
            int mode = MeasureSpec.getMode(parentWSpec);
            int width = (int)(getMeasuredWidth() * 0.9);
            super.measureChild(child, MeasureSpec.makeMeasureSpec(width, mode), parentHSpec);
        } else {
            super.measureChild(child, parentWSpec, parentHSpec);
        }
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {*/
        /*if (!isOpening()) {
            return false;
        }

        int action = ev.getAction();

        if (action != MotionEvent.ACTION_UP
                && action != MotionEvent.ACTION_DOWN) {
            return false;
        }*/

        /* if user press and release both on Content while
         * sidebar is opening, call listener. otherwise, pass
         * the event to child. */
        /*int x = (int)ev.getX();
        int y = (int)ev.getY();
        if (mContent.getLeft() < x
                && mContent.getRight() > x
                && mContent.getTop() < y
                && mContent.getBottom() > y) {
            if (action == MotionEvent.ACTION_DOWN) {
                mPressed = true;
            }

            if (mPressed
                    && action == MotionEvent.ACTION_UP
                    && mListener != null) {
                mPressed = false;
                return mListener.onContentTouchedWhenOpening();
            }
        } else {
            mPressed = false;
        }*/

/*        return false;
    }*/

    public void setListener(Listener l) {
        mListener = l;
    }

    /* to see if the Sidebar is visible */
    public boolean isOpening() {
        return mOpened;
    }

    public void toggleSidebar() {
        if (mContent.getAnimation() != null) {
            return;
        }

        if (mOpened) {
            /* opened, make close animation*/
            if (mPlaceLeft) {
                mAnimation = new TranslateAnimation(0, -mSidebarWidth, 0, 0);
            } else {
                mAnimation = new TranslateAnimation(0, mSidebarWidth, 0, 0);
            }
            mAnimation.setAnimationListener(mCloseListener);
        } else {
            /* not opened, make open animation */
            if (mPlaceLeft) {
                mAnimation = new TranslateAnimation(0, mSidebarWidth, 0, 0);
            } else {
                mAnimation = new TranslateAnimation(0, -mSidebarWidth, 0, 0);
            }
            mAnimation.setAnimationListener(mOpenListener);
        }
        mAnimation.setDuration(DURATION);
        
//        mAnimation.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
//        mAnimation.setInterpolator(new AnticipateOvershootInterpolator(1.0f, 1.0f));
//        mAnimation.setInterpolator(new DecelerateInterpolator(2.0f)); // ok
        
        mAnimation.setInterpolator(new OvershootInterpolator(1.7f));
        
        mAnimation.setFillAfter(true);
        mAnimation.setFillEnabled(true);
        mContent.startAnimation(mAnimation);
    }

    public void openSidebar() {
        if (!mOpened) {
            toggleSidebar();
        }
    }

    public void closeSidebar() {
        if (mOpened) {
            toggleSidebar();
        }
    }

    class OpenListener implements Animation.AnimationListener {
        View iSidebar;
        View iContent;

        OpenListener(View sidebar, View content) {
            iSidebar = sidebar;
            iContent = content;
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
            //iSidebar.setVisibility(View.VISIBLE);
            if (mListener != null) {
                mListener.onSidebarOpened();
            }
        }

        public void onAnimationEnd(Animation animation) {
            iContent.clearAnimation();
            mOpened = !mOpened;
            requestLayout();
            /*if (mListener != null) {
                mListener.onSidebarOpened();
            }*/
        }
    }

    class CloseListener implements Animation.AnimationListener {
        View iSidebar;
        View iContent;

        CloseListener(View sidebar, View content) {
            iSidebar = sidebar;
            iContent = content;
        }

        public void onAnimationRepeat(Animation animation) {
        }
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            iContent.clearAnimation();
            //iSidebar.setVisibility(View.INVISIBLE);
            mOpened = !mOpened;
            requestLayout();
            if (mListener != null) {
                mListener.onSidebarClosed();
            }
        }
    }

    public interface Listener {
        public void onSidebarOpened();
        public void onSidebarClosed();
        public boolean onContentTouchedWhenOpening();
    }
}
