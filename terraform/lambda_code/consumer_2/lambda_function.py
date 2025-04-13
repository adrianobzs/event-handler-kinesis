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
            # decodifica o payload do Kinesis
            payload = json.loads(base64.b64decode(record["kinesis"]["data"]).decode('utf-8'))
            produtor_id = payload["produtorID"]
            consumidor_id = payload["consumidorID"]
            
            # gera a partition key concatenando os ids do produtor - consumidor - uuid
            dynamo_pk = f"{produtor_id}_{consumidor_id}_{str(uuid.uuid4())}"
            
            # salva os dados na tabela
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