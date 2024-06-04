package dev.httpmarco.polocloud.api.events;

public interface CancelableEvent {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
