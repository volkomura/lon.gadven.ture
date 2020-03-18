package com.template.application;

import android.content.Context;

/**
 * DON'T MODIFY THIS FILE
 */
public class Loader
{
    private Context context;

    private boolean is_finish = false;

    public Loader(Context context)
    {
        this.context = context;
    }

    public void load()
    {
        this.is_finish = true;
    }

    public boolean isFinish()
    {
        return this.is_finish;
    }
}
