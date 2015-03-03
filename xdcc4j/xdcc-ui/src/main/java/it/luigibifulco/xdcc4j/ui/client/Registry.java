package it.luigibifulco.xdcc4j.ui.client;



import java.util.HashMap;
import java.util.Map;



/**
 * A local storage of objects stored by id.
 */
public final class Registry  {

  protected static Map<String, Object> map = new HashMap<String, Object>();

  /**
   * Returns the object with the given id.
   * 
   * @param id the identifier
   * @return the object or <code>null</code> if no match
   */
  public static <X> X get(String id) {
    return (X) map.get(id);
  }

  /**
   * Returns a map of all registered objects.
   * 
   * @return the object map
   */
  public static Map getAll() {
    return map;
  }

  /**
   * Registers an object.
   * 
   * @param id the indentifier
   * @param obj the object to be registred
   */
  public static void register(String id, Object obj) {
    map.put(id, obj);
  }

  /**
   * Unregisters an object.
   * 
   * @param id the identifier
   */
  public static void unregister(String id) {
    map.remove(id);
  }

  /**
   * Unregisters all registered objects.
   */
  public static void unregisterAll() {
    map.clear();
  }

  private Registry() {
  }

}

