resource "aws_dynamodb_table" "this" {
  name             = var.table_name
  billing_mode     = "PAY_PER_REQUEST"
  hash_key         = var.hash_key
  table_class      = "STANDARD"

  attribute {
    name = "pk"
    type = "S"
  }
}