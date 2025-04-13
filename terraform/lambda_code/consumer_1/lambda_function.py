import json
import boto3
import base64
import uuid
from datetime import datetime

dynamodb = boto3.resource('dynamodb')
table = dynamodb.Table('events-consumer-1-test')

def lambda_handler(event, context):
    for record in event["Records"]:
        try:
            # Decodifica o payload do Kinesis
            payload = json.loads(base64.b64decode(record["kinesis"]["data"]).decode('utf-8'))
            produtor_id = payload["produtorID"]
            consumidor_id = payload["consumidorID"]
            
            # Gera uma Partition Key única para o DynamoDB
            dynamo_pk = f"{produtor_id}_{consumidor_id}_{str(uuid.uuid4())}"
            
            # Salva no DynamoDB sem risco de sobrescrita
            table.put_item(Item={
                "pk": dynamo_pk,  # Chave única composta
                "produtorID": produtor_id,
                "consumidorID": consumidor_id,
                "dummy_data": payload["dummy_data"],
                "timestamp": datetime.now().isoformat()
            })
            
            print(f"Registro salvo com PK: {dynamo_pk}")
            
        except Exception as e:
            print(f"Erro ao processar registro: {e}")
            raise

    return {"statusCode": 200}