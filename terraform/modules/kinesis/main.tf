resource "aws_kinesis_stream" "this" {
  name             = var.stream_name
  shard_count      = var.kinesis_shard_count
  retention_period = var.retention_hours
}