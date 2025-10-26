'use client';

export const dynamic = 'force-dynamic';

import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { Check } from 'lucide-react';
import { FlyingParticles } from './components/FlyingParticles';
import { StepIndicator } from './components/StepIndicator';
import { BackendConnectStep } from './components/BackendConnectStep';
import { AdminAccountStep } from './components/AdminAccountStep';
import { OverviewStep } from './components/OverviewStep';

interface OnboardingData {
    backendUrl: string;
    username: string;
    password: string;
    confirmPassword: string;
}

const steps = [
    {
        label: 'Backend Connect',
        description: 'Connect to your PoloCloud backend',
    },
    {
        label: 'Admin Account',
        description: 'Create your administrator account',
    },
    {
        label: 'Overview',
        description: 'Review and complete setup',
    },
];

export default function OnboardingPage() {
    const [currentStep, setCurrentStep] = useState(0);
    const [onboardingData, setOnboardingData] = useState<OnboardingData>({
        backendUrl: '',
        username: '',
        password: '',
        confirmPassword: '',
    });

    const handleNext = () => {
        if (currentStep < steps.length - 1) {
            setCurrentStep(currentStep + 1);
        } else {

            localStorage.setItem('backendIp', onboardingData.backendUrl);
            document.cookie = `backend_ip=${encodeURIComponent(onboardingData.backendUrl)}; path=/; max-age=${7 * 24 * 60 * 60}; secure; samesite=lax`;
            localStorage.setItem('onboarding-completed', 'true');
            window.location.href = '/';
        }
    };

    const renderStepContent = () => {
        switch (currentStep) {
            case 0:
                return <BackendConnectStep data={onboardingData} setData={setOnboardingData} onNext={handleNext} />;
            case 1:
                return <AdminAccountStep data={onboardingData} setData={setOnboardingData} onNext={handleNext} />;
            case 2:
                return <OverviewStep data={onboardingData} onNext={handleNext} />;
            default:
                return null;
        }
    };

    return (
        <div className="min-h-screen bg-gradient-to-br from-background via-background to-muted/20 relative overflow-hidden">
            <FlyingParticles />

            <div className="flex flex-col max-w-6xl w-full mx-auto p-8 relative z-10">
                <StepIndicator currentStep={currentStep} steps={steps} />

                <div className="flex flex-col lg:flex-row justify-between space-x-8">
                    <motion.div
                        className="flex flex-col w-full lg:w-96 space-y-6 lg:sticky top-4 self-center lg:self-start"
                        initial={{ opacity: 0, x: -30 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ duration: 0.8, delay: 0.8 }}
                    >
                        <motion.div
                            className="rounded-2xl p-6 bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-xl backdrop-blur-sm"
                            whileHover={{ y: -4, scale: 1.02 }}
                            transition={{ duration: 0.3 }}
                        >
                            <div className="mb-4">
                                <p className="font-semibold text-foreground">Setup Progress</p>
                            </div>

                            <div className="space-y-3">
                                <div className="flex items-center justify-between">
                                    <span className="text-sm text-muted-foreground">Backend URL:</span>
                                    <span className="text-sm font-medium text-foreground max-w-32 truncate">
                                        {onboardingData.backendUrl || 'Not set'}
                                    </span>
                                </div>
                                <div className="flex items-center justify-between">
                                    <span className="text-sm text-muted-foreground">Admin Account:</span>
                                    <span className="text-sm font-medium text-foreground max-w-32 truncate">
                                        {onboardingData.username || 'Not created'}
                                    </span>
                                </div>
                            </div>
                        </motion.div>

                        <motion.div
                            className="rounded-2xl p-6 bg-gradient-to-br from-card/80 via-card/60 to-card/80 border border-border/40 shadow-xl backdrop-blur-sm"
                            whileHover={{ y: -4, scale: 1.02 }}
                            transition={{ duration: 0.3 }}
                        >
                            <h3 className="font-semibold text-foreground mb-4 flex items-center gap-2">
                                <div className="size-2 bg-primary rounded-full animate-pulse"></div>
                                Setup Steps
                            </h3>
                            <div className="space-y-3">
                                {steps.map((step, index) => (
                                    <div
                                        key={index}
                                        className={`flex items-center space-x-3 p-3 rounded-lg transition-all duration-200 cursor-pointer ${
                                            index <= currentStep
                                                ? 'bg-primary/10 border border-primary/20'
                                                : 'bg-muted/20 border border-border/20'
                                        }`}
                                        onClick={() => index <= currentStep && setCurrentStep(index)}
                                    >
                                        <div className={`size-6 rounded-full flex items-center justify-center ${
                                            index < currentStep
                                                ? 'bg-green-500 text-white'
                                                : index === currentStep
                                                ? 'bg-primary text-primary-foreground'
                                                : 'bg-muted text-muted-foreground'
                                        }`}>
                                            {index < currentStep ? (
                                                <Check className="size-3" />
                                            ) : (
                                                <span className="text-xs font-medium">{index + 1}</span>
                                            )}
                                        </div>
                                        <div className="flex-1">
                                            <p className={`text-sm font-medium ${
                                                index <= currentStep ? 'text-foreground' : 'text-muted-foreground'
                                            }`}>
                                                {step.label}
                                            </p>
                                            <p className="text-xs text-muted-foreground">
                                                {step.description}
                                            </p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </motion.div>
                    </motion.div>

                    <motion.div
                        className="flex-1 flex flex-col"
                        initial={{ opacity: 0, x: 30 }}
                        animate={{ opacity: 1, x: 0 }}
                        transition={{ duration: 0.8, delay: 1.0 }}
                    >
                        <motion.div
                            className="mb-8"
                            key={currentStep}
                            initial={{ opacity: 0, y: 20 }}
                            animate={{ opacity: 1, y: 0 }}
                            transition={{ duration: 0.5 }}
                        >
                            <div className="flex items-center gap-3 mb-3">
                                <div>
                                    <h2 className="text-3xl font-bold text-foreground">
                                        {steps[currentStep].label}
                                    </h2>
                                    <p className="text-muted-foreground text-lg">
                                        {steps[currentStep].description}
                                    </p>
                                </div>
                            </div>

                            <div className="flex items-center gap-2 text-sm text-muted-foreground">
                                <span>Step {currentStep + 1} of {steps.length}</span>
                                <div className="size-1 bg-muted-foreground rounded-full"></div>
                                <span>{Math.round(((currentStep + 1) / steps.length) * 100)}% Complete</span>
                            </div>
                        </motion.div>

                        <div className="min-h-[500px]">
                            <AnimatePresence mode="wait">
                                {renderStepContent()}
                            </AnimatePresence>
                        </div>
                    </motion.div>
                </div>
            </div>
        </div>
    );
}
