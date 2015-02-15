package it.luigibifulco.xdcc4j.db;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import org.neodatis.odb.ODB;
import org.neodatis.odb.ODBFactory;
import org.neodatis.odb.OID;
import org.neodatis.odb.Objects;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;

public abstract class ObjectStore<E> {
	protected ODB odb;
	private final Class<E> clazz;

	public ObjectStore(String name, Class<E> objectType) {
		odb = ODBFactory.open(name);

		this.clazz = objectType;
	}

	public Collection<E> getAll() {
		return odb.getObjects(clazz);
	}

	public E get(String id) {
		Objects<E> objs = odb.getObjects(clazz);
		objs = odb.getObjects(new CriteriaQuery(clazz, Where.equal("id", id)));
		if (objs != null && objs.size() > 0) {
			E e = objs.getFirst();
			// odb.disconnect(e);
			return e;
		}
		return null;
	}

	public long count() {
		BigInteger i = odb.count(new CriteriaQuery(clazz));
		return i.longValueExact();
	}

	public E saveOrUpdate(String id, E e) {
		return update(id, e, true);
	}

	@SuppressWarnings("unchecked")
	public E insert(E e) {
		OID id = odb.store(e);
		odb.commit();
		E attached = (E) odb.getObjectFromId(id);
		odb.disconnect(attached);
		return attached;

	}

	protected E update(String id, E modifiedObj, boolean insertIfNotExists) {
		E e = get(id);
		if (e == null) {
			if (!insertIfNotExists) {
				return insert(modifiedObj);
			} else {
				return null;
			}
		}
		odb.delete(e);
		return insert(modifiedObj);
	}

	public E updateProperty(String id, String property, String newValue) {
		return null;
	}

	public int clear() {
		Collection<E> list = getAll();
		int cnt = list.size();
		odb.getObjects(getEntityType());
		for (E e : list) {
			OID id = odb.getObjectId(e);
			System.out.println(id);

			odb.delete(odb.getObjectFromId(id));
			// odb.deleteCascade(e);
		}
		odb.commit();
		return cnt;
	}

	public long remove(E e) {
		// odb.reconnect(e);
		OID id = odb.delete(e);
		odb.commit();
		return id.getObjectId();
	}

	public boolean close() {
		odb.rollback();
		odb.close();
		return true;
	}

	protected abstract Class<E> getEntityType();

	public abstract List<E> searchByExample(E e);
}
