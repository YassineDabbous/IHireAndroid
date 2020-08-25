package tn.core.domain.base;

import tn.core.domain.Failure;

public interface Closure<D> {
    public void onSuccess(D response);
    public void onError(Failure failure);
}
