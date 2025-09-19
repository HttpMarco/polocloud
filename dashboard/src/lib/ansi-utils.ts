export interface AnsiToHtmlOptions {
  removeColors?: boolean;
  convertToHtml?: boolean;
}

export function stripAnsiCodes(text: string): string {
  return text.replace(/\x1b\[[0-9;]*[mGKHf]|\x1b\[[\d;]*[mGKHfABCDsuJK]|\[\d+m|\[m/g, '');
}

export function ansiToHtml(text: string): string {
  const colorMap: Record<string, string> = {
    '30': 'color: #000000',
    '31': 'color: #ff0000',
    '32': 'color: #00ff00',
    '33': 'color: #ffff00',
    '34': 'color: #0000ff',
    '35': 'color: #ff00ff',
    '36': 'color: #00ffff',
    '37': 'color: #ffffff',
    '90': 'color: #808080',
    '91': 'color: #ff6666',
    '92': 'color: #66ff66',
    '93': 'color: #ffff66',
    '94': 'color: #6666ff',
    '95': 'color: #ff66ff',
    '96': 'color: #66ffff',
    '97': 'color: #ffffff',
    '0': '',
    '1': 'font-weight: bold',
    '2': 'opacity: 0.7',
    '4': 'text-decoration: underline',
  };

  let result = text;
  let openSpans = 0;

  result = result.replace(/\x1b\[([0-9;]*)m/g, (match, codes) => {
    if (codes === '0' || codes === '') {
      const closeSpans = '</span>'.repeat(openSpans);
      openSpans = 0;
      return closeSpans;
    }

    const codeList = codes.split(';');
    const styles: string[] = [];

    codeList.forEach((code: string) => {
      if (colorMap[code]) {
        styles.push(colorMap[code]);
      }
    });

    if (styles.length > 0) {
      openSpans++;
      return `<span style="${styles.join('; ')}">`;
    }

    return '';
  });

  result += '</span>'.repeat(openSpans);

  return result;
}

export function processTerminalLog(rawLog: string, options: AnsiToHtmlOptions = {}): string {
  const { removeColors = true, convertToHtml = false } = options;

  if (convertToHtml) {
    return ansiToHtml(rawLog);
  }

  if (removeColors) {
    return stripAnsiCodes(rawLog);
  }

  return rawLog;
}

export function extractTimestamp(logMessage: string): { timestamp: string; message: string } {
  const timestampRegex = /^\[(\d{2}:\d{2}:\d{2})\]\s*/;
  const match = logMessage.match(timestampRegex);

  if (match) {
    return {
      timestamp: match[1],
      message: logMessage.substring(match[0].length)
    };
  }

  return {
    timestamp: new Date().toLocaleTimeString('de-DE', { hour12: false }),
    message: logMessage
  };
}
