package it.luigibifulco.xdcc4j.db;

import it.luigibifulco.xdcc4j.common.model.XdccRequest;
import it.luigibifulco.xdcc4j.common.util.XdccRequestCreator;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.And;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public class XdccRequestStore extends ObjectStore<XdccRequest> {

	public XdccRequestStore(String name) {
		super(name, XdccRequest.class);
	}

	public XdccRequest saveOrUpdate(String id, XdccRequest e) {
		e = XdccRequestCreator.identify(e);
		return saveOrUpdate(id, e);
	};

	@Override
	public XdccRequest insert(XdccRequest e) {
		e = XdccRequestCreator.identify(e);
		return super.insert(e);
	}

	@Override
	public List<XdccRequest> searchByExample(XdccRequest e) {
		if (e == null) {
			return null;
		}
		CriteriaQuery query = new CriteriaQuery(XdccRequest.class);
		Field[] fields = e.getClass().getDeclaredFields();
		And and = Where.and();
		for (Field field : fields) {
			Class<?> fType = field.getType();
			Object o;
			try {

				o = e.getClass()
						.getMethod(
								"get" + StringUtils.capitalize(field.getName()),
								new Class[] {}).invoke(e, new Object[] {});

			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e2) {
				continue;
			}

			if (o != null && !o.toString().isEmpty()) {

				if (fType.equals(String.class)) {
					if (field.getName().equalsIgnoreCase("id")) {
						continue;
					} else {
						and.add(Where.ilike(field.getName(), o.toString()));
					}
				}
			}
		}
		query.setCriterion(and);
		Objects<XdccRequest> requests = odb.getObjects(query);
		return new ArrayList<XdccRequest>(requests);
	}

	@Override
	protected Class<XdccRequest> getEntityType() {
		return XdccRequest.class;
	}

}
