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
    protected int lineNum;

    /**
     * @throws NullPointerException
     */
    public synchronized void saveOrUpdate() throws NullPointerException {
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
                    if (field.getType() == ZoneHelper.class) {
                        ZoneHelper zoneHelper = (ZoneHelper) field.get(this);
                        modelBuffer.append(field.getName()).append(":").append(zoneHelper.toString().replace(":", "&^").replace(",", "*&")).append(",");
                    } else {
                        modelBuffer.append(field.getName()).append(":").append(field.get(this).toString().replace(":", "&^").replace(",", "*&")).append(",");
                    }

                }
            }
            Zone.saveOrUpdate(modelClass, this);
            File file = FileUtil.createFile(Zone.getInstance().context, Zone.getInstance().userName, getClass().getName());
            if (isNew) {
                FileUtil.write(modelBuffer.toString().replace("\n", "@#$"), file);
            } else {
                FileUtil.update(file, modelBuffer.toString().replace("\n", "@#$"), this.lineNum);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean delete() {
        if (defaultKey == null || lineNum == 0) {
            return false;
        }
        if (Zone.delete(getClass(), this)) {
            File file = FileUtil.createFile(Zone.getInstance().context, Zone.getInstance().userName, getClass().getName());
            FileUtil.deleteLine(file, lineNum);
        }
        return true;
    }

}
