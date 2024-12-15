package dev.httpmarco.polocloud.node.terminal.setup.impl.defaults;

import dev.httpmarco.polocloud.node.terminal.setup.common.AbstractSetup;
import dev.httpmarco.polocloud.node.terminal.setup.impl.SelectQuestionImpl;

public final class OnboardingSetup extends AbstractSetup {

    public OnboardingSetup() {
        super("Onboarding-Setup");
    }

    @Override
    public void bindQuestion() {
        add(new SelectQuestionImpl(this,"What is your favourite platform?", "Spigot", "Paper", "Waterfall", "Velocity", "BungeeCord", "Minestom"));
        add(new SelectQuestionImpl(this,"Eula is ok?", "tes", "no"));

        this.start();
    }
}
