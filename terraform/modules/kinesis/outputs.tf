output "arn" {
  description = "Stream arn"
  value       = aws_kinesis_stream.this.arn
}

output "name" {
  description = "Stream name"
  value       = aws_kinesis_stream.this.name
}