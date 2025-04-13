output "arn" {
  description = "iam role arn"
  value       = aws_iam_role.this.arn
}

output "name" {
  description = "iam role name"
  value       = aws_iam_role.this.name
}
