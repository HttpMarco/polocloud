import { XTerm } from 'react-xtermjs';

export const ServiceDetailTerminalLayout = () => {
  return (
    <div className="flex flex-1">
      <XTerm
        listeners={{
          onData: (data) => console.log(data),
        }}
        options={{ cursorBlink: true, scrollOnUserInput: true }}
        style={{ width: '100%', height: '100%' }}
      />
    </div>
  );
};
