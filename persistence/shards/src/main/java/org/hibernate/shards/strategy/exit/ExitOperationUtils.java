/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.hibernate.shards.strategy.exit;

import org.hibernate.shards.util.Lists;
import org.hibernate.shards.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Maulik Shah
 */
public class ExitOperationUtils {

  public static List<Object> getNonNullList(List<Object> list) {
    List<Object> nonNullList = Lists.newArrayList();
    for(Object obj : list) {
      if(obj != null) {
        nonNullList.add(obj);
      }
    }
    return nonNullList;
  }

  public static List<Comparable<Object>> getComparableList(List<Object> results) {
    @SuppressWarnings("unchecked")
    List<Comparable<Object>> result = (List<Comparable<Object>>) (List) results;
    return result;
  }


  public static Comparable<Object> getPropertyValue(Object obj, String propertyName) {
    /**
     * TODO(maulik) respect the client's choice in how Hibernate accesses
     * property values.
     *
     * Currently this method access members of an object using getters only,
     * event of the client has specifed to use direct field access. Ideally,
     * we could get an EntityPersister from the SessionFactoryImplementor and
     * use that. However, hibernate's EntityPersister expects all properties
     * to be a ComponentType. In pratice, these objects are interconnected in
     * the mapping and Hibernate instantiates them as BagType or ManyToOneType,
     * i.e. as they are specified in the mappings. Hence, we cannot use
     * Hibernate's EntityPersister.
     */

    try {
      StringBuilder propertyPath = new StringBuilder();
      for(int i=0; i < propertyName.length(); i++) {
        String s =propertyName.substring(i,i+1);
        if (i == 0 || propertyName.charAt(i-1) == '.') {
          propertyPath.append(StringUtil.capitalize(s));
        } else {
          propertyPath.append(s);
        }
      }
      String[] methods = ("get" + propertyPath.toString().replaceAll("\\.", ".get")).split("\\.");
      Object root = obj;
      for (String method : methods) {
        Class clazz = root.getClass();
        Method m = clazz.getMethod(method);
        root = m.invoke(root);
        if (root == null) {
          break;
        }
      }
      @SuppressWarnings("unchecked")
      Comparable<Object> result = (Comparable<Object>) root;
      return result;
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

}
