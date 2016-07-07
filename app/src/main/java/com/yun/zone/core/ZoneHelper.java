package com.yun.zone.core;

/**
 * Created by dell on 2016/7/7.
 */
public interface ZoneHelper {
    /**
     * model转为String
     *
     * @return
     */
    public String toString();

    /**
     * string转为model
     *
     * @param model
     */
    public void fromString(String model);
}
