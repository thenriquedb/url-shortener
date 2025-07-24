terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "6.4.0"
    }
  }

  backend "s3" {
    bucket         = "iac-url-shortener-state-bucket"
    key            = "terraform"
    region         = "us-east-1"
    encrypt        = true
  }
}

provider "aws" {
  profile = "thenriquedb"
  region  = "us-east-1"
}
