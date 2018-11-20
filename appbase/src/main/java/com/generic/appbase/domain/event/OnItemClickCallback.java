package com.generic.appbase.domain.event;

import android.view.View;

/**
 * All Rights Reserved.
 * Created by Suzn on 3/23/2018.
 * suzanparajuli@gmail.com
 */

public interface OnItemClickCallback<T> {

    void onItemClick(View v, int position, T t);

}
