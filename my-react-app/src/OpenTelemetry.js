import { WebTracerProvider } from '@opentelemetry/sdk-trace-web';
import { SimpleSpanProcessor } from '@opentelemetry/sdk-trace-base';
import { ZipkinExporter } from '@opentelemetry/exporter-zipkin';
import { registerInstrumentations } from '@opentelemetry/instrumentation';
import { DocumentLoadInstrumentation } from '@opentelemetry/instrumentation-document-load';
import { FetchInstrumentation } from '@opentelemetry/instrumentation-fetch';

const zipkinExporter = new ZipkinExporter({
  url: 'http://localhost:9411/api/v2/spans',
});

const provider = new WebTracerProvider({
  resource: {
    attributes: {
      'service.name': 'frontend',
    },
  },
  spanProcessors: [
    new SimpleSpanProcessor(zipkinExporter)
  ]
});

provider.register();

registerInstrumentations({
  instrumentations: [
    new DocumentLoadInstrumentation(),
    new FetchInstrumentation({
      propagateTraceHeaderCorsUrls: [/http:\/\/localhost:8080/],
    }),
  ],
});

//console.log("OpenTelemetry init");
