package com.yun.zone.core;

import java.io.File;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by dell on 2016/7/6.
 */
public class ZoneModel {
    public String defaultKey;

    public void save() {
        boolean isNew = false;
        Class<?> modelClass = getClass();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (defaultKey == null) {
                Random ra = new Random();
                defaultKey = df.format(new Date());
                defaultKey = new StringBuffer(defaultKey).append(ra.nextInt()).toString();
                isNew = true;
            }
            Field[] fields = Zone.getFields(modelClass);
            StringBuffer modelBuffer = new StringBuffer();
            for (Field field : fields) {
                if (field.get(this) != null) {

                    modelBuffer.append(field.getName()).append(":").append(field.get(this)).append(",");

                }
            }
            Zone.saveOrUpdate(modelClass, this);
            File file = FileUtil.createFile(Zone.getInstance().context, Zone.getInstance().userName, getClass().getName());
            if (isNew) {
                FileUtil.write(modelBuffer.toString(), file);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
