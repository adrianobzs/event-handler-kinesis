variable "kinesis_shard_count" {
  description = "Número de shards do Kinesis"
  type        = number
}

variable "aws_region" {
  description = "aws region"
  type        = string
}

# ========== Cloud provider ==========================
provider "aws" {
  region = var.aws_region
}

# ============= MÓDULOS PREREQUISITOS ================
module "kinesis" {
  source              = "../../modules/kinesis"
  stream_name         = "events-stream-test"
  kinesis_shard_count = var.kinesis_shard_count
  retention_hours     = 24
}

# ======== IAM Role (lambda-kinesis) =================
module "lambda_iam" {
  source    = "../../modules/iam"
  role_name = "kinesis-lambda-role-test"
  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Principal = { Service = "lambda.amazonaws.com" },
      Action    = "sts:AssumeRole"
    }]
  })
  policy_document = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Action = [
          "kinesis:DescribeStream",
          "kinesis:DescribeStreamSummary",
          "kinesis:GetRecords",
          "kinesis:GetShardIterator",
          "kinesis:ListShards",
          "kinesis:ListStreams",
          "kinesis:SubscribeToShard",
          "dynamodb:PutItem",
          "dynamodb:GetItem",
          "logs:CreateLogGroup",
          "logs:CreateLogStream",
          "logs:PutLogEvents"
        ],
        Effect   = "Allow",
        Resource = "*"
      }
    ]
  })
}

# ============= LAMBDAS ================
module "consumer_lambda_1" {
  source        = "../../modules/lambda"
  function_name = "event-processor-consumer-1-test"
  source_code_path = "../../lambda_code/consumer_1"
  role_arn      = module.lambda_iam.arn
  kinesis_stream_arn = module.kinesis.arn
  dynamodb_table_name = module.dynamodb_consumer_1.name
}

module "consumer_lambda_2" {
  source        = "../../modules/lambda"
  function_name = "event-processor-consumer-2-test"
  source_code_path = "../../lambda_code/consumer_2"
  role_arn      = module.lambda_iam.arn
  kinesis_stream_arn = module.kinesis.arn
  dynamodb_table_name = module.dynamodb_consumer_2.name
}

# ============= DYNAMODB TABLES ================
module "dynamodb_consumer_1" {
  source     = "../../modules/dynamodb"
  table_name = "events-consumer-1-test"
  hash_key   = "pk"
}

module "dynamodb_consumer_2" {
  source     = "../../modules/dynamodb"
  table_name = "events-consumer-2-test"
  hash_key   = "pk"
}

# ============= EVENT MAPPINGS ================
resource "aws_lambda_event_source_mapping" "lambda_1" {
  event_source_arn  = module.kinesis.arn
  function_name     = module.consumer_lambda_1.arn
  starting_position = "LATEST"
  filter_criteria {
    filter {
      pattern = jsonencode({ "data" : {
        "consumidorID" : ["lambda1"],
        "produtorID" : [{"exists" : true}],
        "dummy_data" : [{"exists" : true}]
      }})
    }
  }
}

resource "aws_lambda_event_source_mapping" "lambda_2" {
  event_source_arn  = module.kinesis.arn
  function_name     = module.consumer_lambda_2.arn
  starting_position = "LATEST"
  filter_criteria {
    filter {
      pattern = jsonencode({ "data" : {
        "consumidorID" : ["lambda2"],
        "produtorID" : [{"exists" : true}],
        "dummy_data" : [{"exists" : true}]
      }})
    }
  }
}