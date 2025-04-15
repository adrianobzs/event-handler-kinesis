import json
import boto3
import base64
import uuid
from datetime import datetime

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('events-consumer-2-test')

def lambda_handler(event, context):
    for record in event["Records"]:
        try:
            # Decodifica o payload do Kinesis
            payload = json.loads(base64.b64decode(record["kinesis"]["data"]).decode('utf-8'))
            event_id = payload.get("eventId", str(uuid.uuid4()))
            produtor_id = payload["produtorID"]
            consumidor_id = payload["consumidorID"]
            event_type = payload.get("type_event")

            # Verifica o tipo de evento e processa de acordo
            if event_type == 1:
                # Processamento para Tipo 1 (Financeiro)
                item = {
                    "pk": f"Transferencia_{produtor_id}_{consumidor_id}_{event_id}",
                    "sk": datetime.now().isoformat(),
                    "produtorID": produtor_id,
                    "consumidorID": consumidor_id,
                    "eventId": event_id,
                    "type_event": 1,
                    "origem": payload["origem"],
                    "destino": payload["destino"],
                    "valor": payload["valor"],
                    "timestamp": datetime.now().isoformat()
                }
            elif event_type == 2:
                # Processamento para Tipo 2 (Contato)
                item = {
                    "pk": f"ContatoUpdate_{produtor_id}_{consumidor_id}_{event_id}",
                    "sk": datetime.now().isoformat(),
                    "produtorID": produtor_id,
                    "consumidorID": consumidor_id,
                    "eventId": event_id,
                    "type_event": 2,
                    "nome": payload["nome"],
                    "telefone": payload["telefone"],
                    "timestamp": datetime.now().isoformat()
                }
            # Salva os dados na tabela
            table.put_item(Item=item)
            print(f"Registro salvo: {json.dumps(item)}")

        except Exception as e:
            print(f"Erro ao processar registro: {e}")
            raise

    return {
        "statusCode": 200,
        "body": json.dumps({"message": "Processamento concluído"})
    }