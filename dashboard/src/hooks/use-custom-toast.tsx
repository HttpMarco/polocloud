import { useState, useCallback } from 'react'
import { Toast } from '@/components/ui/toast'
import { createPortal } from 'react-dom'
import React from 'react'

interface ToastItem {
  id: string
  title: string
  description?: React.ReactNode
  variant?: "default" | "success" | "error" | "warning" | "info"
  duration?: number
}

export function useCustomToast() {
  const [toasts, setToasts] = useState<ToastItem[]>([])

  const toast = useCallback(({
    title,
    description,
    variant = "default",
    duration = 3000
  }: Omit<ToastItem, 'id'>) => {
    const id = Math.random().toString(36).substr(2, 9)
    
    setToasts(prev => [...prev, { id, title, description, variant, duration }])

    if (duration > 0) {
      setTimeout(() => {
        setToasts(prev => prev.filter(t => t.id !== id))
      }, duration)
    }
  }, [])

  const dismiss = useCallback((id: string) => {
    setToasts(prev => prev.filter(t => t.id !== id))
  }, [])

  const ToastContainer = () => {
    if (typeof window === 'undefined') return null

    return createPortal(
      React.createElement('div', { className: "fixed top-4 right-4 z-50 space-y-2" },
        toasts.map((toastItem) =>
          React.createElement(Toast, {
            key: toastItem.id,
            title: toastItem.title,
            description: toastItem.description,
            variant: toastItem.variant,
            className: "animate-in slide-in-from-right-full duration-300"
          })
        )
      ),
      document.body
    )
  }

  return {
    toast,
    dismiss,
    ToastContainer
  }
}
