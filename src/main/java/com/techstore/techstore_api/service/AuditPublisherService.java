package com.techstore.techstore_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techstore.techstore_api.dto.AuditEventDTO;
import com.techstore.techstore_api.model.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.time.Instant;

@Service
public class AuditPublisherService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuditPublisherService.class);

    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.sqs.audit-queue-url:}")
    private String auditQueueUrl;

    public AuditPublisherService(SqsAsyncClient sqsAsyncClient) {

        this.sqsAsyncClient = sqsAsyncClient;
    }

    public void publicarEvento(String accion, Producto producto) {
        if (auditQueueUrl == null || auditQueueUrl.isBlank()) {
            logger.warn("SQS_QUEUE_URL no configurada. No se envio auditoria {}", accion);
            return;
        }

        try {
            AuditEventDTO evento = new AuditEventDTO(
                    accion,
                    producto.getId(),
                    producto.getNombre(),
                    obtenerUsuarioAutenticado(),
                    Instant.now().toString()
            );

            String mensajeJson =
                    objectMapper.writeValueAsString(evento);

            SendMessageRequest request =
                    SendMessageRequest.builder()
                            .queueUrl(auditQueueUrl)
                            .messageBody(mensajeJson)
                            .build();

            sqsAsyncClient.sendMessage(request)
                    .whenComplete((respuesta, error) -> {
                        if (error != null) {
                            logger.error("Error enviando auditoria a SQS", error);
                        } else {
                            logger.info("Auditoria enviada a SQS. MessageId: {}",
                                    respuesta.messageId());
                        }
                    });

        } catch (Exception e) {
            logger.error("No se pudo construir el mensaje de auditoria", e);
        }
    }

    private String obtenerUsuarioAutenticado() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            return "desconocido";
        }

        return authentication.getName();
    }
}
