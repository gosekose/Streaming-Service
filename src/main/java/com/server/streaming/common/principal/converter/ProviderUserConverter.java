package com.server.streaming.common.principal.converter;

public interface ProviderUserConverter<T, R> {

    R converter(T t);
}
