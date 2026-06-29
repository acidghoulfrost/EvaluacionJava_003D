import json


def lambda_handler(event, context):
    print("[FaaS Audit] Funcion Serverless de Auditoria iniciada...")

    for record in event.get("Records", []):
        body = record.get("body", "{}")

        try:
            audit = json.loads(body)
        except json.JSONDecodeError:
            print(f"[FaaS Audit] Mensaje no valido: {body}")
            continue

        print("=======================================================")
        print("[FaaS Audit] NUEVA AUDITORIA DE PRODUCTO DETECTADA EN SQS")
        print("=======================================================")
        print(f"Accion Realizada: {audit.get('accion')}")
        print(f"ID Producto: {audit.get('productoId')}")
        print(f"Nombre Producto: {audit.get('nombre')}")
        print(f"Usuario Operador: {audit.get('usuario')}")
        print(f"Fecha Operacion: {audit.get('fecha')}")
        print("=======================================================")

    return {
        "statusCode": 200,
        "body": json.dumps("Procesamiento de auditoria de TechStore finalizado con exito.")
    }
