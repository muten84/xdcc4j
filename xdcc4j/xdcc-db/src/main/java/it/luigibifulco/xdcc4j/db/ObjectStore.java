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
		odb = ODBFactory.open("./db/" + name);
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

	@SuppressWarnings("unchecked")
	public E insert(E e) {
		OID id = odb.store(e);
		odb.commit();
		E attached = (E) odb.getObjectFromId(id);
		odb.disconnect(attached);
		return attached;
	}

	public int clear() {
		Collection<E> list = getAll();
		int cnt = list.size();
		for (E e : list) {
			odb.deleteCascade(e);
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
