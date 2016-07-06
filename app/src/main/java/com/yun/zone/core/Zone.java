package com.yun.zone.core;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 2016/7/6.
 */
public class Zone {
    protected String userName;
    protected static Zone _this;
    protected Context context;
    private HashMap<String, List<ZoneModel>> dataCache;


    protected static Zone getInstance() {
        return _this;
    }

    public static void init(Context context, String userName) {
        if (_this == null || !_this.userName.equals(userName)) {
            _this = new Zone(context, userName);
        }
    }

    public static void init(Context context) {
        if (_this == null || !_this.userName.equals("default")) {
            _this = new Zone(context, "default");
        }
    }

    private Zone(Context context, String userName) {
        this.context = context;
        this.userName = userName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();

    }

    private void initData() {
        dataCache = new HashMap<String, List<ZoneModel>>();
        File[] files = FileUtil.getFiles(context, userName);
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                String[] modelStrList = FileUtil.read(file).split("\n\r");
                List<ZoneModel> zoneModels = new ArrayList<ZoneModel>();
                for (String modelStr : modelStrList) {
                    zoneModels.add(getModel(fileName, modelStr));
                }
                dataCache.put(fileName, zoneModels);
            }
        }
    }

    private ZoneModel getModel(String modelName, String modelStr) {
        Class<?> modelClass = null;
        Object obj = null;
        try {
            modelClass = Class.forName(modelName);
            obj = modelClass.newInstance();
            Field[] fields = getFields(modelClass);
            String[] keyValues = modelStr.split(",");
            for (String keyValue : keyValues) {
                String key = keyValue.substring(0, keyValue.indexOf(":"));
                String value = keyValue.substring(keyValue.indexOf(":") + 1, keyValue.length());
                for (Field field : fields) {
                    if (key.equals(field.getName())) {
                        setVariables(obj, field, value);
                    }
                }
            }
            return (ZoneModel) obj;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected static Field[] getFields(Class<?> modelClass) {
        Field[] field = modelClass.getFields();
        return field;
    }


    protected void setVariables(Object obj, Field field, String variableValue) {
        try {
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (type == int.class) {
                field.set(obj, Integer.valueOf(variableValue));
            } else if (type == double.class) {
                field.set(obj, Double.valueOf(variableValue));
            } else if (type == float.class) {
                field.set(obj, Float.valueOf(variableValue));
            } else if (type == long.class) {
                field.set(obj, Long.valueOf(variableValue));
            } else if (type == String.class) {
                field.set(obj, String.valueOf(variableValue));
            } else if (type == Date.class) {
                field.set(obj, new Date(variableValue));
            } else if (type == Boolean.class) {
                field.set(obj, Boolean.valueOf(variableValue));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static List findAll(Class modelClass) {
        List<ZoneModel> temp = new ArrayList<ZoneModel>();
        temp.addAll(_this.dataCache.get(modelClass.getName()));
        return temp;
    }

    private static ZoneModel find(Class modelClass, ZoneModel model) {
        List<ZoneModel> temp = _this.dataCache.get(modelClass.getName());
        if (temp != null) {
            for (ZoneModel zoneModel : temp) {
                if (zoneModel.defaultKey.equals(model.defaultKey)) {
                    return zoneModel;
                }
            }
        }
        return null;
    }

    private static void add(Class modelClass, ZoneModel model) {
        List<ZoneModel> temp = _this.dataCache.get(modelClass.getName());
        if (temp != null) {
            temp.add(model);
        } else {
            temp = new ArrayList<ZoneModel>();
            temp.add(model);
            _this.dataCache.put(modelClass.getName(), temp);
        }
    }

    private static void update(Class modelClass, ZoneModel oldModel, ZoneModel newModel) {
        List<ZoneModel> temp = _this.dataCache.get(modelClass.getName());
        if (temp != null) {
            temp.remove(oldModel);
            temp.add(newModel);
        }
    }


    protected static void saveOrUpdate(Class modelClass, ZoneModel model) {
        ZoneModel oldModel = _this.find(modelClass, model);
        if (oldModel == null) {
            _this.add(modelClass, model);
        } else {
            _this.update(modelClass, oldModel, model);
        }
    }
}
