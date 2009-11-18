package org.ddd.toolbox.naturalkeytools;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

public class NaturalKeyTools
{
	// TODO:
	// It does not make sense for NaturalKey properties to be collections.  However, we may want to enfore this here at some level. 
	
	private static NaturalKey getNaturalKeyAnnotation(Class<?> c)
	{
		return c.getAnnotation(NaturalKey.class);
	}

	private static String[] getNaturalKey(Class c)
	{
		NaturalKey annotation = getNaturalKeyAnnotation(c);
		if(annotation == null)
			throw new RuntimeException("No NaturalKey annotation present.");
		else
			return annotation.properties();
	}

	public static Criteria getCriteriaNaturalKey(Session hibernateSession, Object entity)
	{
		Criteria criteria = hibernateSession.createCriteria(entity.getClass());
		
		String[] properties = getNaturalKey(entity.getClass());
		for(String property : properties)
		{
			try
			{
				Object propertyValue = PropertyUtils.getSimpleProperty(entity, property);
				if (propertyValue == null)
				{
					throw new NullPointerException("NaturalKey property (" + property + ") is null.");
				}
				criteria.add(Restrictions.eq(property, propertyValue));
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e) 
			{
				throw new RuntimeException(e);
			} 
			catch (NoSuchMethodException e)
			{
				throw new RuntimeException(e);
			}
		}
        
		return criteria;
	}

	public static Criteria getDeepCriteriaNaturalKey(Session hibernateSession, Object entity)
	{
		Criteria rootCriteria = hibernateSession.createCriteria(entity.getClass());
		return getDeepCriteriaNaturalKey(entity, rootCriteria);
	}
	
	private static Criteria getDeepCriteriaNaturalKey(Object entity, Criteria rootCriteria)
	{
		String[] properties = getNaturalKey(entity.getClass());
		for(String property : properties)
		{
			try
			{
				Class propertyType = PropertyUtils.getPropertyType(entity, property);
				Object propertyValue = PropertyUtils.getSimpleProperty(entity, property);
				
				NaturalKey annotation = getNaturalKeyAnnotation(propertyType);
				if (annotation == null)
				{
					rootCriteria.add(Restrictions.eq(property, propertyValue));
				}
				else
				{
					getDeepCriteriaNaturalKey(propertyValue, rootCriteria.createCriteria(property));
				}
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e) 
			{
				throw new RuntimeException(e);
			} 
			catch (NoSuchMethodException e)
			{
				throw new RuntimeException(e);
			}
		}
		return rootCriteria;
	}
	
	/*
	public static Object getHetergeneousManagedGraphByNaturalKey(Session hibernateSession, Object entity)
	{
	
	}
	*/
	
	public static Object refreshDeepIdentityByNaturalKey(Session hibernateSession, Object entity)
	{
		String[] properties = getNaturalKey(entity.getClass());
		for(String property : properties)
		{
			try
			{
				Class propertyType = PropertyUtils.getPropertyType(entity, property);
				Object propertyValue = PropertyUtils.getSimpleProperty(entity, property);
			
				NaturalKey annotation = getNaturalKeyAnnotation(propertyType);
				if (annotation != null)
				{
					PropertyUtils.setSimpleProperty(entity, property, refreshDeepIdentityByNaturalKey(hibernateSession, propertyValue));
				}
			}
			catch (IllegalAccessException e)
			{
				throw new RuntimeException(e);
			}
			catch (InvocationTargetException e)
			{
				throw new RuntimeException(e);
			} 
			catch (NoSuchMethodException e)
			{
				throw new RuntimeException(e);
			}
		}
		
		// TODO: optimization: if the id is already set...
		
		return getCriteriaNaturalKey(hibernateSession, entity).uniqueResult();	
	}

}