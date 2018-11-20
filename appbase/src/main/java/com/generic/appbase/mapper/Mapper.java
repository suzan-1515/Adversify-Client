/*
 * *
 *  * All Rights Reserved.
 *  * Created by Suzn on 2018.
 *  * suzanparajuli@gmail.com
 *
 */

package com.generic.appbase.mapper;

public interface Mapper<From, To> {

    To from(From from);

    From to(To to);

}