package dk.cesit.androidsampleproject.util;

/**
 * Created by Cesit on 10/02/2017.
 */

public abstract class Result<T> {
    abstract public void onSuccess(T content);
    abstract public void onFailure(String message);
}
