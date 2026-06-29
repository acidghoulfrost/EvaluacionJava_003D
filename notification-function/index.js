exports.handler = async (event) => {
  console.log("[FaaS Audit] Funcion Serverless de Auditoria iniciada...");

  for (const record of event.Records || []) {
    let audit;

    try {
      audit = JSON.parse(record.body || "{}");
    } catch (error) {
      console.log("[FaaS Audit] Mensaje no valido:", record.body);
      continue;
    }

    console.log("=======================================================");
    console.log("[FaaS Audit] NUEVA AUDITORIA DE PRODUCTO DETECTADA EN SQS");
    console.log("=======================================================");
    console.log("Accion Realizada:", audit.accion);
    console.log("ID Producto:", audit.productoId);
    console.log("Nombre Producto:", audit.nombre);
    console.log("Usuario Operador:", audit.usuario);
    console.log("Fecha Operacion:", audit.fecha);
    console.log("=======================================================");
  }

  return {
    statusCode: 200,
    body: JSON.stringify("Procesamiento de auditoria de TechStore finalizado con exito.")
  };
};
