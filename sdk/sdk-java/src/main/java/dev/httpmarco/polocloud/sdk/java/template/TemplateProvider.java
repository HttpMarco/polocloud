package dev.httpmarco.polocloud.sdk.java.template;

import dev.httpmarco.polocloud.common.future.FutureConverterKt;
import dev.httpmarco.polocloud.shared.template.SharedTemplateProvider;
import dev.httpmarco.polocloud.shared.template.Template;
import dev.httpmarco.polocloud.v1.templates.TemplateControllerGrpc;
import dev.httpmarco.polocloud.v1.templates.TemplateFindRequest;
import io.grpc.ManagedChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public final class TemplateProvider implements SharedTemplateProvider<Template> {

    private final TemplateControllerGrpc.TemplateControllerFutureStub futureStub;
    private final TemplateControllerGrpc.TemplateControllerBlockingStub blockingStub;

    public TemplateProvider(ManagedChannel channel) {
        this.blockingStub = TemplateControllerGrpc.newBlockingStub(channel);
        this.futureStub = TemplateControllerGrpc.newFutureStub(channel);
    }

    @Override
    public @NotNull List<Template> findAll() {
        return blockingStub.find(TemplateFindRequest.getDefaultInstance()).getTemplateList().stream().map(Template.Companion::fromSnapshot).toList();
    }

    @Override
    @Nullable
    public Template find(@NotNull String name) {
        return blockingStub.find(TemplateFindRequest.newBuilder().setName(name).build()).getTemplateList().stream().map(Template.Companion::fromSnapshot).findFirst().orElse(null);
    }

    @Override
    public @NotNull CompletableFuture<List<Template>> findAllAsync() {
        return FutureConverterKt.completableFromGuava(futureStub.find(TemplateFindRequest.getDefaultInstance()), it -> it.getTemplateList().stream().map(Template.Companion::fromSnapshot).toList());
    }

    @Override
    @NotNull
    public CompletableFuture<Template> findAsync(@NotNull String name) {
        return FutureConverterKt.completableFromGuava(futureStub.find(TemplateFindRequest.newBuilder().setName(name).build()), it -> it.getTemplateList().stream().map(Template.Companion::fromSnapshot).findFirst().orElse(null));
    }
}
