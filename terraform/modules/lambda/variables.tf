variable "function_name" {
  description = "function name"
  type        = string
}

variable "handler" {
  description = "handler method handler"
  type        = string
  default     = "lambda_function.lambda_handler"
}

variable "runtime" {
  description = "lambda runtime"
  type        = string
  default     = "python3.9"
}

variable "role_arn" {
  description = "IAM Role"
  type        = string
}

variable "memory_size" {
  description = "ram im mb"
  type        = number
  default     = 128
}

variable "dynamodb_table_name" {
  description = "DynamoDB table name"
  type        = string
}

variable "timeout" {
  description = "timeout in seconds"
  type        = number
  default     = 3
}

variable "kinesis_stream_arn" {
  description = "kinesis stream arn"
  type        = string
  default     = null
}

variable "source_code_path" {
  description = "lambda function code"
  type        = string
}