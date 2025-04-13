output "kinesis_stream_arn" {
  description = "ARN do Kinesis Stream"
  value       = module.kinesis.arn
}

output "lambda_function_arns" {
  description = "ARNs das funções Lambda"
  value = {
    consumer_1 = module.consumer_lambda_1.arn
    consumer_2 = module.consumer_lambda_2.arn
  }
}

output "dynamodb_table_names" {
  description = "Nomes das tabelas DynamoDB"
  value = {
    consumer_1 = module.dynamodb_consumer_1.name
    consumer_2 = module.dynamodb_consumer_2.name
  }
}

output "lambda_iam_role_arn" {
  description = "ARN da IAM Role compartilhada"
  value       = module.lambda_iam.arn
}