import { trace } from '@opentelemetry/api';

function attachToSpan(level, message, data = {}) {
  const span = trace.getActiveSpan();
  if (!span) return;

  //i can simplify i want to show detailed logs (doesn't work yet)
  let json = undefined;
  try {
    json = (data && typeof data === 'object')
      ? JSON.stringify(data)
      : String(data);
  } catch (_) {
    json = String(data);
  }

  span.addEvent(level, {
    message,
    data: json,
  });
}

export const logInfo = (message, data = {}) => {
  console.info("[INFO]", message, data);
  attachToSpan("info", message, data);
};

export const logError = (message, error = {}) => {
  console.error("[ERROR]", message, error);
  attachToSpan("error", message, {
    error: error?.message || error,
  });
};

export const logDebug = (message, data = {}) => {
  console.debug("[DEBUG]", message, data);
  attachToSpan("debug", message, data);
};
