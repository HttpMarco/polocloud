package dev.httpmarco.polocloud.common.downloading;

import java.io.InputStream;

@FunctionalInterface
public interface DownloadAcceptor<T> {

    T accept(InputStream stream) throws Exception;

}
