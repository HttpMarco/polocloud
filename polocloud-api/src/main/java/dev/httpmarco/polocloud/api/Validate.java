package dev.httpmarco.polocloud.api;

public interface Validate<B, C> {

    boolean valid(B b, C c);

}
