output "arn" {
  description = "dynamodb table arn"
  value       = aws_dynamodb_table.this.arn
}

output "name" {
  description = "dynamodb table name"
  value       = aws_dynamodb_table.this.name
}