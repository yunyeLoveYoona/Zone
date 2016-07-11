package com.yun.zone.core;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private int lineNum;


    /**
     * @return
     * @throws NullPointerException
     */
    protected static Zone getInstance() throws NullPointerException {
        checkThis();
        return _this;
    }

    /**
     * @param context
     * @param userName
     */
    public static void init(Context context, String userName) {
        if (_this == null || !_this.userName.equals(userName)) {
            _this = new Zone(context, userName);
        }
    }

    /**
     * @param context
     */
    public static void init(Context context) {
        if (_this == null || !_this.userName.equals("default")) {
            _this = new Zone(context, "default");
        }
    }

    /**
     * @throws NullPointerException
     */
    private static void checkThis() throws NullPointerException {
        if (_this == null) {
            throw new NullPointerException("Zone is not init");
        }
    }

    /**
     * @param context
     * @param userName
     */
    private Zone(Context context, String userName) {
        this.context = context;
        this.userName = userName;
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        }).start();
        lineNum = 1;
    }

    /**
     * 从文件中加载数据
     */
    private void initData() {
        dataCache = new HashMap<String, List<ZoneModel>>();
        File[] files = FileUtil.getFiles(context, userName);
        if (files != null && files.length > 0) {
            for (File file : files) {
                lineNum = 1;
                String fileName = file.getName();
                String[] modelStrList = FileUtil.read(file).split("\n");
                List<ZoneModel> zoneModels = new ArrayList<ZoneModel>();
                for (String modelStr : modelStrList) {
                    zoneModels.add(getModel(fileName, modelStr));
                }
                dataCache.put(fileName, zoneModels);
            }
        }
    }

    /**
     * 通过字符串反射单个model
     *
     * @param modelName
     * @param modelStr
     * @return
     */
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
                String value = keyValue.substring(keyValue.indexOf(":") + 1, keyValue.length()).replace("@#$", "/n").replace("&^", ":").replace("*&", ",");
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


    /**
     * 获取类中所有变量
     *
     * @param modelClass
     * @return
     */
    protected static Field[] getFields(Class<?> modelClass) {
        Field[] field = modelClass.getFields();
        return field;
    }


    /**
     * 设置某个变量值
     *
     * @param obj
     * @param field
     * @param variableValue
     */
    protected void setVariables(Object obj, Field field, String variableValue) {
        try {
            field.setAccessible(true);
            if (field.getName().equals("lineNum")) {
                field.set(obj, lineNum);
                lineNum = lineNum + 1;
            } else {
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
                    field.set(obj, String.valueOf(variableValue).replace("@#$", "\n"));
                } else if (type == Date.class) {
                    field.set(obj, new Date(variableValue));
                } else if (type == Boolean.class) {
                    field.set(obj, Boolean.valueOf(variableValue));
                } else if (type == ZoneHelper.class) {
                    Method method = type.getMethod("fromString", new Class[]{String.class});
                    ZoneHelper zoneHelper = ZoneHelper.class.newInstance();
                    method.invoke(zoneHelper, variableValue);
                    field.set(obj, zoneHelper);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取某个model的所有数据
     *
     * @param modelClass
     * @return
     * @throws NullPointerException
     */
    public static List findAll(Class modelClass) throws NullPointerException {
        checkThis();
        List<ZoneModel> temp = new ArrayList<ZoneModel>();
        if (_this.dataCache.get(modelClass.getName()) != null) {
            temp.addAll(_this.dataCache.get(modelClass.getName()));
        }
        return temp;
    }

    /**
     * 查找是否存在相同主键的model
     *
     * @param modelClass
     * @param model
     * @return
     */
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

    /**
     * @param modelClass
     * @param model
     */
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

    /**
     * @param modelClass
     * @param oldModel
     * @param newModel
     */
    private static void update(Class modelClass, ZoneModel oldModel, ZoneModel newModel) {
        List<ZoneModel> temp = _this.dataCache.get(modelClass.getName());
        if (temp != null) {
            temp.remove(oldModel);
            temp.add(newModel);
        }
    }


    /**
     * @param modelClass
     * @param model
     * @throws NullPointerException
     */
    protected static void saveOrUpdate(Class modelClass, ZoneModel model) throws NullPointerException {
        checkThis();
        ZoneModel oldModel = _this.find(modelClass, model);
        if (oldModel == null) {
            _this.add(modelClass, model);
            model.lineNum = _this.dataCache.get(modelClass.getName()).size();
        } else {
            _this.update(modelClass, oldModel, model);
        }
    }


    protected static boolean delete(Class modelClass, ZoneModel model) {
        ZoneModel oldModel = _this.find(modelClass, model);
        if (oldModel == null) {
            return false;
        } else {
            _this.dataCache.get(modelClass.getName()).remove(oldModel);
            for (ZoneModel zoneModel : _this.dataCache.get(modelClass.getName())) {
                if (zoneModel.lineNum > model.lineNum) {
                    zoneModel.lineNum = zoneModel.lineNum - 1;
                }
            }
            return true;
        }
    }

    /**
     * @param start
     * @param end
     * @param modelClass
     * @return
     * @throws NullPointerException
     */
    public static List limit(int start, int end, Class modelClass) throws NullPointerException {
        List<ZoneModel> temp = findAll(modelClass);
        if (temp.size() > 0) {
            if (end - start > temp.size()) {
                return temp.subList(0, temp.size() - 1);
            }
            return temp.subList(start, end);
        }
        return null;
    }

    public static List where(WhereCondition whereCondition, Class modelClass, String fieldName, Object value) {
        List<ZoneModel> temp = findAll(modelClass);
        return where(temp, whereCondition, modelClass, fieldName, value);
    }

    public static List where(List<ZoneModel> zoneModels, WhereCondition whereCondition, Class modelClass, String fieldName, Object value) {
        Field[] fields = modelClass.getFields();
        Field tempField = null;
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                tempField = field;
            }
        }
        if (tempField != null) {
            List<ZoneModel> temp = zoneModels;
            List<ZoneModel> returnList = new ArrayList<ZoneModel>();
            if (temp.size() > 0) {
                switch (whereCondition) {
                    case EQUALS:
                        for (ZoneModel zoneModel : temp) {
                            try {
                                if (tempField.get(zoneModel).equals(value)) {
                                    returnList.add(zoneModel);
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        return returnList;

                    case CONTAINS:
                        if (tempField.getType() == String.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (tempField.get(zoneModel).toString().contains(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return returnList;
                    case MORE_THAN:
                        if (tempField.getType() == int.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Integer.valueOf(tempField.get(zoneModel).toString()) > Integer.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == float.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Float.valueOf(tempField.get(zoneModel).toString()) > Float.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == double.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Double.valueOf(tempField.get(zoneModel).toString()) > Double.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == long.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Long.valueOf(tempField.get(zoneModel).toString()) > Long.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return returnList;
                    case LESS_THAN:
                        if (tempField.getType() == int.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Integer.valueOf(tempField.get(zoneModel).toString()) < Integer.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == float.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Float.valueOf(tempField.get(zoneModel).toString()) < Float.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == double.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Double.valueOf(tempField.get(zoneModel).toString()) < Double.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else if (tempField.getType() == long.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (Long.valueOf(tempField.get(zoneModel).toString()) < Long.valueOf(value.toString())) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return returnList;
                    case BEFORE:
                        if (tempField.getType() == Date.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (new Date(tempField.get(zoneModel).toString()).before((Date) value)) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return returnList;
                    case AFTER:
                        if (tempField.getType() == Date.class) {
                            for (ZoneModel zoneModel : temp) {
                                try {
                                    if (new Date(tempField.get(zoneModel).toString()).after((Date) value)) {
                                        returnList.add(zoneModel);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return returnList;
                }

            }
        }

        return null;
    }


    public static List orderBy(Class modelClass, final String sortField, final String sortMode) {
        List<ZoneModel> temp = findAll(modelClass);
        if (temp.size() < 1) {
            return null;
        }
        Collections.sort(temp, new Comparator<ZoneModel>() {
            @Override
            public int compare(ZoneModel o1, ZoneModel o2) {
                try {
                    Class clazz = o1.getClass();
                    Field field = clazz.getDeclaredField(sortField);
                    field.setAccessible(true);
                    String typeName = field.getType().getName().toLowerCase();
                    Object v1 = field.get(o1);
                    Object v2 = field.get(o2);
                    boolean ASC_order = (sortMode == null || "ASC".equalsIgnoreCase(sortMode));
                    if (typeName.endsWith("string")) {
                        String value1 = v1.toString();
                        String value2 = v2.toString();
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("short")) {
                        Short value1 = Short.parseShort(v1.toString());
                        Short value2 = Short.parseShort(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("byte")) {
                        Byte value1 = Byte.parseByte(v1.toString());
                        Byte value2 = Byte.parseByte(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("char")) {
                        Integer value1 = (int) (v1.toString().charAt(0));
                        Integer value2 = (int) (v2.toString().charAt(0));
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("int") || typeName.endsWith("integer")) {
                        Integer value1 = Integer.parseInt(v1.toString());
                        Integer value2 = Integer.parseInt(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("long")) {
                        Long value1 = Long.parseLong(v1.toString());
                        Long value2 = Long.parseLong(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("float")) {
                        Float value1 = Float.parseFloat(v1.toString());
                        Float value2 = Float.parseFloat(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("double")) {
                        Double value1 = Double.parseDouble(v1.toString());
                        Double value2 = Double.parseDouble(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("boolean")) {
                        Boolean value1 = Boolean.parseBoolean(v1.toString());
                        Boolean value2 = Boolean.parseBoolean(v2.toString());
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else if (typeName.endsWith("date")) {
                        Date value1 = (Date) (v1);
                        Date value2 = (Date) (v2);
                        return ASC_order ? value1.compareTo(value2) : value2.compareTo(value1);
                    } else {
                        Method method = field.getType().getDeclaredMethod("compareTo", new Class[]{field.getType()});
                        method.setAccessible(true);
                        int result = (Integer) method.invoke(v1, new Object[]{v2});
                        return ASC_order ? result : result * (-1);
                    }
                } catch (Exception e) {
                    String err = e.getLocalizedMessage();
                    System.out.println(err);
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return temp;
    }


    public enum WhereCondition {
        EQUALS, BEFORE, AFTER, CONTAINS, MORE_THAN, LESS_THAN
    }
}
