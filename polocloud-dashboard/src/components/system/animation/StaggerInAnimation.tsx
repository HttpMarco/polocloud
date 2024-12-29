import { motion } from 'framer-motion';

interface StaggerInAnimationProps {
  children: React.ReactNode;
}
export const StaggerInAnimation = ({ children }: StaggerInAnimationProps) => {
  return (
    <motion.div
      variants={{
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0 },
      }}
    >
      {children}
    </motion.div>
  );
};
