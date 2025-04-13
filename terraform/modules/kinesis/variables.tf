variable "stream_name" {
  description = "kinesis Stream name"
  type        = string
}

variable "kinesis_shard_count" {
  description = "number of shards"
  type        = number
  default     = 2
}

variable "retention_hours" {
  description = "retention in hours"
  type        = number
  default     = 24
}
