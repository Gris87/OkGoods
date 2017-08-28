package ru.okmarket.okgoods.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.util.Locale;

public final class AnimationUtils
{
    // region Statics
    // region Tag
    @SuppressWarnings("unused")
    private static final String TAG = "AnimationUtils";
    // endregion
    // endregion



    private AnimationUtils()
    {
        // Nothing
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static void expand(final View view, float speed)
    {
        if (speed <= 0)
        {
            throw new IllegalArgumentException(String.format(Locale.US, "speed (%1$.2f) should be positive", speed));
        }

        view.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation)
            {
                view.getLayoutParams().height = interpolatedTime >= 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);

                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms * speed
        animation.setDuration((int)(targetHeight / view.getContext().getResources().getDisplayMetrics().density / speed));
        view.startAnimation(animation);
    }

    @SuppressWarnings("NumericCastThatLosesPrecision")
    public static void collapse(final View view, float speed)
    {
        if (speed <= 0)
        {
            throw new IllegalArgumentException(String.format(Locale.US, "speed (%1$.2f) should be positive", speed));
        }

        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation)
            {
                if (interpolatedTime >= 1)
                {
                    view.setVisibility(View.GONE);
                }
                else
                {
                    view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);

                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds()
            {
                return true;
            }
        };

        // 1dp/ms * speed
        animation.setDuration((int)(initialHeight / view.getContext().getResources().getDisplayMetrics().density / speed));
        view.startAnimation(animation);
    }

    public static void fadeIn(final View view, int duration)
    {
        if (duration <= 0)
        {
            throw new IllegalArgumentException(String.format(Locale.US, "duration (%1$d) should be positive", duration));
        }

        view.setVisibility(View.VISIBLE);
        view.setAlpha(0);

        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation)
            {
                view.setAlpha(interpolatedTime);
            }

            @Override
            public boolean willChangeBounds()
            {
                return false;
            }
        };

        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void fadeOut(final View view, int duration)
    {
        if (duration <= 0)
        {
            throw new IllegalArgumentException(String.format(Locale.US, "duration (%1$d) should be positive", duration));
        }

        view.setAlpha(1);

        Animation animation = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation transformation)
            {
                if (interpolatedTime >= 1)
                {
                    view.setVisibility(View.GONE);
                }
                else
                {
                    view.setAlpha(1 - interpolatedTime);
                }
            }

            @Override
            public boolean willChangeBounds()
            {
                return false;
            }
        };

        animation.setDuration(duration);
        view.startAnimation(animation);
    }
}
