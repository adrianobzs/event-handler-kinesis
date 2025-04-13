data "archive_file" "lambda_zip" {
  type        = "zip"
  source_dir  = var.source_code_path
  output_path = "${path.module}/../../lambda_zips/${var.function_name}.zip"
}

resource "aws_lambda_function" "this" {
  function_name    = var.function_name
  handler          = var.handler
  runtime          = var.runtime
  role             = var.role_arn
  filename         = data.archive_file.lambda_zip.output_path
  source_code_hash = data.archive_file.lambda_zip.output_base64sha256
  memory_size      = var.memory_size
  timeout          = var.timeout
  architectures    = ["x86_64"]
}