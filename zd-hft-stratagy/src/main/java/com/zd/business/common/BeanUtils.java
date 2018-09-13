package com.zd.business.common;

import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class BeanUtils extends PropertyUtilsBean{
	
	/**
	 * 对象拷贝
	 * 数据对象空值不拷贝到目标对象
	 * 
	 * @param dataObject
	 * @param toObject
	 * @throws NoSuchMethodException
	 * copy
	 */
  public static void copyBeanNotNull2Bean(Object databean,Object tobean)throws Exception
  {
	  PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(databean);
      for (int i = 0; i < origDescriptors.length; i++) {
          String name = origDescriptors[i].getName();
//          String type = origDescriptors[i].getPropertyType().toString();
          if ("class".equals(name)) {
              continue; // No point in trying to set an object's class
          }
          if (PropertyUtils.isReadable(databean, name) &&PropertyUtils.isWriteable(tobean, name)) {
              try { 
                  Object value = PropertyUtils.getSimpleProperty(databean, name);
                  if(value!=null){
                	  getInstance().setSimpleProperty(tobean, name, value);
                  }
              }
              catch (java.lang.IllegalArgumentException ie) {
                  ; // Should not happen
              }
              catch (Exception e) {
                  ; // Should not happen
              }
          	}
          }
      }
}
