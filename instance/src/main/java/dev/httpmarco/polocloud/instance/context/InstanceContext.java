package dev.httpmarco.polocloud.instance.context;

import java.io.File;

public abstract class InstanceContext {

    public abstract ClassLoader context(File bootFile);

}
