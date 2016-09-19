package lostandfound.mi.ur.de.lostandfound.helpers;

/**
 * Created by Konstantin on 11.09.2016.
 */
public interface Filter<T> {
    String getField();

    String getQueryString();

    boolean applyFilter(T var1);
}
