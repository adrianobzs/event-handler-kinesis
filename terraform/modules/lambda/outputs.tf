output "arn" {
  description = "lambda arn"
  value       = aws_lambda_function.this.arn
}

output "function_name" {
  description = "function name"
  value       = aws_lambda_function.this.function_name
}

output "invoke_arn" {
  description = "arn invoke"
  value       = aws_lambda_function.this.invoke_arn
}

output "role_arn" {
  description = "iam role"
  value       = aws_lambda_function.this.role
}