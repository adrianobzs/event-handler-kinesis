variable "table_name" {
  description = "dynamodb table name"
  type        = string
}

variable "hash_key" {
  description = "partition key"
  type        = string
}
